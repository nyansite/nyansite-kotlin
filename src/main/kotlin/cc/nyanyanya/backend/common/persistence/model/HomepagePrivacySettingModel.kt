package cc.nyanyanya.backend.common.persistence.model

import cc.nyanyanya.backend.common.util.bo.DefaultValue
import com.baomidou.mybatisplus.annotation.IdType
import com.baomidou.mybatisplus.annotation.TableField
import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.annotation.TableName
import java.util.UUID

@TableName(schema = "user_management_", value = "homepage_privacy_setting_")
class HomepagePrivacySettingModel(
    @TableId(value = "id_", type = IdType.INPUT)
    var id: UUID,

    @TableField(value = "user_id_")
    var userId: UUID,

    @TableField(value = "is_display_phone_")
    var isDisplayPhone: Boolean,

    @TableField(value = "is_display_mail_")
    var isDisplayMail: Boolean,

    @TableField(value = "is_display_birthday_")
    var isDisplayBirthday: Boolean,

    @TableField(value = "is_display_follows_and_fans_")
    var isDisplayFollowsAndFans: Boolean,
) {
    companion object {
        val ID_DEFAULT = DefaultValue().DEFAULT_UUID
        val USER_ID_DEFAULT = DefaultValue().DEFAULT_UUID
        const val IS_DISPLAY_PHONE_DEFAULT = false
        const val IS_DISPLAY_MAIL_DEFAULT = false
        const val IS_DISPLAY_BIRTHDAY_DEFAULT = false
        const val IS_DISPLAY_FOLLOWS_AND_FANS_DEFAULT = false
    }

    constructor() : this(
        id = ID_DEFAULT,
        userId = USER_ID_DEFAULT,
        isDisplayPhone = IS_DISPLAY_PHONE_DEFAULT,
        isDisplayMail = IS_DISPLAY_MAIL_DEFAULT,
        isDisplayBirthday = IS_DISPLAY_BIRTHDAY_DEFAULT,
        isDisplayFollowsAndFans = IS_DISPLAY_FOLLOWS_AND_FANS_DEFAULT,
    )
}
