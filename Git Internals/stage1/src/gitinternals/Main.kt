package gitinternals

import java.io.File
import java.io.FileInputStream
import java.util.zip.InflaterInputStream

fun main() {
    println("Enter git object location:")

    val filename = readLine() ?: error("failed to receive filename as input")

    val file = File(filename).also {
        if(it.exists().not()) error("could not find file with path ${it.path}")
    }

    val inflaterInputStream = InflaterInputStream(FileInputStream(file))

    val input = inflaterInputStream.readAllBytes()
        .map(Byte::toInt)
        .map { if (it == 0) 10 else it }
        .map(Int::toChar)
        .joinToString("")
        .also(::println)
}
