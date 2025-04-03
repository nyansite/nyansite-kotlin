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
    @PostMapping("/login")
    fun login(
        @RequestParam(defaultValue = User.USERNAME_DEFAULT) username: String,
        @RequestParam(defaultValue = User.EMAIL_DEFAULT) email: String,
        @RequestParam(defaultValue = User.PHONE_DEFAULT) phone: String,
        @RequestParam(defaultValue = User.PASSWORD_DEFAULT) password: String,
        session: HttpSession,
        response: HttpServletResponse
    ): ReturnErrorCode {
        val isLogin = (session.getAttribute("isLogin") as? String ?: "false").toBoolean()
        if (isLogin) {
            return ReturnErrorCode(1)
        }

        val isJustOneNull = LogicTool.isJustOneNotNull(
            listOf(
                username,
                email,
                phone
            ),
            listOf(
                User.USERNAME_DEFAULT,
                User.EMAIL_DEFAULT,
                User.PHONE_DEFAULT
            )
        )
        if (!isJustOneNull.isTrue) {
            return ReturnErrorCode(2)
        }
        val notNullArgIndex = isJustOneNull.trueIndex

        var dbUser = User()
        when (notNullArgIndex) {
            0 -> {
                // username
                dbUser = userService.fetchUserByUsername(username)
            }

            1 -> {
                // email
                dbUser = userService.fetchUserByEmail(email)
            }

            2 -> {
                // phone
                dbUser = userService.fetchUserByPhone(phone)
            }
        }

        if (dbUser == User()) {
            return ReturnErrorCode(4)
        }

        if (userService.login(dbUser, password) == 1) {
            return ReturnErrorCode(3)
        }

        session.setAttribute("username", dbUser.username)
        session.setAttribute("isLogin", true.toString())
        CookieTool.addCookie("username", dbUser.username, response)

        return ReturnErrorCode(0)
    }

    @PostMapping("/logout")
    fun logout(
        session: HttpSession,
        response: HttpServletResponse,
    ): ReturnErrorCode {
        val isLogin = (session.getAttribute("isLogin") as? String ?: "false").toBoolean()
        if (!isLogin) {
            return ReturnErrorCode(1)
        }

        session.invalidate()
        return ReturnErrorCode(0)
    }
}
