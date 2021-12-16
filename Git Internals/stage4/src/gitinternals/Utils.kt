package gitinternals

import java.io.File
import java.io.FileInputStream
import java.util.zip.InflaterInputStream

fun log(message: String){
    // System.err is not checked by tests, which is good for logging
    System.err.println(message);
}

fun log(message: Any) {
    System.err.println(message.toString())
}

fun readLine(message: String): String? {
    println(message)
    return readLine()
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