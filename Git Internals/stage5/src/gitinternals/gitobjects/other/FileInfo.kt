package gitinternals.gitobjects.other

class FileInfo(val permission: Int, val fileName: String, val hash: String) {

    override fun toString(): String {
        return "$permission $hash $fileName"
    }
}