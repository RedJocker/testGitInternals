package gitinternals.commands.utils

import gitinternals.log
import java.io.File

fun getCurrentBranch(gitDirectory: String): String {
    val headFile = File("$gitDirectory/HEAD").also {
        if (it.exists().not()) error("could not locate file ${it.path}")
    }

    val headContent = headFile.bufferedReader().readLines().joinToString("")
    log("head content: $headContent")
    if (headContent.startsWith("ref: refs/heads/").not()) error("invalid HEAD file content")

    return headContent.substringAfter("ref: refs/heads/")
}

fun getHeadsFolder(gitDirectory: String): File {
    return File("$gitDirectory/refs/heads").also {
        if (it.exists().not()) error("could not locate file ${it.path}")
        if (it.isDirectory.not()) error("heads file at path ${it.path} should be a folder")
    }
}

fun validateHash(hash: String) {
    if(hash.matches("^[0-9a-f]{40}$".toRegex()).not()) error("invalid hash on head file: $hash")
}