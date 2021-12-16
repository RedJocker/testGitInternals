package gitinternals.commands

import gitinternals.log
import java.io.File

class ListBranches(override val gitDirectory: String): GitCommand {
    override fun execute() {
        val headFile = File("$gitDirectory/HEAD").also {
            if(it.exists().not()) error("could not locate file ${it.path}")
        }
        val headsFolder = File("$gitDirectory/refs/heads").also {
            if(it.exists().not()) error("could not locate file ${it.path}")
            if(it.isDirectory.not()) error("heads file at path ${it.path} should be a folder")
        }

        val headContent = headFile.bufferedReader().readLines().joinToString("")
        log("head content: $headContent")
        if(headContent.startsWith("ref: refs/heads/").not()) error("invalid HEAD file content")

        val currentBranch = headContent.substringAfter("ref: refs/heads/")
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