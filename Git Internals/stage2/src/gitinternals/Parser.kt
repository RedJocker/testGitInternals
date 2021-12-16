package gitinternals

class Parser(private val rawInput: ByteArray) {

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

    val head by lazy {
        "type:$type length:$length"
    }
}