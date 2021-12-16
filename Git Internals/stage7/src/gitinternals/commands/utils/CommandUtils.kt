package gitinternals.commands.utils

import gitinternals.Hash
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

fun Hash.getGitObjectPath(gitDirectory: String): String {
    this.validateHash()
    return "$gitDirectory/objects/${this.take(2)}/${this.drop(2)}"
}

fun Hash.validateHash() {
    if(this.matches("^[0-9a-f]{40}$".toRegex()).not()) error("invalid hash: $this")
}