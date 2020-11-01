import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class CgenTest {
    @Test
    public void testExample() {
        String programFilename = "example.cl";
        String programPath = "tests/" + programFilename;

        runCodeGen(programPath);
        checkAgainstRef(programPath);
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

    private void checkAgainstRef(String programFilename) {
        try {
            List<String> file1 = Files.readAllLines(Paths.get(programFilename + "_ref.s"));
            List<String> file2 = Files.readAllLines(Paths.get(programFilename + "_test.s"));

            for (int i = 0, j = 0; i < file1.size(); i++) {
                String refString = file1.get(i);
                if (refString.trim().startsWith("#")) {
                    // skip comments in ref
                    continue;
                }
                String testString = file2.get(j);
                while (testString.trim().startsWith("#")) {
                    // skip comments in test
                    j++;
                    testString = file2.get(j);
                }
                assertEquals(
                        refString.replaceAll("[\t ]+", " "),
                        testString.replaceAll("[\t ]+", " "),
                        "Failed at line " + (i+1) + " in ref and line " + (j+1) + "in test");
                j++;
            }
        } catch (IOException e) {
            e.printStackTrace();
            fail();
        }
    }
}
