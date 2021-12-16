package gitinternals

import gitinternals.gitobjects.Blob
import gitinternals.gitobjects.Commit
import gitinternals.gitobjects.GitObject
import gitinternals.gitobjects.Tree

class GitObjectParser(private val rawInput: ByteArray) {

    private val firstLine by lazy {
        rawInput.takeWhile {  it != 0.toByte() }
            .map(Byte::toInt)
            .map(Int::toChar)
            .joinToString("")
    }

    private val type by lazy {
        firstLine.takeWhile { it != ' ' }
    }

    private val length by lazy {
        firstLine.substringAfter("$type ")
                 .toIntOrNull() ?: error("header does not contain length")
    }

    private val rawContent by lazy {
        rawInput.dropWhile { it != 0.toByte() }.drop(1).toByteArray()
    }

    val head by lazy {
        "type:$type length:$length"
    }

    val parsedInput by lazy {
        rawInput.map(Byte::toInt)
            .map(Int::toChar)
            .joinToString("")
    }

    val content : GitObject by lazy {
        log("Git Object:")
        when(type){
            "blob" -> Blob(rawContent, length)
            "commit" -> Commit(rawContent, length)
            "tree" -> Tree(rawContent, length)
            else -> error("invalid type on header\n$type")
        }.also(::log)
    }
}