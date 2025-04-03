package cc.nyanyanya.backend.user_management.controller


import cc.nyanyanya.backend.common.persistence.model.Fan
import cc.nyanyanya.backend.common.persistence.model.User
import cc.nyanyanya.backend.common.util.LogicTool
import cc.nyanyanya.backend.common.util.bo.DefaultValue
import cc.nyanyanya.backend.common.util.bo.ReturnErrorCode
import cc.nyanyanya.backend.common.util.result
import cc.nyanyanya.backend.user_management.entity.FollowList
import cc.nyanyanya.backend.user_management.service.FanService
import cc.nyanyanya.backend.user_management.service.FollowService
import cc.nyanyanya.backend.user_management.service.UserService
import jakarta.servlet.http.HttpServletResponse
import jakarta.servlet.http.HttpSession
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/user")
class UserController(
    userService: UserService,
    followService: FollowService,
    fanService: FanService
) {
    val userService: UserService = userService
    val followService: FollowService = followService
    val fanService: FanService = fanService

//    @GetMapping("/selectAll")
//    fun selectAll(): List<User?> {
//        val list: List<User?> = userService.selectAll() ?: arrayListOf()
//        return list
//    }

//    /**
//     * 分页查询
//     */
//    @GetMapping("/selectPage")
//    fun selectPage(
//        @RequestParam(defaultValue = "1") pageNum: Int,
//        @RequestParam(defaultValue = "10") pageSize: Int,
//        @RequestParam(defaultValue = "") name: String
//    ): List<User> {
//        val page: List<User> = userService.selectPage(pageNum, pageSize, name)
//        return page
//    }

    @PostMapping("/verify-account")
    fun logout(
        @RequestParam(defaultValue = User.USERNAME_DEFAULT) username: String,
        @RequestParam(defaultValue = User.EMAIL_DEFAULT) email: String,
        @RequestParam(defaultValue = User.PHONE_DEFAULT) phone: String,
    ): ReturnErrorCode {
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
            return ReturnErrorCode(1)
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
            return ReturnErrorCode(0)
        }
        return ReturnErrorCode(2)
    }

    @PostMapping("/get-own-info")
    fun getOwnInfo(
        session: HttpSession,
    ): Any {
        var dbUser = User()

        val fansInfoTransformer = { fanList: List<Fan> ->
            val fansInfo = mutableListOf<UUID>()

            fanList.forEach { it ->
                fansInfo.add(it.fanId)
            }
            fansInfo
        }
        val followsInfoTransformer = { followList: FollowList ->
            val followsInfo = mutableListOf<Any>()

            followList.groups.forEach { it ->
                followsInfo.add(
                    object {
                        val group_name = it.name
                        val user_ids = it.follows.map { it.userId }.toMutableList()
                    }
                )
            }
            followsInfo
        }
        val userInfo = object {
            var id = User.ID_DEFAULT
            var username = User.USERNAME_DEFAULT
            var name = User.NICKNAME_DEFAULT
            var phone = User.PHONE_DEFAULT
            var gender = User.GENDER_ID_DEFAULT
            var email = User.EMAIL_DEFAULT
            var level = User.LEVEL_ID_DEFAULT
            var avatar = ""
            var birthday = User.BIRTHDAY_DEFAULT
            var follows = listOf<Any>()
            var fans = listOf<Any>()
            var error = DefaultValue.DEFAULT_BYTE
        }

        val isLogin = (session.getAttribute("isLogin") as? String ?: "false").toBoolean()
        if (!isLogin) {
            userInfo.error = 1.toByte()

            return userInfo
        }

        val sessionUsername = session.getAttribute("username") as? String ?: User.USERNAME_DEFAULT
        dbUser = userService.fetchUserByUsername(sessionUsername)

        userInfo.id = dbUser.id
        userInfo.username = dbUser.username
        userInfo.name = dbUser.nickName
        userInfo.phone = dbUser.phone
        userInfo.gender = dbUser.genderId
        userInfo.email = dbUser.email
        userInfo.level = dbUser.levelId
        // TODO: localdebug
//        userInfo.avatar = userService.readAvatar(dbUser.avatarPath)
        userInfo.birthday = dbUser.birthday
        userInfo.follows = followsInfoTransformer(followService.fetchAllFollows(userInfo.id))
        userInfo.fans = fansInfoTransformer(fanService.fetchAllFans(userInfo.id))
        userInfo.error = 0.toByte()
        return userInfo
    }
}
