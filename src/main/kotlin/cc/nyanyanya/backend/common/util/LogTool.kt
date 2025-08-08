package cc.nyanyanya.backend.common.util

import cc.nyanyanya.backend.NyasideKotlinBackendApplication
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component

@Component
@Scope("prototype")
object LogTool {
    val logger = LoggerFactory.getLogger(NyasideKotlinBackendApplication::class.java)
}