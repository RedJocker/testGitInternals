package gitinternals.gitobjects.other

import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

class UserInfo(val name: String, val email: String, val type: UserType, val time: LocalDateTime, val offset: ZoneOffset) {
    companion object {
        private val timeFormat: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss xxx")

    }
    private val timeStamp = time.atOffset(offset).format(timeFormat)
    private val timeStampType = when(type) {
        UserType.COMMITTER -> "commit timestamp:"
        UserType.AUTHOR -> "original timestamp:"
    }

    override fun toString(): String {
        return "$name $email $timeStampType $timeStamp"
    }

    enum class UserType {
        COMMITTER, AUTHOR
    }
}