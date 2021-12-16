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
                                        "618383db6d7ee3bd2e97b871205f113b6a3ba854\n")
                        .setAttach(Arrays.asList(
                                "Enter .git directory location:",
                                "Enter git object hash:",
                                "*BLOB*",
                                "Hello world! ")),
                new TestCase<List<String>>()
                        .setInput(
                                gitOnePath +
                                        "39a0337532d7720acc90497043e2ade92c386939\n")
                        .setAttach(Arrays.asList(
                                "Enter .git directory location:",
                                "Enter git object hash:",
                                "*COMMIT*",
                                "tree: 998b5fa98f0fae83e6cb24a8815b8923aead7ee0",
                                "parents: 6d537a47eddc11f866bcc2013703bf31bfcf9ed8",
                                "author: Neo neo@matrix original timestamp: 2020-04-04 09:59:23 +03:00",
                                "committer: Neo neo@matrix commit timestamp: 2020-04-04 09:59:23 +03:00",
                                "commit message:",
                                "this commit message will have multiple lines",
                                "we need multiple lines commit message for test purposes",
                                "3",
                                "4",
                                "5")),
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
                                "awsome hello"))
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
