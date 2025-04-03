package cc.nyanyanya.backend.common.persistence.repository

import okio.*
import okio.Path.Companion.toPath
import org.springframework.stereotype.Repository

@Repository
class FileRepo {
    fun readFileToBase64String(filePath: String): String {
        val fileSource = FileSystem.SYSTEM.source(filePath.toPath())
        val buffer = fileSource.buffer()
        val base64String = buffer.readByteString().base64()
        return base64String
    }
}