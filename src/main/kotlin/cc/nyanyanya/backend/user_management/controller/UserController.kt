package cc.nyanyanya.backend.user_management.controller


import cc.nyanyanya.backend.common.persistence.model.FanModel
import cc.nyanyanya.backend.common.persistence.model.UserModel
import cc.nyanyanya.backend.common.util.LogicTool
import cc.nyanyanya.backend.common.util.bo.DefaultValue
import cc.nyanyanya.backend.common.util.bo.ResultErrorCode
import cc.nyanyanya.backend.user_management.entity.FollowList
import cc.nyanyanya.backend.user_management.service.FanService
import cc.nyanyanya.backend.user_management.service.FollowService
import cc.nyanyanya.backend.user_management.service.UserService
import jakarta.servlet.http.HttpSession
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/user")
class UserController(
    private val userService: UserService,
    private val followService: FollowService,
    private val fanService: FanService
) {
    @PostMapping("/verify-account")
    fun logout(
        @RequestParam username: String,
        @RequestParam email: String,
        @RequestParam phone: String,
    ): ResultErrorCode {
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
            return ResultErrorCode(1)
        }
        val notNullArgIndex = isJustOneNull.trueIndex

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

            2 -> {
                // phone
                dbUser = userService.fetchUserByPhone(phone)
            }
        }

        if (dbUser == UserModel()) {
            return ResultErrorCode(0)
        }
        return ResultErrorCode(2)
    }

    @PostMapping("/get-own-info")
    fun getOwnInfo(
        session: HttpSession,
    ): Any {
        var dbUser = UserModel()

        val fansInfoTransformer = { fanModelList: List<FanModel> ->
            val fansInfo = mutableListOf<UUID>()

            fanModelList.forEach { it ->
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
            var id = UserModel.ID_DEFAULT
            var username = UserModel.USERNAME_DEFAULT
            var name = UserModel.NICKNAME_DEFAULT
            var phone = UserModel.PHONE_DEFAULT
            var gender = UserModel.GENDER_ID_DEFAULT
            var email = UserModel.EMAIL_DEFAULT
            var level = UserModel.LEVEL_ID_DEFAULT
            var avatar = ""
            var birthday = UserModel.BIRTHDAY_DEFAULT
            var follows = listOf<Any>()
            var fans = listOf<Any>()
            var error = DefaultValue.DEFAULT_BYTE
        }

        val isLogin = (session.getAttribute("isLogin") as? String ?: "false").toBoolean()
        if (!isLogin) {
            userInfo.error = 1.toByte()

            return userInfo
        }

        val sessionUsername = session.getAttribute("username") as? String ?: UserModel.USERNAME_DEFAULT
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
        userInfo.follows = followsInfoTransformer(followService.fetchAllFollows(dbUser.id))
        userInfo.fans = fansInfoTransformer(fanService.fetchAllFans(dbUser.id))
        userInfo.error = 0.toByte()
        return userInfo
    }


    @PostMapping("/get-user-id")
    fun getUserId(
        @RequestParam username: String,
        session: HttpSession,
    ): Any {
        val usernameInfo = object {
            var id = UserModel.ID_DEFAULT
            var error = DefaultValue.DEFAULT_BYTE
        }

        if (username == "") {
            usernameInfo.error = 1.toByte()

            return usernameInfo
        }

        val isLogin = (session.getAttribute("isLogin") as? String ?: "false").toBoolean()
        if (!isLogin) {
            usernameInfo.error = 4.toByte()

            return usernameInfo
        }

        var dbUser = UserModel()
        val sessionUsername = session.getAttribute("username") as? String ?: UserModel.USERNAME_DEFAULT
        dbUser = userService.fetchUserByUsername(sessionUsername)

        if (dbUser.levelId <= 0) {
            usernameInfo.error = 3.toByte()

            return usernameInfo
        }

        var dbOtherUser = UserModel()
        dbOtherUser = userService.fetchUserByUsername(username)

        if (dbOtherUser == UserModel()) {
            usernameInfo.error = 2.toByte()

            return usernameInfo
        }

        usernameInfo.id = dbOtherUser.id
        usernameInfo.error = 0.toByte()
        return usernameInfo
    }


    @PostMapping("/get-user-info")
    fun getUserInfo(
        @RequestParam id: String,
        session: HttpSession,
    ): Any {
        val fansInfoTransformer = { fanModelList: List<FanModel> ->
            val fansInfo = mutableListOf<UUID>()

            fanModelList.forEach { it ->
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
        val otherUserInfo = object {
            var username = UserModel.USERNAME_DEFAULT
            var name = UserModel.NICKNAME_DEFAULT
            var phone = UserModel.PHONE_DEFAULT
            var gender = UserModel.GENDER_ID_DEFAULT
            var email = UserModel.EMAIL_DEFAULT
            var level = UserModel.LEVEL_ID_DEFAULT
            var avatar = ""
            var birthday = UserModel.BIRTHDAY_DEFAULT
            var follows = listOf<Any>()
            var fans = listOf<Any>()
            var error = DefaultValue.DEFAULT_BYTE
        }

        if (id == "") {
            otherUserInfo.error = 1.toByte()

            return otherUserInfo
        }

        val isLogin = (session.getAttribute("isLogin") as? String ?: "false").toBoolean()
        if (!isLogin) {
            otherUserInfo.error = 4.toByte()

            return otherUserInfo
        }

        var dbUser = UserModel()
        val sessionUsername = session.getAttribute("username") as? String ?: UserModel.USERNAME_DEFAULT
        dbUser = userService.fetchUserByUsername(sessionUsername)
        if (dbUser.levelId <= 0) {
            otherUserInfo.error = 3.toByte()

            return otherUserInfo
        }

        var dbOtherUser = UserModel()
        dbOtherUser = userService.fetchUserById(UUID.fromString(id))
        if (dbOtherUser == UserModel()) {
            otherUserInfo.error = 2.toByte()

            return otherUserInfo
        }

        otherUserInfo.username = dbOtherUser.username
        otherUserInfo.name = dbOtherUser.nickName
        otherUserInfo.phone = dbOtherUser.phone
        otherUserInfo.gender = dbOtherUser.genderId
        otherUserInfo.email = dbOtherUser.email
        otherUserInfo.level = dbOtherUser.levelId
        // TODO: localdebug
//        otherUserInfo.avatar = userService.readAvatar(dbOtherUser.avatarPath)
        otherUserInfo.birthday = dbOtherUser.birthday
        otherUserInfo.follows = followsInfoTransformer(followService.fetchAllFollows(dbOtherUser.id))
        otherUserInfo.fans = fansInfoTransformer(fanService.fetchAllFans(dbOtherUser.id))
        otherUserInfo.error = 0.toByte()
        return otherUserInfo
    }
}
