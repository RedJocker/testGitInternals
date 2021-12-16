package gitinternals


import java.io.File
import java.io.FileInputStream
import java.util.zip.InflaterInputStream

typealias Hash =  String

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


fun getInflatedRawFileInput(filepath: String): ByteArray {
    val file = File(filepath).also {
        if(it.exists().not()) error("could not find file with path ${it.path}")
    }
    val inflaterInputStream = InflaterInputStream(FileInputStream(file))
    return inflaterInputStream.readAllBytes()
}

