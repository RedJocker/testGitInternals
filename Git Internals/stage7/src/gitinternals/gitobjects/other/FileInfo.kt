package gitinternals.gitobjects.other

import gitinternals.Hash

class FileInfo(val permission: Int, val fileName: String, val properHash: Hash, val improperHash: String) {

    override fun toString(): String {
        return "$permission $properHash $fileName"
    }
}