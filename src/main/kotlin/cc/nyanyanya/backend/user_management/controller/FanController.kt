package cc.nyanyanya.backend.user_management.controller


import cc.nyanyanya.backend.common.persistence.model.FanGroupModel
import cc.nyanyanya.backend.common.persistence.model.UserModel
import cc.nyanyanya.backend.common.util.bo.ResultErrorCode
import cc.nyanyanya.backend.user_management.service.FanGroupService
import cc.nyanyanya.backend.user_management.service.FanService
import cc.nyanyanya.backend.user_management.service.UserService
import jakarta.servlet.http.HttpSession
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/user")
class FanController(
    private val fanService: FanService,
    private val fanGroupService: FanGroupService,
    private val userService: UserService,
) {
    @PostMapping("/add-follow")
    fun addFollow(
        @RequestParam id: String,
        @RequestParam(value = "group_name") groupName: String,
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
        val fanGroupModel = fanGroupService.fetchFanGroupByNameAndUsername(groupName, fanUsername)
        if (fanGroupModel == FanGroupModel()) {
            return ResultErrorCode(4)
        }

        val success = fanService.addFan(fanUsername, followUserModel.id, groupName)
        return ResultErrorCode(0)
    }

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
        val success = fanService.removeFanByFanUsername(fanUsername, UUID.fromString(id))
        return ResultErrorCode(if (success) 0 else 1)
    }

    @PostMapping("/add-follow-group")
    fun addFollowGroup(
        @RequestParam(value = "group_name") groupName: String,
        session: HttpSession
    ): ResultErrorCode {
        val isLogin = (session.getAttribute("isLogin") as? String ?: "false").toBoolean()
        if (!isLogin) {
            return ResultErrorCode(2)
        }

        val fanUsername = session.getAttribute("username") as? String ?: UserModel.USERNAME_DEFAULT
        val fanGroupModel = fanGroupService.fetchFanGroupByNameAndUsername(groupName, fanUsername)
        if (fanGroupModel != FanGroupModel()) {
            return ResultErrorCode(1)
        }

        fanGroupService.addFanGroupByFanUsername(fanUsername, groupName)
        return ResultErrorCode(0)
    }

    @PostMapping("/delete-follow-group")
    fun deleteFollowGroup(
        @RequestParam(value = "group_name") groupName: String,
        session: HttpSession
    ): ResultErrorCode {
        val isLogin = (session.getAttribute("isLogin") as? String ?: "false").toBoolean()
        if (!isLogin) {
            return ResultErrorCode(3)
        }

        val fanUsername = session.getAttribute("username") as? String ?: UserModel.USERNAME_DEFAULT
        val fanGroupModel = fanGroupService.fetchFanGroupByNameAndUsername(groupName, fanUsername)
        if (fanGroupModel == FanGroupModel()) {
            return ResultErrorCode(1)
        }

        if (fanGroupModel.number == 0.toShort() || fanGroupModel.number == 1.toShort()) {
            return ResultErrorCode(2)
        }

        fanGroupService.removeFanGroupByFanGroupName(fanUsername, groupName)
        return ResultErrorCode(0)
    }
}
