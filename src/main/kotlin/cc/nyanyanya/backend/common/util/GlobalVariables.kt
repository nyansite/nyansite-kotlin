package cc.nyanyanya.backend.common.util

import cc.nyanyanya.backend.common.persistence.repository.FileRepo
import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Component


@Component
class GlobalVariables() {
    final var SENSITIVE_WORDS: MutableMap<String, List<String>> = mutableMapOf()
        private set

    init {
        loadSensitiveWords()
    }

    private final fun loadSensitiveWords() {
        val FILE_NAME_LIST = listOf(
            "反动词库",
            "敏感网址",
            "暴恐词库",
            "民生词库",
            "色情词库",
            "贪腐词库",
        )

        FILE_NAME_LIST.forEach() { it ->
            val filePath = ClassPathResource("static/sensitive-words/$it.txt").file
            val words = FileRepo.readFileAllLines(filePath)
            SENSITIVE_WORDS[it] = words
        }
    }
}