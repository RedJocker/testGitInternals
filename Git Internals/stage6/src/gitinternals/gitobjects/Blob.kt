package gitinternals.gitobjects

class Blob(override val rawContent: ByteArray, override val length: Int) : GitObject {

    val content by lazy {
        rawContent.map(Byte::toInt)
            .map(Int::toChar)
            .joinToString("")
    }


    override fun toString(): String {
        return "*BLOB*\n$content"
    }
}