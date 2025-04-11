package cc.nyanyanya.backend.common.persistence.repository

import okio.*
import okio.ByteString.Companion.decodeBase64
import okio.Path.Companion.toPath
import org.springframework.stereotype.Repository
import java.io.File

@Repository
class FileRepo {
    companion object {

        fun readFileAllLines(file: File): MutableList<String> {
            val words = mutableListOf<String>()

            val fileSource = file.source()
            val buffer = fileSource.buffer()
            do {
                val line = buffer.readUtf8Line() ?: ""
                if (line != "") words.add(line)
            } while (!buffer.exhausted())

            return words
        }

        fun readFileToBase64String(filePath: String): String {
            val fileSource = FileSystem.SYSTEM.source(filePath.toPath())
            val buffer = fileSource.buffer()
            val base64String = buffer.readByteString().base64()
            return base64String
        }

        fun writeFileToBase64String(
            filePath: String,
            avatarEncodeString: String,
        ) {
            val fileSink = FileSystem.SYSTEM.sink(filePath.toPath())
            val buffer = fileSink.buffer()
            avatarEncodeString.decodeBase64()?.let { buffer.write(it) }
            buffer.flush()
        }

        fun deleteFile(filePath: String) {
            FileSystem.SYSTEM.delete(filePath.toPath())
        }
    }
}
