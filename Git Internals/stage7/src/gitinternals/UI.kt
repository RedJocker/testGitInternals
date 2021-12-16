package gitinternals

import gitinternals.commands.utils.validateHash

object UI {
    fun getCommandFromInput(): String {
        return readLine("Enter command:")?.lowercase() ?: error("failed to receive command")
    }

    fun getGitDirectoryFromInput(): String {
        return readLine("Enter .git directory location:") ?: error("failed to receive directory location")
    }

    fun getGitObjectHashFromInput(): Hash {
        val hash: Hash = readLine("Enter git object hash:") ?: error("failed to receive hash")
        return hash.also(Hash::validateHash)

    }
    fun getGitCommitHashFromInput(): Hash {
        val hash: Hash = readLine("Enter commit-hash:") ?: error("failed to receive hash")
        return hash.also(Hash::validateHash)
    }

    fun getBranchFromInput(): String {
        return readLine("Enter branch name:") ?: error("failed to receive branch name")
    }
}