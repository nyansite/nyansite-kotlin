package cc.nyanyanya.backend.common.persistence.model

import cc.nyanyanya.backend.common.util.bo.DefaultValue
import cc.nyanyanya.backend.common.util.type_handler.UuidTypeHandler
import com.baomidou.mybatisplus.annotation.IdType
import com.baomidou.mybatisplus.annotation.TableField
import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.annotation.TableName
import org.apache.ibatis.type.JdbcType
import java.util.UUID

@TableName(schema = "user_management_", value = "homepage_privacy_setting_")
class HomepagePrivacySettingModel(
    @TableId(value = "id_", type = IdType.INPUT)
    @TableField(jdbcType = JdbcType.VARCHAR, typeHandler = UuidTypeHandler::class)
    var id: UUID = ID_DEFAULT,

    @TableField(value = "user_id_", jdbcType = JdbcType.VARCHAR, typeHandler = UuidTypeHandler::class)
    var userId: UUID = USER_ID_DEFAULT,

    @TableField(value = "is_display_phone_")
    var isDisplayPhone: Boolean = IS_DISPLAY_PHONE_DEFAULT,

    @TableField(value = "is_display_mail_")
    var isDisplayMail: Boolean = IS_DISPLAY_MAIL_DEFAULT,

    @TableField(value = "is_display_birthday_")
    var isDisplayBirthday: Boolean = IS_DISPLAY_BIRTHDAY_DEFAULT,

    @TableField(value = "is_display_follows_and_fans_")
    var isDisplayFollowsAndFans: Boolean = IS_DISPLAY_FOLLOWS_AND_FANS_DEFAULT
) {
    companion object {
        val ID_DEFAULT = DefaultValue().DEFAULT_UUID
        val USER_ID_DEFAULT = DefaultValue().DEFAULT_UUID
        const val IS_DISPLAY_PHONE_DEFAULT = false
        const val IS_DISPLAY_MAIL_DEFAULT = false
        const val IS_DISPLAY_BIRTHDAY_DEFAULT = false
        const val IS_DISPLAY_FOLLOWS_AND_FANS_DEFAULT = false
    }
}
