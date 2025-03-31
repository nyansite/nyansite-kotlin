package cc.nyanyanya.backend.user_management.controller


import cc.nyanyanya.backend.common.persistence.model.User
import cc.nyanyanya.backend.common.util.CookieTool
import cc.nyanyanya.backend.common.util.LogicTool
import cc.nyanyanya.backend.common.util.bo.ReturnErrorCode
import cc.nyanyanya.backend.user_management.service.UserService
import jakarta.servlet.http.HttpServletResponse
import jakarta.servlet.http.HttpSession
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/user")
class LogonController(
    private val userService: UserService
) {
    private fun loginByUser(dbUser: User): Int {
        if (dbUser == User()) {
            return 6
        }

        if (userService.login(dbUser) == 1) {
            return 5
        }

        return 0
    }

    @PostMapping("/login")
    fun login(
        @RequestParam(defaultValue = User.USERNAME_DEFAULT) username: String,
        @RequestParam(defaultValue = User.EMAIL_DEFAULT) email: String,
        @RequestParam(defaultValue = User.PHONE_DEFAULT) phone: String,
        @RequestParam(defaultValue = User.PASSWORD_DEFAULT) password: String,
        session: HttpSession,
        response: HttpServletResponse
    ): ReturnErrorCode {
        if (session.getAttribute("isLogon") as? Boolean ?: false) {
            return ReturnErrorCode(7)
        }

        if (username == User.USERNAME_DEFAULT && email == User.EMAIL_DEFAULT && phone == User.PHONE_DEFAULT) {
            return ReturnErrorCode(2)
        }
        if (userService.verifyPasswordFormat(password)) {
            return ReturnErrorCode(3)
        }

        val args = listOf(username, email, phone)
        val argsDefaultValues = listOf(
            User.USERNAME_DEFAULT,
            User.EMAIL_DEFAULT,
            User.PHONE_DEFAULT
        )
        val isJustOneNull = LogicTool.isJustOneNotNull(args, argsDefaultValues)
        if (!isJustOneNull.isTrue) {
            return ReturnErrorCode(4)
        }
        val notNullArgIndex = isJustOneNull.trueIndex

        var dbUser = User()
        when (notNullArgIndex) {
            0 -> {
                // username
                dbUser = userService.verifyUserByUsername(username)
                if (!userService.verifyUsernameFormat(username)) {
                    return ReturnErrorCode(2)
                }
            }

            1 -> {
                // email
                dbUser = userService.verifyUserByEmail(email)
                if (!userService.verifyEmailFormat(email)) {
                    return ReturnErrorCode(2)
                }
            }

            2 -> {
                // phone
                dbUser = userService.verifyUserByPhone(phone)
                if (!userService.verifyPhoneFormat(phone)) {
                    return ReturnErrorCode(2)
                }
            }
        }

        session.setAttribute("username", dbUser.username)
        session.setAttribute("isLogin", true.toString())
        CookieTool.addCookie("username", dbUser.username, response)
        CookieTool.addCookie("isLogon", true.toString(), response)
        return ReturnErrorCode(loginByUser(dbUser).toByte())
    }

    @PostMapping("/logout")
    fun logout(
        session: HttpSession,
        response: HttpServletResponse,
    ): ReturnErrorCode {
        if (session.getAttribute("isLogon") as? Boolean ?: false) {
            return ReturnErrorCode(1)
        }

        session.invalidate()
        CookieTool.addCookie("isLogon", false.toString(), response)
        return ReturnErrorCode(0)
    }
}
