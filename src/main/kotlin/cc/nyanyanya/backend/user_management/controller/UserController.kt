package cc.nyanyanya.backend.user_management.controller


import cc.nyanyanya.backend.common.persistence.model.FanModel
import cc.nyanyanya.backend.common.persistence.model.UserModel
import cc.nyanyanya.backend.common.util.LogicTool
import cc.nyanyanya.backend.common.util.VerificationCode
import cc.nyanyanya.backend.common.util.bo.DefaultValue
import cc.nyanyanya.backend.common.util.bo.ResultErrorCode
import cc.nyanyanya.backend.user_management.entity.FollowList
import cc.nyanyanya.backend.user_management.service.EmailService
import cc.nyanyanya.backend.user_management.service.FanService
import cc.nyanyanya.backend.user_management.service.FollowService
import cc.nyanyanya.backend.user_management.service.UserService
import jakarta.servlet.http.HttpSession
import org.apache.commons.lang3.time.DateUtils
import org.springframework.aot.hint.annotation.RegisterReflectionForBinding
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/user")
class UserController(
    private val userService: UserService,
    private val followService: FollowService,
    private val fanService: FanService,
    private val emailService: EmailService,
) {
    @PostMapping("/verify-account")
    fun logout(
        @RequestParam username: String,
        @RequestParam email: String,
    ): ResultErrorCode {
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
            return ResultErrorCode(1)
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
            return ResultErrorCode(0)
        }
        return ResultErrorCode(2)
    }

    @RegisterReflectionForBinding
    @Component
    @Scope("prototype")
    data class UserInfo(
        var id: UUID = UserModel.ID_DEFAULT,
        var username: String = UserModel.USERNAME_DEFAULT,
        var name: String = UserModel.NICKNAME_DEFAULT,
        var gender: Short = UserModel.GENDER_ID_DEFAULT,
        var mail: String = UserModel.EMAIL_DEFAULT,
        var level: Short = UserModel.LEVEL_ID_DEFAULT,
        var avatar: String = "",
        var birthday: Date = UserModel.BIRTHDAY_DEFAULT,
        var follows: List<Any> = listOf<Any>(),
        var fans: List<Any> = listOf<Any>(),
        var error: Byte = DefaultValue.DEFAULT_BYTE,
    )

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

        val userInfo = UserInfo()

        val isLogin = session.getAttribute("isLogin") as? Boolean ?: false
        if (!isLogin) {
            userInfo.error = 1.toByte()

            return userInfo
        }

        val sessionUsername = session.getAttribute("username") as? String ?: UserModel.USERNAME_DEFAULT
        dbUser = userService.fetchUserByUsername(sessionUsername)

        userInfo.id = dbUser.id
        userInfo.username = dbUser.username
        userInfo.name = dbUser.nickName
        userInfo.gender = dbUser.genderId
        userInfo.mail = dbUser.email
        userInfo.level = dbUser.levelId
        // TODO: localdebug
        userInfo.avatar = userService.loadAvatar(dbUser.avatarPath)
        userInfo.birthday = dbUser.birthday
        userInfo.follows = followsInfoTransformer(followService.fetchAllFollows(dbUser.id))
        userInfo.fans = fansInfoTransformer(fanService.fetchAllFans(dbUser.id))
        userInfo.error = 0.toByte()
        return userInfo
    }

    @RegisterReflectionForBinding
    @Component
    @Scope("prototype")
    data class UsernameInfo(
        var id: UUID = UserModel.ID_DEFAULT,
        var error: Byte = DefaultValue.DEFAULT_BYTE,
    )

    @PostMapping("/get-user-id")
    fun getUserId(
        @RequestParam username: String,
        session: HttpSession,
    ): Any {
        val usernameInfo = UsernameInfo()

        if (username == "") {
            usernameInfo.error = 1.toByte()

            return usernameInfo
        }

        val isLogin = session.getAttribute("isLogin") as? Boolean ?: false
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

    @RegisterReflectionForBinding
    @Component
    @Scope("prototype")
    data class OtherUserInfo(
        var username: String = UserModel.USERNAME_DEFAULT,
        var name: String = UserModel.NICKNAME_DEFAULT,
        var gender: Short = UserModel.GENDER_ID_DEFAULT,
        var mail: String = UserModel.EMAIL_DEFAULT,
        var level: Short = UserModel.LEVEL_ID_DEFAULT,
        var avatar: String = "",
        var birthday: Date = UserModel.BIRTHDAY_DEFAULT,
        var follows: List<Any> = listOf<Any>(),
        var fans: List<Any> = listOf<Any>(),
        var error: Byte = DefaultValue.DEFAULT_BYTE,
    )

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

        val otherUserInfo = OtherUserInfo()

        if (id == "") {
            otherUserInfo.error = 1.toByte()

            return otherUserInfo
        }

        val isLogin = session.getAttribute("isLogin") as? Boolean ?: false
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
        otherUserInfo.gender = dbOtherUser.genderId
        otherUserInfo.mail = dbOtherUser.email
        otherUserInfo.level = dbOtherUser.levelId
        // TODO: localdebug
        otherUserInfo.avatar = userService.loadAvatar(dbOtherUser.avatarPath)
        otherUserInfo.birthday = dbOtherUser.birthday
        otherUserInfo.follows = followsInfoTransformer(followService.fetchAllFollows(dbOtherUser.id))
        otherUserInfo.fans = fansInfoTransformer(fanService.fetchAllFans(dbOtherUser.id))
        otherUserInfo.error = 0.toByte()
        return otherUserInfo
    }

    @PostMapping("/change-passwd")
    fun changePasswd(
        @RequestParam email: String,
        @RequestParam password: String,
        @RequestParam code: String,
        session: HttpSession,
    ): Any {
        if (email == "") {
            return ResultErrorCode(3)
        }

        if (password == "") {
            return ResultErrorCode(6)
        }

        val sessionEmailToVerify = session.getAttribute("emailToVerify") as? String ?: UserModel.EMAIL_DEFAULT
        if (email != sessionEmailToVerify) {
            return ResultErrorCode(4)
        }

        val sessionLastEmailCodeSendTime = session.getAttribute("lastEmailCodeSendTime") as? Long
            ?: DefaultValue().DEFAULT_TIMESTAMP.time
        if (System.currentTimeMillis() - sessionLastEmailCodeSendTime > VerificationCode.EXPIRATION_DURING) {
            return ResultErrorCode(5)
        }

        val sessionVerificationCode = session.getAttribute("verificationCode") as? String
            ?: ""
        if (code != sessionVerificationCode) {
            return ResultErrorCode(2)
        }

        if (!UserModel.verifyPasswordFormat(password)) {
            return ResultErrorCode(7)
        }

        var dbUser = UserModel()
        dbUser = userService.fetchUserByEmail(email)
        if (dbUser == UserModel()) {
            return ResultErrorCode(1)
        }

        dbUser.password = password
        userService.modifyUser(dbUser)
        return ResultErrorCode(0)
    }

    @PostMapping("/get-passwd")
    fun getPasswd(
        @RequestParam email: String,
        @RequestParam password: String,
        @RequestParam code: String,
        session: HttpSession,
    ): Any {
        if (email == "") {
            return ResultErrorCode(3)
        }

        val sessionEmailToVerify = session.getAttribute("emailToVerify") as? String ?: UserModel.EMAIL_DEFAULT
        if (email != sessionEmailToVerify) {
            return ResultErrorCode(4)
        }

        val lastEmailCodeSendTime = session.getAttribute("lastEmailCodeSendTime") as? Long
            ?: DefaultValue().DEFAULT_TIMESTAMP.time
        if (System.currentTimeMillis() - lastEmailCodeSendTime > VerificationCode.EXPIRATION_DURING) {
            return ResultErrorCode(5)
        }

        val sessionVerificationCode = session.getAttribute("verificationCode") as? String
            ?: ""
        if (code != sessionVerificationCode) {
            return ResultErrorCode(2)
        }

        var dbUser = UserModel()
        dbUser = userService.fetchUserByEmail(email)
        if (dbUser == UserModel()) {
            return ResultErrorCode(1)
        }

        emailService.sendPasswordToEmail(dbUser)
        return ResultErrorCode(0)
    }

    @PostMapping("/dropout")
    fun dropout(
        @RequestParam email: String,
        @RequestParam code: String,
        session: HttpSession,
    ): Any {
        if (email == "") {
            return ResultErrorCode(3)
        }

        val sessionEmailToVerify = session.getAttribute("emailToVerify") as? String ?: UserModel.EMAIL_DEFAULT
        if (email != sessionEmailToVerify) {
            return ResultErrorCode(4)
        }

        val isLogin = session.getAttribute("isLogin") as? Boolean ?: false
        if (!isLogin) {
            return ResultErrorCode(1)
        }

        val lastEmailCodeSendTime = session.getAttribute("lastEmailCodeSendTime") as? Long
            ?: DefaultValue().DEFAULT_TIMESTAMP.time
        if (System.currentTimeMillis() - lastEmailCodeSendTime > VerificationCode.EXPIRATION_DURING) {
            return ResultErrorCode(5)
        }

        val sessionVerificationCode = session.getAttribute("verificationCode") as? String
            ?: ""
        if (code != sessionVerificationCode) {
            return ResultErrorCode(2)
        }

        var dbUser = UserModel()
        dbUser = userService.fetchUserByEmail(email)
        userService.removeUser(dbUser)
        return ResultErrorCode(0)
    }

    @PostMapping("/set-own-info")
    fun setOwnInfo(
        @RequestParam username: String,
        @RequestParam name: String,
        @RequestParam gender: String,
        @RequestParam(name = "original_mail") originalMail: String,
        @RequestParam mail: String,
        @RequestParam avatar: String,
        @RequestParam birthday: String,
        @RequestParam code: String,
        @RequestParam(name = "original_code") originalCode: String,
        session: HttpSession,
    ): Any {
        var dbUser = UserModel()

        if (mail != "" && originalMail == "") {
            return ResultErrorCode(3)
        }

        if (username == "" && name == "" && gender == "" && mail == "" && avatar == "" && birthday == "") {
            return ResultErrorCode(2)
        }

        val isLogin = session.getAttribute("isLogin") as? Boolean ?: false
        if (!isLogin) {
            return ResultErrorCode(1)
        }

        val sessionId = session.getAttribute("id") as? UUID ?: UserModel.ID_DEFAULT
        dbUser = userService.fetchUserById(sessionId)
        if (mail != "" && dbUser.isChangeEmailTooOften()) {
            return ResultErrorCode(12)
        }

        val sessionLastOriginalEmailCodeSendTime = session.getAttribute("lastOriginalEmailCodeSendTime") as? Long
            ?: DefaultValue().DEFAULT_TIMESTAMP.time
        if (mail != "" &&
                System.currentTimeMillis() - sessionLastOriginalEmailCodeSendTime > VerificationCode.EXPIRATION_DURING) {
            return ResultErrorCode(7)
        }

        val sessionLastEmailCodeSendTime = session.getAttribute("lastEmailCodeSendTime") as? Long
            ?: DefaultValue().DEFAULT_TIMESTAMP.time
        if (mail != "" &&
                System.currentTimeMillis() - sessionLastEmailCodeSendTime > VerificationCode.EXPIRATION_DURING) {
            return ResultErrorCode(9)
        }

        val sessionOriginalEmailToVerify = session.getAttribute("originalEmailToVerify") as? String
            ?: UserModel.EMAIL_DEFAULT
        if (mail != "" && (originalMail != sessionOriginalEmailToVerify || dbUser.email != originalMail)) {
            return ResultErrorCode(4)
        }

        val sessionEmailToVerify = session.getAttribute("emailToVerify") as? String ?: UserModel.EMAIL_DEFAULT
        if (mail != "" && mail != sessionEmailToVerify) {
            return ResultErrorCode(5)
        }

        val sessionOriginalVerificationCode = session.getAttribute("originalVerificationCode") as? String ?: ""
        if (mail != "" && originalCode != sessionOriginalVerificationCode) {
            return ResultErrorCode(6)
        }

        val sessionVerificationCode = session.getAttribute("verificationCode") as? String ?: ""
        if (mail != "" && code != sessionVerificationCode) {
            return ResultErrorCode(8)
        }

        val isFormatCorrect =
            ((username == "") || (username != "" && UserModel.verifyUsernameFormat(username))) &&
            ((name == "") || (name != "" && UserModel.verifyNicknameFormat(name))) &&
            ((mail == "") || (mail != "" && UserModel.verifyEmailFormat(mail))) &&
            ((avatar == "") || (avatar != "" && UserModel.verifyAvatarFormat(avatar)))
        if (!isFormatCorrect) {
            return ResultErrorCode(10)
        }

        val isNotRegistered =
            (username != "" && userService.fetchUserByUsername(username) == UserModel()) ||
                    (name != "" && userService.fetchUserByNickname(name) == UserModel()) ||
                    (mail != "" && userService.fetchUserByEmail(mail) == UserModel())
        if (!isNotRegistered) {
            return ResultErrorCode(11)
        }

        if (username != "") { dbUser.username = username }
        if (name != "") { dbUser.nickName = name }
        if (gender != "") { userService.setUserGenderId(dbUser, gender) }
        if (mail != "") { dbUser.email = mail }
        if (avatar != "") { userService.saveAvatar(dbUser, avatar)}
        if (birthday != "") { dbUser.birthday = DateUtils.parseDate(birthday, "yyyy-MM-dd") }
        userService.modifyUser(dbUser)
        return ResultErrorCode(0)
    }

    @PostMapping("/signup")
    fun signup(
        @RequestParam username: String,
        @RequestParam mail: String,
        @RequestParam password: String,
        @RequestParam code: String,
        session: HttpSession,
    ): Any {
        var dbUser = UserModel()

        val isLogin = session.getAttribute("isLogin") as? Boolean ?: false
        if (isLogin) {
            return ResultErrorCode(1)
        }

        if (!UserModel.verifyPasswordFormat(password)) {
            return ResultErrorCode(3)
        }

        val isJustOneNotNull = LogicTool.isJustOneNotNull(
            listOf(
                username,
                mail,
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

        val sessionLastEmailCodeSendTime = session.getAttribute("lastEmailCodeSendTime") as? Long
            ?: DefaultValue().DEFAULT_TIMESTAMP.time
        if (mail != "" &&
                System.currentTimeMillis() - sessionLastEmailCodeSendTime > VerificationCode.EXPIRATION_DURING) {
            return ResultErrorCode(6)
        }

        val sessionEmailToVerify = session.getAttribute("emailToVerify") as? String ?: UserModel.EMAIL_DEFAULT
        if (mail != "" && mail != sessionEmailToVerify) {
            return ResultErrorCode(5)
        }

        val sessionVerificationCode = session.getAttribute("verificationCode") as? String ?: ""
        if (mail != "" && code != sessionVerificationCode) {
            return ResultErrorCode(5)
        }

        when (notNullArgIndex) {
            0 -> {
                // username
                val isUsernameHaveRightFormat = UserModel.verifyUsernameFormat(username)
                dbUser = userService.fetchUserByUsername(username)
                if (!isUsernameHaveRightFormat || dbUser != UserModel()) {
                    return ResultErrorCode(2)
                }
                userService.createUserWithUsername(username, password)
            }

            1 -> {
                // mail
                val isEmailHaveRightFormat = UserModel.verifyEmailFormat(mail)
                dbUser = userService.fetchUserByEmail(mail)
                if (!isEmailHaveRightFormat || dbUser != UserModel()) {
                    return ResultErrorCode(2)
                }
                userService.createUserWithEmail(mail, password)
            }
        }

        return ResultErrorCode(0)
    }
}
