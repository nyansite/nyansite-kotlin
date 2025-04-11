package cc.nyanyanya.backend.common.persistence.model

import cc.nyanyanya.backend.common.util.GlobalVariables
import cc.nyanyanya.backend.common.util.bo.DefaultValue
import com.baomidou.mybatisplus.annotation.IdType
import com.baomidou.mybatisplus.annotation.TableField
import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.annotation.TableName
import kotlinx.coroutines.*
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.client.RestClient
import org.springframework.web.client.toEntity
import java.nio.charset.Charset
import java.sql.Timestamp
import java.util.*

@TableName(schema = "user_management_", value = "user_")
data class UserModel(
    @TableId(
        value = "id_",
        type = IdType.INPUT,
    )
    var id: UUID = ID_DEFAULT,

    @TableField(value = "username_")
    var username: String = USERNAME_DEFAULT,

    @TableField(value = "nickname_")
    var nickName: String = NICKNAME_DEFAULT,

    @TableField(value = "phone_")
    var phone: String = PHONE_DEFAULT,

    @TableField(value = "gender_id_")
    var genderId: Short = GENDER_ID_DEFAULT,

    @TableField(value = "email_")
    var email: String = EMAIL_DEFAULT,

    @TableField(value = "avatar_path_")
    var avatarPath: String = AVATAR_PATH_DEFAULT,

    @TableField(value = "birthday_")
    var birthday: Date = BIRTHDAY_DEFAULT,

    @TableField(value = "level_id_")
    var levelId: Short = LEVEL_ID_DEFAULT,

    @TableField(value = "signup_time_")
    var signupTime: Timestamp = SIGNUP_TIME_DEFAULT,

    @TableField(value = "password_")
    var password: String = PASSWORD_DEFAULT,

    @TableField(value = "phone_modify_time_")
    var phoneModifyTime: Timestamp = PHONE_MODIFY_TIME_DEFAULT,

    @TableField(value = "email_modify_time_")
    var emailModifyTime: Timestamp = EMAIL_MODIFY_TIME_DEFAULT,
) {
    companion object {
        val ID_DEFAULT = DefaultValue().DEFAULT_UUID
        const val NICKNAME_DEFAULT = " "
        const val USERNAME_DEFAULT = "      "
        const val PHONE_DEFAULT = "000000000000"
        const val GENDER_ID_DEFAULT = DefaultValue.DEFAULT_SHORT
        const val EMAIL_DEFAULT = "-@-.--"
        const val AVATAR_PATH_DEFAULT = "  "
        val BIRTHDAY_DEFAULT = DefaultValue().DEFAULT_DATE
        const val LEVEL_ID_DEFAULT = DefaultValue.DEFAULT_SHORT
        val SIGNUP_TIME_DEFAULT = DefaultValue().DEFAULT_TIMESTAMP
        const val PASSWORD_DEFAULT = "      "
        val PHONE_MODIFY_TIME_DEFAULT = DefaultValue().DEFAULT_TIMESTAMP
        val EMAIL_MODIFY_TIME_DEFAULT = DefaultValue().DEFAULT_TIMESTAMP


        fun verifyEmailFormat(email: String): Boolean {
            val regexConditionOne = Regex("(?=.{1,253}\$)" +
                    "(?:[\\p{L}\\p{N}\\-]{0,61}[\\p{L}\\p{N}]\\.)+[\\p{L}\\p{N}]{1,62}\\p{L}")
            val regexConditionTwo = Regex("(?=.{1,254}\$)" +
                    "(?:[\\p{L}\\p{N}\\-]{0,61}[\\p{L}\\p{N}]\\.)+[\\p{L}\\p{N}]{1,62}\\p{L}\\.")

            val regex = Regex("^(?=.{6,320}\$)" +
                    "[\\p{L}\\p{N}._%+\\-]{1,64}" +
                    "@" +
                    "(?:" +
                    regexConditionOne.pattern +
                    "|" +
                    regexConditionTwo.pattern +
                    ")$")

            return regex.matches(email)
        }

        fun verifyPhoneFormat(phone: String): Boolean {
            val regex = Regex("^[\\d]{12,14}$")
            return regex.matches(phone)
        }

        fun verifyAvatarFormat(avatarEncodeString: String): Boolean {
            val avatarFileExtensionList = "jpg|gif|jpeg|png|gif|bmp|webp|svg|tiff|ico|jfif|tif"
            val regex = Regex("^data:image/(?:${avatarFileExtensionList});base64,([A-Za-z0-9+/=]+)$")
            return regex.matches(avatarEncodeString)
        }

        fun haveSensitiveWords(strToTest: String): Boolean {
            val SENSITIVE_WORDS = GlobalVariables().SENSITIVE_WORDS

            SENSITIVE_WORDS.forEach() { it ->
                it.value.forEach() { word ->
                    if (strToTest.contains(word, false)) {
                        return true
                    }
                }
            }
            return false
        }

        fun verifyUsernameFormat(username: String): Boolean {
            val regex = Regex("^[A-Za-z0-9_]{6,20}$")

            val notHaveSensitiveWords = !haveSensitiveWords(username)
            val inRightFormat = regex.matches(username)
            return notHaveSensitiveWords && inRightFormat
        }

        fun verifyNicknameFormat(nickname: String): Boolean {
            val notHaveSensitiveWords = !haveSensitiveWords(nickname)
            val inRightFormat = nickname.length in 1..64
            return notHaveSensitiveWords && inRightFormat
        }

        fun verifyPasswordFormat(password: String): Boolean {
            val regex = Regex("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[A-Za-z0-9_]{6,20}$")
            return regex.matches(password)
        }
    }

//    constructor() : this(
//        id = ID_DEFAULT,
//        username = USERNAME_DEFAULT,
//        nickName = NICKNAME_DEFAULT,
//        phone = PHONE_DEFAULT,
//        genderId = GENDER_ID_DEFAULT,
//        email = EMAIL_DEFAULT,
//        avatarPath = AVATAR_PATH_DEFAULT,
//        birthday = BIRTHDAY_DEFAULT,
//        levelId = LEVEL_ID_DEFAULT,
//        signupTime = SIGNUP_TIME_DEFAULT,
//        password = PASSWORD_DEFAULT,
//        phoneModifyTime = PHONE_MODIFY_TIME_DEFAULT,
//        emailModifyTime = EMAIL_MODIFY_TIME_DEFAULT,
//    )

    fun genAvatarFilePath(
        avatarEncodeString: String
    ): String {
        val regex = Regex("(?<=data:image/)[A-Za-z]+(?=;base64,[A-Za-z0-9+/=]+)")
        val fileExtension = regex.find(avatarEncodeString)?.value ?: ""

        if (fileExtension == "") return ""
        val filePath = "/opt/website/nyaside/backend/resources/images/avatar/${id}.${fileExtension}"
        this.avatarPath = filePath
        return filePath
    }

    fun genRandomUsername(): String {
        val distChars = ('a'..'z') + ('A'..'Z') + ('0'..'9') + ('_')
        val stringBuilder = StringBuilder().apply { (1 .. 30).onEach { append(distChars.random()) } }
        this.username = stringBuilder.toString()
        return username
    }

    fun genRandomNickname(): String {
        val distChars = ('a'..'z') + ('A'..'Z') + ('0'..'9') + ('_')
        val stringBuilder = StringBuilder().apply { (1 .. 64).onEach { append(distChars.random()) } }
        this.nickName = stringBuilder.toString()
        return nickName
    }

    private fun fetchAvatar(restClient: RestClient): ResponseEntity<String> {
        val url = "https://api.dicebear.com/9.x/initials/svg?seed=${nickName[0]}" +
                "&backgroundType=gradientLinear&backgroundRotation=${(0..360).random()}"
        val result = restClient.get()
            .uri(url)
            .retrieve()
            .toEntity<String>()

        return result
    }

    suspend fun genRandomAvatar(): String = coroutineScope{
        val restClient = RestClient.create()

        val asyncResult = async {
            var result = fetchAvatar(restClient)
            var avatar = result.body ?: ""

            while (result.statusCode != HttpStatus.OK) {
                delay(1000)
                result = fetchAvatar(restClient)
                avatar = result.body ?: ""
            }

            return@async avatar
        }

        val avatarString = asyncResult.await()
        val avatarBase64String = Base64.getEncoder()
            .encodeToString(avatarString.toByteArray(Charset.forName("UTF-8")))
        val encodeString = "data:image/svg;base64,${avatarBase64String}"
        return@coroutineScope encodeString
    }

    fun genSignupTime(): Timestamp {
        this.signupTime = Timestamp(System.currentTimeMillis())
        return signupTime
    }

    fun isChangeEmailTooOften(): Boolean {
        val CHANGE_EMAIL_MINUS_DURING = 30 * 24 * 60 * 60 * 1000L //一个月
        val currentTime = System.currentTimeMillis()
        val timeDifference = currentTime - this.emailModifyTime.time
        return timeDifference < CHANGE_EMAIL_MINUS_DURING
    }
}
