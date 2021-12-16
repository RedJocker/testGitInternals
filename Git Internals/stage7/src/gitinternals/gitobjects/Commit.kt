package gitinternals.gitobjects

import gitinternals.Hash
import gitinternals.gitobjects.other.UserInfo
import gitinternals.gitobjects.other.UserInfo.UserType
import java.time.LocalDateTime
import java.time.ZoneOffset

class Commit(override val rawContent: ByteArray,
             override val length: Int): GitObject {

    private var commitParser: CommitParser? = CommitParser()

    val tree: Hash by lazy {
        commitParser?.tree ?: error("CommitParser failed to parse tree")
    }

    val parents: List<Hash> by lazy {
        commitParser!!.parents.toList()
    }

    val authorInfo: UserInfo by lazy {
        commitParser?.authorInfo ?: error("CommitParser failed to parse author")
    }

    val committerInfo: UserInfo by lazy {
        commitParser?.committerInfo ?: error("CommitParser failed to parse committer")
    }

    val commitMessage: String by lazy {
        commitParser?.message ?: error("CommitParser failed to parse commit message")
    }

    init {
        commitParser!!.parse()
        tree
        parents
        authorInfo
        committerInfo
        commitMessage
        commitParser = null
    }



    override fun toString(): String {
        val parentsString = if(parents.isNotEmpty()) {
            parents.joinToString(" | ", "parents: ", "\n")
        } else {
            ""
        }
        return "*COMMIT*\n" +
                "tree: $tree\n" +
                parentsString +
                "author: $authorInfo\ncommitter: " +
                "$committerInfo\n" +
                "commit message:\n" +
                commitMessage
    }

    private inner class CommitParser {

        var tree: String? = null
        var parents: MutableList<String> = mutableListOf()
        var authorInfo: UserInfo? = null
        var committerInfo: UserInfo? = null
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
                         currentIndex += parseUserInfo(UserType.AUTHOR)
                         parsingState = 3
                     }
                     3 -> {
                         currentIndex += parseUserInfo(UserType.COMMITTER)
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

        private fun parseUserInfo(type: UserType): Int {
            val userInfoLine = rawContent.drop(currentIndex)
                .map(Byte::toInt)
                .map(Int::toChar)
                .takeWhile { it != '\n' }
                .joinToString("", "", "\n")

            val regex = when(type) {
                UserType.COMMITTER -> "^committer (\\w+) <([\\w.-]+@[\\w.-]+)> (\\d+) ([+-]\\d{4})\n$".toRegex()
                UserType.AUTHOR -> "^author (\\w+) <([\\w.-]+@[\\w.-]+)> (\\d+) ([+-]\\d{4})\n$".toRegex()
            }

            val matchResult = regex.matchEntire(userInfoLine)
            val groupValues = matchResult?.groupValues
                ?: error("wrong format for commit while trying to parse ${type.name.lowercase()} info\n$userInfoLine")
            val offsetTime = ZoneOffset.of(groupValues[4])

            val time = LocalDateTime.ofEpochSecond(groupValues[3].toLong(), 0, offsetTime)
            when(type) {
                UserType.COMMITTER -> committerInfo = UserInfo(
                    name=groupValues[1],
                    email=groupValues[2],
                    type=type,
                    time=time,
                    offset=offsetTime
                )
                UserType.AUTHOR -> authorInfo = UserInfo(
                    name=groupValues[1],
                    email=groupValues[2],
                    type=type,
                    time=time,
                    offset=offsetTime
                )
            }
            return userInfoLine.length
        }

        fun parseMessage() {
            message = rawContent.drop(currentIndex + 1)
                .map(Byte::toInt)
                .map(Int::toChar)
                .joinToString("")
        }
     }
}