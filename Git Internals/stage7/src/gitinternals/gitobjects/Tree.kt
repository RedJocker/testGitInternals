package gitinternals.gitobjects

import gitinternals.gitobjects.other.FileInfo

class Tree(override val rawContent: ByteArray, override val length: Int) : GitObject {

    private var treeParser: TreeParser? = TreeParser()
    val listFileInfo: List<FileInfo> by lazy {
        treeParser!!.listFileInfo
    }

    init {
        treeParser!!.parse()
        listFileInfo
        treeParser = null
    }

    override fun toString(): String {
        return "*TREE*\n" + listFileInfo.joinToString("\n", transform = FileInfo::toString)
    }

    private inner class TreeParser() {

        val listFileInfo = mutableListOf<FileInfo>()

        var currentPermission = 0
        var currentFileName = ""
        var currentImproperHash = ""
        var currentProperHash = ""
        var currentIndex = 0
        fun parse() {
            var parsingState = 0

            while(currentIndex < length) {
                when(parsingState) {
                    0 -> {
                        currentIndex += parsePermission()
                        parsingState = 1
                    }
                    1 -> {
                        currentIndex += parseFileName()
                        parsingState = 2
                    }
                    2 -> {
                        currentIndex += parseHash()
                        listFileInfo.add(FileInfo(currentPermission, currentFileName, currentProperHash ,currentImproperHash))
                        parsingState = 0
                    }
                }
            }
        }

        fun parsePermission(): Int {
            val whiteSpace = 32.toByte()
            val permissionAsString = rawContent
                .drop(currentIndex)
                .takeWhile { it != whiteSpace}
                .map(Byte::toInt)
                .map(Int::toChar)
                .joinToString("")

            val permission = permissionAsString.toIntOrNull() ?: error("incorrect permission value while parsing tree:")
            currentPermission = permission
            return permissionAsString.length + 1
        }

        private fun parseFileName(): Int {
            val nullByte = 0.toByte()
            val fileName = rawContent
                .drop(currentIndex)
                .takeWhile { it != nullByte}
                .map(Byte::toInt)
                .map(Int::toChar)
                .joinToString("")

            currentFileName = fileName
            return fileName.length + 1
        }

        private fun parseHash(): Int {
            val hashSize = 20
            val content = rawContent
                .drop(currentIndex)
                .take(hashSize)
            val improperHash = content
                .joinToString("") { "%X".format(it) }
                .lowercase()
            val properHash = content
                .joinToString("") { "%02X".format(it) }
                .lowercase()
            currentImproperHash = improperHash
            currentProperHash = properHash
            return hashSize
        }
    }
}