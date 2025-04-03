package cc.nyanyanya.backend.common.persistence.model

import cc.nyanyanya.backend.common.util.bo.DefaultValue
import com.baomidou.mybatisplus.annotation.IdType
import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.annotation.TableField
import com.baomidou.mybatisplus.annotation.TableName
import java.sql.Timestamp
import java.util.Date
import java.util.UUID

@TableName(schema = "user_management_", value = "user_")
data class UserModel(
    @TableId(
        value = "id_",
        type = IdType.INPUT,
    )
    var id: UUID,

    @TableField(value = "username_")
    var username: String,

    @TableField(value = "nickname_")
    var nickName: String,

    @TableField(value = "phone_")
    var phone: String,

    @TableField(value = "gender_id_")
    var genderId: Short,

    @TableField(value = "email_")
    var email: String,

    @TableField(value = "avatar_path_")
    var avatarPath: String = "  ",

    @TableField(value = "birthday_")
    var birthday: Date,

    @TableField(value = "level_id_")
    var levelId: Short,

    @TableField(value = "signup_time_")
    var signupTime: Timestamp,

    @TableField(value = "password_")
    var password: String,

    @TableField(value = "phone_modify_time_")
    var phoneModifyTime: Timestamp,
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
    }

    constructor() : this(
        id = ID_DEFAULT,
        username = USERNAME_DEFAULT,
        nickName = NICKNAME_DEFAULT,
        phone = PHONE_DEFAULT,
        genderId = GENDER_ID_DEFAULT,
        email = EMAIL_DEFAULT,
        avatarPath = AVATAR_PATH_DEFAULT,
        birthday = BIRTHDAY_DEFAULT,
        levelId = LEVEL_ID_DEFAULT,
        signupTime = SIGNUP_TIME_DEFAULT,
        password = PASSWORD_DEFAULT,
        phoneModifyTime = PHONE_MODIFY_TIME_DEFAULT
    )
}
