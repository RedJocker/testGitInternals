package gitinternals

object UI {
    fun getCommandFromInput(): String {
        return readLine("Enter command:")?.lowercase() ?: error("failed to receive command")
    }

    fun getGitDirectoryFromInput(): String {
        return readLine("Enter .git directory location:") ?: error("failed to receive directory location")
    }

    fun getGitObjectHashFromInput():String {
        return readLine("Enter git object hash:") ?: error("failed to receive hash")
    }
}