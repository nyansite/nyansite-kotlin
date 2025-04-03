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
            return ResultErrorCode(0)
        }
        return ResultErrorCode(2)
    }

    @PostMapping("/get-own-info")
    fun getOwnInfo(
        session: HttpSession,
    ): Any {
        var dbUserModel = UserModel()

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
        dbUserModel = userService.fetchUserByUsername(sessionUsername)

        userInfo.id = dbUserModel.id
        userInfo.username = dbUserModel.username
        userInfo.name = dbUserModel.nickName
        userInfo.phone = dbUserModel.phone
        userInfo.gender = dbUserModel.genderId
        userInfo.email = dbUserModel.email
        userInfo.level = dbUserModel.levelId
        // TODO: localdebug
//        userInfo.avatar = userService.readAvatar(dbUser.avatarPath)
        userInfo.birthday = dbUserModel.birthday
        userInfo.follows = followsInfoTransformer(followService.fetchAllFollows(userInfo.id))
        userInfo.fans = fansInfoTransformer(fanService.fetchAllFans(userInfo.id))
        userInfo.error = 0.toByte()
        return userInfo
    }

    @PostMapping("/get-user-info")
    fun getUserInfo(
        session: HttpSession,
    ): Any {
        var dbUserModel = UserModel()

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
        dbUserModel = userService.fetchUserByUsername(sessionUsername)

        userInfo.id = dbUserModel.id
        userInfo.username = dbUserModel.username
        userInfo.name = dbUserModel.nickName
        userInfo.phone = dbUserModel.phone
        userInfo.gender = dbUserModel.genderId
        userInfo.email = dbUserModel.email
        userInfo.level = dbUserModel.levelId
        // TODO: localdebug
//        userInfo.avatar = userService.readAvatar(dbUser.avatarPath)
        userInfo.birthday = dbUserModel.birthday
        userInfo.follows = followsInfoTransformer(followService.fetchAllFollows(userInfo.id))
        userInfo.fans = fansInfoTransformer(fanService.fetchAllFans(userInfo.id))
        userInfo.error = 0.toByte()
        return userInfo
    }
}
