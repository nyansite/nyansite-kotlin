package cc.nyanyanya.backend.common.persistence.model

import cc.nyanyanya.backend.common.util.type_handler.UuidTypeHandler
import cc.nyanyanya.backend.common.util.bo.DefaultValue
import com.baomidou.mybatisplus.annotation.IdType
import com.baomidou.mybatisplus.annotation.TableField
import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.annotation.TableName
import java.util.UUID

@TableName(schema = "user_management_", value = "fan_group_")
class FanGroup(
    @TableId(value = "id_", type = IdType.AUTO)
    var id: UUID,

    @TableField(value = "user_id_")
    var userId: UUID,

    @TableField(value = "number_")
    var number: Short,

    @TableField(value = "name_")
    var name: String,
) {
    companion object {
        val ID_DEFAULT = DefaultValue().DEFAULT_UUID
        val USER_ID_DEFAULT = DefaultValue().DEFAULT_UUID
        const val NUMBER_DEFAULT: Short = DefaultValue.DEFAULT_SHORT
        const val NAME_DEFAULT = ""
    }

    constructor() : this(
        id = ID_DEFAULT,
        userId = USER_ID_DEFAULT,
        number = NUMBER_DEFAULT,
        name = NAME_DEFAULT
    )
}
