package gitinternals

import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

class Commit(override val rawContent: ByteArray,
             override val length: Int): GitObject {

    private val commitParser = CommitParser()

    init {
        commitParser.parse()
    }

    val tree by lazy {
        commitParser.tree ?: error("CommitParser failed to parse tree")
    }

    val parents by lazy {
        commitParser.parents.toList()
    }

    val authorInfo by lazy {
        commitParser.authorInfo ?: error("CommitParser failed to parse author")
    }

    val committerInfo by lazy {
        commitParser.committerInfo ?: error("CommitParser failed to parse committer")
    }

    val commitMessage by lazy {
        commitParser.message ?: error("CommitParser failed to parse commit message")
    }



    private inner class CommitParser {

        private val timeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss xxx")

        var tree: String? = null
        var parents: MutableList<String> = mutableListOf()
        var authorInfo: String? = null
        var committerInfo: String? = null
        var message: String? = null
        var currentIndex = 0

        fun parse() {
            var parsingState = 0
            currentIndex = 0
            while(currentIndex < length) {
                when(parsingState) {
                     0 -> {
                         currentIndex += parseTree()
                         parsingState = 1
                     }
                     1 -> {
                         if(rawContent[currentIndex].toInt().toChar() == 'p') {
                             currentIndex += parseParent()
                         } else {
                             parsingState = 2
                         }
                     }
                     2 -> {
                         currentIndex += parseAuthor()
                         parsingState = 3
                     }
                     3 -> {
                         currentIndex += parseCommitter()
                         parsingState = 4
                     }
                     4 -> {
                         parseMessage()
                         return
                     }
                }
            }
            error("unexpected end of commit data from file, length declared on header does not match file length")
        }

        private fun parseTree(): Int {
            val lineSize = 46
            val treeLine = rawContent.drop(currentIndex)
                .take(lineSize)
                .map(Byte::toInt)
                .map(Int::toChar)
                .joinToString("")

            if(treeLine.matches("^tree [0-9a-f]{40}\\n$".toRegex())){
                tree = treeLine.substring(5, lineSize - 1)
            } else {
                error("wrong format for commit while trying to parse tree\n$treeLine")
            }

            return lineSize
        }

        private fun parseParent(): Int {
            val lineSize = 48

            val parentLine = rawContent.drop(currentIndex)
                .take(lineSize)
                .map(Byte::toInt)
                .map(Int::toChar)
                .joinToString("")

            if(parentLine.matches("^parent [0-9a-f]{40}\\n$".toRegex())){
                 parents.add(parentLine.substring(7, lineSize - 1))
            } else {
                error("wrong format for commit while trying to parse parent\n$parentLine")
            }

            return lineSize
        }

        private fun parseAuthor(): Int {
            val authorLine = rawContent.drop(currentIndex)
                .map(Byte::toInt)
                .map(Int::toChar)
                .takeWhile { it != '\n' }
                .joinToString("", "", "\n")

            val regex =
                "^author (\\w+) <([\\w.-]+@[\\w.-]+)> (\\d+) ([+-]\\d{4})\n$".toRegex()


            val matchResult = regex.matchEntire(authorLine)
            val groupValues = matchResult?.groupValues
                    ?: error("wrong format for commit while trying to parse author\n$authorLine")
            val offsetTime = ZoneOffset.of(groupValues[4])

            val time = LocalDateTime.ofEpochSecond(groupValues[3].toLong(), 0, offsetTime).atOffset(offsetTime)
            val timeStamp = time.format(timeFormat)
            authorInfo = "${groupValues[1]} ${groupValues[2]} original timestamp: $timeStamp"

            return authorLine.length
        }


        private fun parseCommitter(): Int {
            val committerLine = rawContent.drop(currentIndex)
                .map(Byte::toInt)
                .map(Int::toChar)
                .takeWhile { it != '\n' }
                .joinToString("", "", "\n")

            val regex =
                "^committer (\\w+) <([\\w.-]+@[\\w.-]+)> (\\d+) ([+-]\\d{4})\n$".toRegex()


            val matchResult = regex.matchEntire(committerLine)
            val groupValues = matchResult?.groupValues
                ?: error("wrong format for commit while trying to parse committer\n$committerLine")
            val offsetTime = ZoneOffset.of(groupValues[4])

            val timeStamp = LocalDateTime.ofEpochSecond(groupValues[3].toLong(), 0, offsetTime).atOffset(offsetTime).format(timeFormat)

            committerInfo = "${groupValues[1]} ${groupValues[2]} commit timestamp: $timeStamp"

            return committerLine.length
        }

        fun parseMessage() {
            message = rawContent.drop(currentIndex)
                .map(Byte::toInt)
                .map(Int::toChar)
                .joinToString("")
        }
     }

    override fun toString(): String {
        val parentsString = if(parents.isNotEmpty()) {
            parents.joinToString(" | ", "parents: ", "\n")
        } else {
            ""
        }
        return "*COMMIT*\n" +
                "tree: $tree\n" +
                "${parentsString}" +
                "author: $authorInfo\ncommitter: " +
                "$committerInfo\n" +
                "commit message:$commitMessage"
    }
}