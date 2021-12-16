package gitinternals.commands

interface GitCommand {
    val gitDirectory: String
    fun execute()
}