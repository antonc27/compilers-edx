import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.Permission;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SemantTest {
    @ParameterizedTest
    @ValueSource(strings = {
        "double_class.cl", "basic_redefine.cl", "basic_inheritance.cl", "missing_class.cl", "missing_main.cl",
        "inheritance_cycle.cl", "bool_int_const.cl", "invalid_return_type.cl", "str_const.cl", "valid_arithmetic.cl",
        "invalid_arithmetic.cl", "method_return_subtype.cl", "simple_arithmetic.cl", "return_type_not_exist.cl",
        "valid_letinit.cl", "letinit_out_of_scope.cl", "letbadinit.cl", "letnoinit.cl"
    })
    public void testExample(String programFilename) {
        String programPath = "tests/" + programFilename;

        runSemant(programPath);
        checkAgainstRef(programPath);
    }

    private void runSemant(String programFilename) {
        InputStream stdIn = System.in;
        PrintStream stdOut = System.out;
        PrintStream stdErr = System.err;

        String cmd = "../PA2J/lexer " + programFilename + " | ../PA3J/parser " + programFilename;

        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command("/bin/sh", "-c", cmd);

        try {
            Process process = processBuilder.start();

            System.setIn(process.getInputStream());
            PrintStream ps = new PrintStream(new FileOutputStream(programFilename + "_test.txt"));
            System.setOut(ps);
            System.setErr(ps);

            runSemantWithoutExit(programFilename);

            process.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            System.setIn(stdIn);
            System.setOut(stdOut);
            System.setErr(stdErr);
        }
    }

    private void runSemantWithoutExit(String programFilename) {
        //Before running the external Command
        MySecurityManager secManager = new MySecurityManager();
        System.setSecurityManager(secManager);

        try {
            Semant.main(new String[] { programFilename });
        } catch (SecurityException e) {
            //Do something if the external code used System.exit()
        }
    }

    private void checkAgainstRef(String programFilename) {
        try {
            List<String> file1 = Files.readAllLines(Paths.get(programFilename + "_ref.txt"));
            List<String> file2 = Files.readAllLines(Paths.get(programFilename + "_test.txt"));

            for (int i = 0; i < file1.size(); i++) {
                String refString = file1.get(i);
                if (refString.trim().startsWith("#")) {
                    // skip line numbers due to bug in ref parser
                    continue;
                }
                String testString = file2.get(i);
                assertEquals(
                        refString.replaceFirst(":\\d+:", ""),
                        testString.replaceFirst(":\\d+:", ""),
                        "Failed at line " + (i+1));
            }
        } catch (IOException e) {
            e.printStackTrace();
            fail();
        }
    }

    private static class MySecurityManager extends SecurityManager {
        @Override public void checkExit(int status) {
            throw new SecurityException();
        }

        @Override public void checkPermission(Permission perm) {
            // Allow other activities by default
        }
    }
}