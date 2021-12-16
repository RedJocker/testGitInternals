package gitinternals.commands

import gitinternals.commands.utils.getCurrentBranch
import gitinternals.commands.utils.getHeadsFolder
import gitinternals.log
import java.io.File

class ListBranches(override val gitDirectory: String): GitCommand {
    override fun execute() {
        val currentBranch = getCurrentBranch(gitDirectory)
        val headsFolder = getHeadsFolder(gitDirectory)
        val branches: String = headsFolder.listFiles { file -> file.isFile }
            ?.map(File::getName)
            ?.sorted()
            ?.joinToString("\n") { name ->
                if (name == currentBranch) "* $name" else "  $name"
            }
            ?: error("error while retrieving branches")
        log("result:\n$branches")
        println(branches)
    }


}