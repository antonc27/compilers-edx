import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

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

    private int i = -1;
    private int j = -1;

    private Map<String, String> refStringConsts = new HashMap<>();
    private Map<String, String> testStringConsts = new HashMap<>();

    private void checkAgainstRef(String programFilename) {
        try {
            List<String> file1 = Files.readAllLines(Paths.get(programFilename + "_ref.s"));
            List<String> file2 = Files.readAllLines(Paths.get(programFilename + "_test.s"));

            for (i = 0, j = 0; i < file1.size(); i++) {
                String refString = file1.get(i).trim();
                if (refString.startsWith("#")) {
                    // skip comments in ref
                    continue;
                }
                String testString = file2.get(j).trim();
                while (testString.startsWith("#")) {
                    // skip comments in test
                    j++;
                    testString = file2.get(j).trim();
                }

                // Ref version do not contain strings for No_class, SELF_TYPE and prim_slot
                // For a moment, skip entire string constant section from comparison
                if (refString.startsWith("str_const")) {
                    skipSectionWithPrefix(file1, file2, "str_const");
                    refString = file1.get(i).trim();
                    testString = file2.get(j).trim();
                }
                // skip int constant section too
                if (refString.startsWith("int_const")) {
                    skipSectionWithPrefix(file1, file2, "int_const");
                    refString = file1.get(i).trim();
                    testString = file2.get(j).trim();
                }

                if (refString.startsWith(".word\tstr_const")) {
                    assertTrue(testString.startsWith(".word\tstr_const"));
                    String refStrConst = lastToken(refString);
                    String testStrConst = lastToken(testString);

                    assertEquals(
                            refStringConsts.get(refStrConst),
                            testStringConsts.get(testStrConst),
                            "Failed at line " + (i+1) + " in ref and line " + (j+1) + " in test");
                    j++;
                    continue;
                }

                assertEquals(
                        refString.replaceAll("[\t ]+", " "),
                        testString.replaceAll("[\t ]+", " "),
                        "Failed at line " + (i+1) + " in ref and line " + (j+1) + " in test");
                j++;
            }
        } catch (IOException e) {
            e.printStackTrace();
            fail();
        }
    }

    private void skipSectionWithPrefix(List<String> file1, List<String> file2, String prefix) {
        String refString = file1.get(i).trim();
        String testString = file2.get(j).trim();

        // at least, check we're having string constant section in test
        assertTrue(testString.startsWith(prefix));

        String refStrConst = "";
        if (prefix.equals("str_const")) {
            refStrConst = refString.substring(0, refString.length() - 1);
            //refStringConsts.put(refStrConst, "");
        }
        while (refString.startsWith(prefix)) {
            i++;
            refString = file1.get(i).trim();
            while (refString.startsWith(".")) {
                if (prefix.equals("str_const") && refString.startsWith(".ascii")) {
                    refStringConsts.put(refStrConst, lastToken(refString));
                }
                i++;
                refString = file1.get(i).trim();
            }
        }

        String testStrConst = "";
        if (prefix.equals("str_const")) {
            testStrConst = testString.substring(0, refString.length() - 1);
            //testStringConsts.put(testStrConst, "");
        }
        while (testString.startsWith(prefix)) {
            j++;
            testString = file2.get(j).trim();
            while (testString.startsWith(".")) {
                if (prefix.equals("str_const") && testString.startsWith(".ascii")) {
                    testStringConsts.put(testStrConst, lastToken(testString));
                }
                j++;
                testString = file2.get(j).trim();
            }
        }
    }

    private String lastToken(String str) {
        String[] s = str.replaceAll("[\t ]+", " ").split(" ");
        return s[s.length - 1];
    }
}
