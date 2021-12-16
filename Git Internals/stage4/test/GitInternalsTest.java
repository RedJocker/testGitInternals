import org.hyperskill.hstest.stage.StageTest;
import org.hyperskill.hstest.testcase.CheckResult;
import org.hyperskill.hstest.testcase.TestCase;

import java.util.Arrays;
import java.util.List;

// version 1.2
public class GitInternalsTest extends StageTest<List<String>> {

    private final String gitOnePath = "test/gitone\n";
    private final String gitTwoPath = "test/gittwo\n";

    @Override
    public List<TestCase<List<String>>> generate() {

        return Arrays.asList(
                new TestCase<List<String>>()
                        .setInput(
                                gitOnePath +
                                        "0eee6a98471a350b2c2316313114185ecaf82f0e\n")
                        .setAttach(Arrays.asList(
                                "Enter .git directory location:",
                                "Enter git object hash:",
                                "*COMMIT*",
                                "tree: 79401ddb0e2c0fe0472c813754dd4a8873b66a84",
                                "parents: 12a4717e84b5e414f93cc91ca50a6d5a6c3563a0",
                                "author: Smith mr.smith@matrix original timestamp: 2020-03-29 17:18:20 +03:00",
                                "committer: Cypher cypher@matrix commit timestamp: 2020-03-29 17:25:52 +03:00",
                                "commit message:",
                                "get docs from feature1")),
                new TestCase<List<String>>()
                        .setInput(
                                gitOnePath +
                                        "490f96725348e92770d3c6bab9ec532564b7ebe0\n")
                        .setAttach(Arrays.asList(
                                "Enter .git directory location:",
                                "Enter git object hash:",
                                "*BLOB*",
                                "fun main() {",
                                "    while(true) {",
                                "        println(\"Hello Hyperskill student!\")",
                                "    }",
                                "} ")),
                new TestCase<List<String>>()
                        .setInput(
                                gitOnePath +
                                        "fb043556c251cb450a0d55e4ceb1ff35e12029c3\n")
                        .setAttach(Arrays.asList(
                                "Enter .git directory location:",
                                "Enter git object hash:",
                                "*TREE*",
                                "100644 2b26c15c04375d90203783fb4c2a45ff04b571a6 main.kt",  //fixed
                                "100644 4a8abe7b618ddf9c55adbea359ce891775794a61 readme.txt")),
                new TestCase<List<String>>()
                        .setInput(
                                gitOnePath +
                                        "ad3a818dc87b9940935b24a5aa93fac00f086bf9\n")
                        .setAttach(Arrays.asList(
                                "Enter .git directory location:",
                                "Enter git object hash:",
                                "*TREE*",
                                "100644 c2d888ec7f06b71b5ec8712b0464d1ca7a0a9ffc main.kt")),  //fixed
                new TestCase<List<String>>()
                        .setInput(
                                gitOnePath +
                                        "109e8050b41bd10b81be0a51a5e67327f5609551\n")
                        .setAttach(Arrays.asList(
                                "Enter .git directory location:",
                                "Enter git object hash:",
                                "*TREE*",
                                "100644 2b26c15c04375d90203783fb4c2a45ff04b571a6 main.kt",  //fixed
                                "100644 f674b5d3a4c6cef5815b4e72ef2ea1bbe46b786b readme.txt",
                                "40000 74198c849dbbcd51d060c59253a4757eedb9bd12 some-folder")),
                new TestCase<List<String>>()
                        .setInput(
                                gitOnePath +
                                        "74198c849dbbcd51d060c59253a4757eedb9bd12\n")
                        .setAttach(Arrays.asList(
                                "Enter .git directory location:",
                                "Enter git object hash:",
                                "*TREE*",
                                "100644 83cc19099347924da28890838ad5e698da8504a3 qq.txt")),  //fixed

                new TestCase<List<String>>()
                        .setInput(
                                gitOnePath +
                                        "74198c849dbbcd51d060c59253a4757eedb9bd12\n")
                        .setAttach(Arrays.asList(
                                "Enter .git directory location:",
                                "Enter git object hash:",
                                "*TREE*",
                                "100644 83cc19099347924da28890838ad5e698da8504a3 qq.txt")),  //fixed
                new TestCase<List<String>>()  // added
                        .setInput(
                                gitTwoPath +
                                        "31cddcbd00e715688cd127ad20c2846f9ed98223\n")
                        .setAttach(Arrays.asList(
                                "Enter .git directory location:",
                                "Enter git object hash:",
                                "*COMMIT*",
                                "tree: aaa96ced2d9a1c8e72c56b253a0e2fe78393feb7",
                                "author: Kalinka Kali.k4@email.com original timestamp: 2021-12-11 22:31:36 -03:00",
                                "committer: Kalinka Kali.k4@email.com commit timestamp: 2021-12-11 22:31:36 -03:00",
                                "commit message:",
                                "simple hello")),
                new TestCase<List<String>>()  // added
                        .setInput(
                                gitTwoPath +
                                "dcec4e51e2ce4a46a6206d0d4ab33fa99d8b1ab5\n")
                        .setAttach(Arrays.asList(
                                "Enter .git directory location:",
                                "Enter git object hash:",
                                "*COMMIT*",
                                "tree: d128f76a96c56ac4373717d3fbba4fa5875ca68f",
                                "parents: 5ad3239e54ba7c533d9f215a13ac82d14197cd8f | d2c5bedbb2c46945fd84f2ad209a7d4ee047f7f9",
                                "author: Kalinka Kali.k4@email.com original timestamp: 2021-12-11 22:49:02 -03:00",
                                "committer: Kalinka Kali.k4@email.com commit timestamp: 2021-12-11 22:49:02 -03:00",
                                "commit message:",
                                "awsome hello")),
                new TestCase<List<String>>()  // added
                        .setInput(
                                gitTwoPath +
                                        "aaa96ced2d9a1c8e72c56b253a0e2fe78393feb7\n")
                        .setAttach(Arrays.asList(
                                "Enter .git directory location:",
                                "Enter git object hash:",
                                "*TREE*",
                                "100644 ce013625030ba8dba906f756967f9e9ca394464a hello.txt"))
        );
    }

    @Override
    public CheckResult check(String reply, List<String> expectedOutput) {
        List<String> lines = Arrays.asList(reply.split("(\\r\\n|\\r|\\n)"));

        if (lines.size() != expectedOutput.size()) {
            return CheckResult.wrong(String.format(
                    "Number of lines in your output (%d) does not match expected value(%d)",
                    lines.size(), expectedOutput.size()));
        }

        for (int i = 0; i < lines.size(); i++) {
            if (!lines.get(i).equals(expectedOutput.get(i))) {
                return CheckResult.wrong(String.format(
                        "Output text at line (%d) (%s) does not match expected (%s)",
                        i, lines.get(i), expectedOutput.get(i)));
            }
        }


        return CheckResult.correct();
    }
}
