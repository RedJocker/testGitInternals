package gitinternals.commands

import gitinternals.*
import gitinternals.commands.utils.getGitObjectPath
import gitinternals.gitobjects.GitObject

class CatFile(override val gitDirectory: String): GitCommand {
    override fun execute() {
        val hash: Hash = UI.getGitObjectHashFromInput()
        val filePath = hash.getGitObjectPath(gitDirectory)
        val rawInput = getInflatedRawFileInput(filePath)
        val gitObjectParser = GitObjectParser(rawInput)

        log("raw content:")
        log(gitObjectParser.parsedInput)

        val content: GitObject = gitObjectParser.content
        log("git object:")
        log(content)
        println(content)
    }
}