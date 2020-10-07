import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.Permission;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SemantTest {
    @Test
    public void testExample() {
        String programFilename = "tests/double_class.cl";

        runSemant(programFilename);
        checkAgainstRef(programFilename);
    }

    @Test
    public void testExample2() {
        String programFilename = "tests/basic_redefine.cl";

        runSemant(programFilename);
        checkAgainstRef(programFilename);
    }

    @Test
    public void testExample3() {
        String programFilename = "tests/basic_inheritance.cl";

        runSemant(programFilename);
        checkAgainstRef(programFilename);
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