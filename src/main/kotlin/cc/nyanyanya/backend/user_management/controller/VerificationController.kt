package cc.nyanyanya.backend.user_management.controller


import cc.nyanyanya.backend.common.persistence.model.UserModel
import cc.nyanyanya.backend.common.util.CookieTool
import cc.nyanyanya.backend.common.util.bo.DefaultValue
import cc.nyanyanya.backend.common.util.bo.ResultErrorCode
import cc.nyanyanya.backend.user_management.service.EmailService
import jakarta.servlet.http.HttpServletResponse
import jakarta.servlet.http.HttpSession
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/user")
class VerificationController(
    private val emailService: EmailService,
) {
    @PostMapping("/request-email-code")
    fun requestEmailCode(
        @RequestParam email: String,
        session: HttpSession,
        response: HttpServletResponse,
    ): ResultErrorCode {
        val SEND_EMAIL_MINUS_DURING = 60 * 1000 //60s
        val lastEmailCodeSendTime = session.getAttribute("lastEmailCodeSendTime") as? Long
            ?: DefaultValue().DEFAULT_TIMESTAMP.time
        if (System.currentTimeMillis() - lastEmailCodeSendTime < SEND_EMAIL_MINUS_DURING) {
            return ResultErrorCode(3)
        }

        if (email == "") {
            return ResultErrorCode(2)
        }

        if (!UserModel.verifyEmailFormat(email)) {
            return ResultErrorCode(1)
        }

        val verificationCode = emailService.sendVerificationCode(email)
        session.setAttribute("verificationCode", verificationCode)
        session.setAttribute("lastEmailCodeSendTime", System.currentTimeMillis())
        session.setAttribute("emailToVerify", email)
        CookieTool.addCookie("lastEmailCodeSendTime", System.currentTimeMillis().toString(), response)
        CookieTool.addCookie("emailToVerify", email, response)
        return ResultErrorCode(0)
    }

    @PostMapping("/request-original-email-code")
    fun requestOriginalEmailCode(
        @RequestParam email: String,
        session: HttpSession,
        response: HttpServletResponse,
    ): ResultErrorCode {
        val SEND_EMAIL_MIN_DURING = 60 * 1000 //60s
        val lastEmailCodeSendTime = session.getAttribute("lastOriginalEmailCodeSendTime") as? Long
            ?: DefaultValue().DEFAULT_TIMESTAMP.time
        if (System.currentTimeMillis() - lastEmailCodeSendTime < SEND_EMAIL_MIN_DURING) {
            return ResultErrorCode(3)
        }

        if (email == "") {
            return ResultErrorCode(2)
        }

        if (!UserModel.verifyEmailFormat(email)) {
            return ResultErrorCode(1)
        }

        val verificationCode = emailService.sendVerificationCode(email)
        session.setAttribute("originalVerificationCode", verificationCode)
        session.setAttribute("lastOriginalEmailCodeSendTime", System.currentTimeMillis())
        session.setAttribute("originalEmailToVerify", email)
        CookieTool.addCookie("lastOriginalEmailCodeSendTime", System.currentTimeMillis().toString(), response)
        CookieTool.addCookie("originalEmailToVerify", email, response)
        return ResultErrorCode(0)
    }
}
