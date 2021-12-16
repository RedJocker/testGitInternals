package gitinternals.gitobjects

interface GitObject {
    val rawContent: ByteArray
    val length: Int
}