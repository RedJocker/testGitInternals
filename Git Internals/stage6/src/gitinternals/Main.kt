package gitinternals

import gitinternals.commands.CatFile
import gitinternals.commands.GitCommand
import gitinternals.commands.ListBranches
import gitinternals.commands.Log


fun main() {
    val gitDirectory = UI.getGitDirectoryFromInput()
    val command = UI.getCommandFromInput()
    controller(command, gitDirectory)
}


private fun controller(commandStr: String, gitDirectory: String) {
    log("command: $commandStr")
    log("gitDirectory: $gitDirectory")

    val command : GitCommand = when(commandStr) {
        "cat-file" -> CatFile(gitDirectory)
        "list-branches" -> ListBranches(gitDirectory)
        "log" -> Log(gitDirectory)
        else -> error("unknown command $commandStr")
    }.also(GitCommand::execute)
}





