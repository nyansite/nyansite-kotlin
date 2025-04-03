package cc.nyanyanya.backend.common.persistence.model

import cc.nyanyanya.backend.common.util.type_handler.UuidTypeHandler
import cc.nyanyanya.backend.common.util.bo.DefaultValue
import com.baomidou.mybatisplus.annotation.*
import java.util.*

@TableName(schema = "user_management_", value = "fan_")
class Fan(
    @TableId(value = "id_", type = IdType.INPUT)
    var id: UUID,

    @TableField(value = "user_id_")
    var userId: UUID,

    @TableField("fan_id_")
    var fanId: UUID,

    @TableField("group_id_")
    var groupId: UUID,
) {
    companion object {
        val ID_DEFAULT = DefaultValue().DEFAULT_UUID
        val USER_ID_DEFAULT = DefaultValue().DEFAULT_UUID
        val FAN_ID_DEFAULT = DefaultValue().DEFAULT_UUID
        val GROUP_ID_DEFAULT = DefaultValue().DEFAULT_UUID
    }

    constructor() : this(
        id = ID_DEFAULT,
        userId = USER_ID_DEFAULT,
        fanId = FAN_ID_DEFAULT,
        groupId = GROUP_ID_DEFAULT
    )
}
