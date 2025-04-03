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
        @RequestParam phone: String,
        @RequestParam password: String,
        session: HttpSession,
        response: HttpServletResponse
    ): ResultErrorCode {
        val isLogin = (session.getAttribute("isLogin") as? String ?: "false").toBoolean()
        if (isLogin) {
            return ResultErrorCode(1)
        }

        val isJustOneNull = LogicTool.isJustOneNotNull(
            listOf(
                username,
                email,
                phone
            ),
            listOf(
                "",
                "",
                "",
            )
        )
        if (!isJustOneNull.isTrue) {
            return ResultErrorCode(2)
        }
        val notNullArgIndex = isJustOneNull.trueIndex

        var dbUserModel = UserModel()
        when (notNullArgIndex) {
            0 -> {
                // username
                dbUserModel = userService.fetchUserByUsername(username)
            }

            1 -> {
                // email
                dbUserModel = userService.fetchUserByEmail(email)
            }

            2 -> {
                // phone
                dbUserModel = userService.fetchUserByPhone(phone)
            }
        }

        if (dbUserModel == UserModel()) {
            return ResultErrorCode(4)
        }

        if (userService.login(dbUserModel, password) == 1) {
            return ResultErrorCode(3)
        }

        session.setAttribute("username", dbUserModel.username)
        session.setAttribute("id", dbUserModel.id)
        session.setAttribute("isLogin", true.toString())
        CookieTool.addCookie("username", dbUserModel.username, response)

        return ResultErrorCode(0)
    }

    @PostMapping("/logout")
    fun logout(
        session: HttpSession,
    ): ResultErrorCode {
        val isLogin = (session.getAttribute("isLogin") as? String ?: "false").toBoolean()
        if (!isLogin) {
            return ResultErrorCode(1)
        }

        session.invalidate()
        return ResultErrorCode(0)
    }
}
