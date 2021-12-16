package gitinternals

import gitinternals.gitobjects.GitObject

fun main() {
    val filepath = getFilePathFromInput()
    val rawInput = getInflatedRawInput(filepath)
    val gitObjectParser = GitObjectParser(rawInput)

    log("raw content:")
    log(gitObjectParser.parsedInput)

    val content: GitObject = gitObjectParser.content
    println(content)
}




