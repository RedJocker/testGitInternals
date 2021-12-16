package gitinternals.commands

import gitinternals.GitObjectParser
import gitinternals.UI
import gitinternals.commands.utils.getCurrentBranch
import gitinternals.commands.utils.getHeadsFolder
import gitinternals.commands.utils.validateHash
import gitinternals.getInflatedRawFileInput
import gitinternals.gitobjects.Commit
import gitinternals.log

class Log(override val gitDirectory: String) : GitCommand  {
    override fun execute() {
        val currentBranch: String = UI.getBranchFromInput()

        val headsFolder = getHeadsFolder(gitDirectory)
        val headFile = headsFolder.resolve(currentBranch).also {
            if(it.exists().not()) error("could not find head file at ${it.path}")
            else if(it.isFile.not()) error("head file at ${it.path} is not a file")
        }

        val  headFileContent = headFile.bufferedReader().readLines().also {
            if(it.size != 1) error("invalid head file content at ${headFile.path}")
            validateHash(it[0])
        }

        val headCommitHash = headFileContent[0]

        val resultLog = buildLog(headCommitHash)
        log(resultLog)
        println(resultLog)
    }

    private tailrec fun buildLog(currentCommit: String, accumulatedLog: StringBuilder = StringBuilder()): String {
        val commitObject = appendToLogReturnCommitObject(currentCommit, accumulatedLog)

        return if(commitObject.parents.isEmpty()) {
            accumulatedLog.toString()
        } else if(commitObject.parents.size > 1) {
            commitObject.parents.drop(1).reversed().forEach {
                appendToLogReturnCommitObject(it, accumulatedLog, isMerged = true)
            }
            buildLog(commitObject.parents[0], accumulatedLog)
        } else {
            buildLog(commitObject.parents[0], accumulatedLog)
        }
    }

    private fun appendToLogReturnCommitObject(currentCommit: String, accumulatedLog: StringBuilder, isMerged: Boolean=false): Commit {
        val path = "$gitDirectory/objects/${currentCommit.take(2)}/${currentCommit.drop(2)}"
        val rawInput = getInflatedRawFileInput(path)
        val gitObjectParser = GitObjectParser(rawInput)
        val commitObject = gitObjectParser.content as Commit
        val mergedString = if(isMerged) " (merged)" else ""
        val toLog = "Commit: $currentCommit$mergedString\n" +
                "${commitObject.committerInfo}\n" +
                "${commitObject.commitMessage}\n"

        accumulatedLog.append(toLog)
        return commitObject
    }
}