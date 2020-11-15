import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CgenTest {
    @ParameterizedTest
    @ValueSource(strings = {
            "example.cl", "hello_world.cl", "two_args.cl"
    })
    public void testExample(String programFilename) {
        String programPath = "tests/" + programFilename;

        runCodeGen(programPath);

        runSpim(programPath + "_ref.s", programPath + "_ref.out");
        runSpim(programPath + "_test.s", programPath + "_test.out");

        checkOutput(programPath + "_ref.out", programPath + "_test.out");
    }

    private void runCodeGen(String programFilename) {
        InputStream stdIn = System.in;

        String cmd = "../PA2J/lexer " + programFilename
                + " | ../PA3J/parser " + programFilename
                + " | ../PA4J/semant " + programFilename;

        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command("/bin/sh", "-c", cmd);

        try {
            Process process = processBuilder.start();

            System.setIn(process.getInputStream());
            Cgen.main(new String[] { "-o", programFilename + "_test.s", programFilename });

            process.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.setIn(stdIn);
        }
    }

    private void runSpim(String programFilename, String outputFilename) {
        String cmd = "/usr/local/bin/spim -exception_file /usr/local/class/cs143/cool/lib/trap.handler -file " + programFilename;

        ProcessBuilder processBuilder = new ProcessBuilder().inheritIO();
        processBuilder.redirectErrorStream(true);
        processBuilder.redirectOutput(new File(outputFilename));
        processBuilder.command("/bin/sh", "-c", cmd);

        try {
            Process process = processBuilder.start();

            process.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            fail();
        }
    }

    private void checkOutput(String refFilename, String testFilename) {
        try {
            List<String> file1 = Files.readAllLines(Paths.get(refFilename));
            assertTrue(file1.size() > 1, "Ref program should be executed");
            List<String> file2 = Files.readAllLines(Paths.get(testFilename));

            for (int i = 0; i < file1.size(); i++) {
                String refString = file1.get(i);
                String testString = file2.get(i);
                assertEquals(refString, testString, "Failed at line " + (i+1));
            }
        } catch (IOException e) {
            e.printStackTrace();
            fail();
        }
    }
}
