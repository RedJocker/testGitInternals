package gitinternals.commands

import gitinternals.GitObjectParser
import gitinternals.UI
import gitinternals.getInflatedRawFileInput
import gitinternals.gitobjects.GitObject
import gitinternals.log

class CatFile(override val gitDirectory: String): GitCommand {
    override fun execute() {
        val hash = UI.getGitObjectHashFromInput()
        val filePath = "$gitDirectory/objects/${hash.take(2)}/${hash.drop(2)}"
        val rawInput = getInflatedRawFileInput(filePath)
        val gitObjectParser = GitObjectParser(rawInput)

        log("raw content:")
        log(gitObjectParser.parsedInput)

        val content: GitObject = gitObjectParser.content
        println(content)
    }
}