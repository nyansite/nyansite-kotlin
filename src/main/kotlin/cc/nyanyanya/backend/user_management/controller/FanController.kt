package cc.nyanyanya.backend.user_management.controller


import cc.nyanyanya.backend.common.persistence.model.UserModel
import cc.nyanyanya.backend.common.util.bo.DefaultValue
import cc.nyanyanya.backend.common.util.bo.ResultErrorCode
import cc.nyanyanya.backend.user_management.service.FanService
import cc.nyanyanya.backend.user_management.service.UserService
import jakarta.servlet.http.HttpSession
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/user")
class FanController(
    private val fanService: FanService,
    private val userService: UserService,
) {
    @PostMapping("/cancel-follow")
    fun cancelFollow(
        @RequestParam id: String,
        session: HttpSession
    ): ResultErrorCode {
        val isLogin = (session.getAttribute("isLogin") as? String ?: "false").toBoolean()
        if (!isLogin) {
            return ResultErrorCode(3)
        }

        val followUserModel = userService.fetchUserById(UUID.fromString(id))
        if (followUserModel == UserModel()) {
            return ResultErrorCode(2)
        }
        val fanUsername = session.getAttribute("username") as? String ?: UserModel.USERNAME_DEFAULT
        val fanUserModel = userService.fetchUserByUsername(fanUsername)

        val success = fanService.removeFanByUserId(fanUserModel.id, UUID.fromString(id))
        return ResultErrorCode(if (success) 0 else 1)
    }
}
