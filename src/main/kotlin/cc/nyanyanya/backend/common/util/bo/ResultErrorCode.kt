package cc.nyanyanya.backend.common.util.bo

import org.springframework.stereotype.Component

@Component
data class ResultErrorCode(
    var error: Byte = DefaultValue.DEFAULT_BYTE
)
