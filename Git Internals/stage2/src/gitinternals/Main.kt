package gitinternals

import java.io.File
import java.io.FileInputStream
import java.util.zip.InflaterInputStream

fun main() {
    val filepath = getFilePathFromInput()
    val rawInput = getInflatedRawInput(filepath)
    val parser = Parser(rawInput)

    println(parser.head)
}

fun getFilePathFromInput(): String {
    val directory = readLine("Enter .git directory location:") ?: error("failed to receive directory location")
    val hash = readLine("Enter git object hash:") ?: error("failed to receive hash")
    return "$directory/objects/${hash.take(2)}/${hash.drop(2)}"
}

fun getInflatedRawInput(filepath: String): ByteArray {
    val file = File(filepath).also {
        if(it.exists().not()) error("could not find file with path ${it.path}")
    }
    val inflaterInputStream = InflaterInputStream(FileInputStream(file))
    return inflaterInputStream.readAllBytes()
}

fun readLine(message: String): String? {
    println(message)
    return readLine()
}
