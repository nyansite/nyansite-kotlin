package cc.nyanyanya.backend.user_management.controller


import cc.nyanyanya.backend.common.persistence.model.UserModel
import cc.nyanyanya.backend.common.util.CookieTool
import cc.nyanyanya.backend.common.util.LogicTool
import cc.nyanyanya.backend.common.util.bo.ResultErrorCode
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
        @RequestParam username: String,
        @RequestParam email: String,
        @RequestParam password: String,
        session: HttpSession,
        response: HttpServletResponse
    ): ResultErrorCode {
        val isLogin = session.getAttribute("isLogin") as? Boolean ?: false
        if (isLogin) {
            return ResultErrorCode(1)
        }

        val isJustOneNotNull = LogicTool.isJustOneNotNull(
            listOf(
                username,
                email,
            ),
            listOf(
                "",
                "",
            )
        )
        if (!isJustOneNotNull.isTrue) {
            return ResultErrorCode(2)
        }
        val notNullArgIndex = isJustOneNotNull.trueIndex

        var dbUser = UserModel()
        when (notNullArgIndex) {
            0 -> {
                // username
                dbUser = userService.fetchUserByUsername(username)
            }

            1 -> {
                // email
                dbUser = userService.fetchUserByEmail(email)
            }
        }

        if (dbUser == UserModel()) {
            return ResultErrorCode(4)
        }

        if (userService.login(dbUser, password) == 1) {
            return ResultErrorCode(3)
        }

        session.setAttribute("username", dbUser.username)
        session.setAttribute("id", dbUser.id)
        session.setAttribute("isLogin", true)
        CookieTool.addCookie("username", dbUser.username, response)

        return ResultErrorCode(0)
    }

    @PostMapping("/logout")
    fun logout(
        session: HttpSession,
    ): ResultErrorCode {
        val isLogin = session.getAttribute("isLogin") as? Boolean ?: false
        if (!isLogin) {
            return ResultErrorCode(1)
        }

        session.invalidate()
        return ResultErrorCode(0)
    }
}
