package cc.nyanyanya.backend.user_management.service

import cc.nyanyanya.backend.common.persistence.model.UserModel
import cc.nyanyanya.backend.common.util.VerificationCode
import jakarta.annotation.Resource
import jakarta.mail.internet.MimeMessage
import org.springframework.core.env.Environment
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service


@Service
class EmailService(
    private val mailSender: JavaMailSender,
    @Resource private val env: Environment,
) {
    fun sendVerificationCode(emailAddress: String): String {
        val verificationCode = VerificationCode.generateEmailVerificationCode()

        val mimeMessage: MimeMessage = mailSender.createMimeMessage()
        val helper = MimeMessageHelper(
            mimeMessage,
            true
        )
        helper.setSubject("喵站验证码")
        helper.setText(
"""<h1>欢迎使用喵站</h1>
你的验证码是<br>
<b style='color:blue'>${verificationCode}</b><br>
千万不要给别人看哦！<br>""",
            true
        )
        helper.setTo(emailAddress)
        env.getProperty("spring.mail.username")?.let { helper.setFrom(it) }
        mailSender.send(mimeMessage)

        return verificationCode
    }

    fun sendPasswordToEmail(user: UserModel) {
        val mimeMessage: MimeMessage = mailSender.createMimeMessage()
        val helper = MimeMessageHelper(
            mimeMessage,
            true
        )
        helper.setSubject("喵站密码找回")
        helper.setText(
            """<h1>欢迎使用喵站</h1>
你的密码是<br>
<b style='color:blue'>${user.password}</b><br>
千万不要给别人看哦！<br>""",
            true
        )
        helper.setTo(user.email)
        env.getProperty("spring.mail.username")?.let { helper.setFrom(it) }
        mailSender.send(mimeMessage)
    }
}