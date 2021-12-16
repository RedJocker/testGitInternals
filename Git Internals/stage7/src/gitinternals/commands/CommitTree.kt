package gitinternals.commands

import gitinternals.GitObjectParser
import gitinternals.UI
import gitinternals.commands.utils.getGitObjectPath
import gitinternals.getInflatedRawFileInput
import gitinternals.gitobjects.Commit
import gitinternals.gitobjects.Tree
import gitinternals.log

class CommitTree(override val gitDirectory: String): GitCommand {
    override fun execute() {
        val commitHash = UI.getGitCommitHashFromInput()
        val path = commitHash.getGitObjectPath(gitDirectory)
        val rawInput = getInflatedRawFileInput(path)
        val gitObjectParser = GitObjectParser(rawInput)
        val commitObject = if(gitObjectParser.content is Commit) {
            gitObjectParser.content as Commit
        } else {
            error("was not a commit the object with hash $commitHash")
        }

        val response = buildTree(commitObject.tree)

        log(response)
        println(response)

    }

    private fun buildTree(
        currentHash: String,
        accumulatedResponse: StringBuilder = StringBuilder(),
        currentFolder: String = ""): String {


        val rawInput = getInflatedRawFileInput(currentHash.getGitObjectPath(gitDirectory))
        val gitObjectParser = GitObjectParser(rawInput)

        val treeObject : Tree = if(gitObjectParser.content is Tree) {
            gitObjectParser.content as Tree
        } else {
            error("was not a tree the object with hash $currentHash")
        }

        val filesList = treeObject.listFileInfo.map {
            val rawInput = getInflatedRawFileInput(it.properHash.getGitObjectPath(gitDirectory))
            it to GitObjectParser(rawInput)
        }

        filesList.filter { it.second.type == "blob" }.forEach {
            accumulatedResponse.append("$currentFolder${it.first.fileName}\n")
        }

        filesList.filter { it.second.type == "tree" }.forEach {
            buildTree(it.first.properHash, accumulatedResponse, "$currentFolder${it.first.fileName}/")
        }

        return accumulatedResponse.toString()
    }



}
