package gitinternals

interface GitObject {
    val rawContent: ByteArray
    val length: Int
}