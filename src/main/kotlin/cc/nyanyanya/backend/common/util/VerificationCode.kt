package cc.nyanyanya.backend.common.util

import java.security.SecureRandom

object VerificationCode {
    const val EXPIRATION_DURING = 5 * 60 * 1000 // minutes

    fun generateEmailVerificationCode(): String {
        val rawCode = SecureRandom().nextInt(0, 999999).toString()
        val code = rawCode.padStart(6, '0')
        return code
    }
}
