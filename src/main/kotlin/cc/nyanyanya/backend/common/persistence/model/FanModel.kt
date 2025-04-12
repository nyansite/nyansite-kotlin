package cc.nyanyanya.backend.common.persistence.model

import cc.nyanyanya.backend.common.util.bo.DefaultValue
import cc.nyanyanya.backend.common.util.type_handler.UuidTypeHandler
import com.baomidou.mybatisplus.annotation.*
import org.apache.ibatis.type.JdbcType
import java.util.*

@TableName(schema = "user_management_", value = "fan_")
class FanModel(
    @TableId(value = "id_", type = IdType.INPUT)
    @TableField(jdbcType = JdbcType.VARCHAR, typeHandler = UuidTypeHandler::class)
    var id: UUID = ID_DEFAULT,

    @TableField(value = "user_id_", jdbcType = JdbcType.VARCHAR, typeHandler = UuidTypeHandler::class)
    var userId: UUID = USER_ID_DEFAULT,

    @TableField("fan_id_", jdbcType = JdbcType.VARCHAR, typeHandler = UuidTypeHandler::class)
    var fanId: UUID = FAN_ID_DEFAULT,

    @TableField("group_id_", jdbcType = JdbcType.VARCHAR, typeHandler = UuidTypeHandler::class)
    var groupId: UUID = GROUP_ID_DEFAULT
) {
    companion object {
        val ID_DEFAULT = DefaultValue().DEFAULT_UUID
        val USER_ID_DEFAULT = DefaultValue().DEFAULT_UUID
        val FAN_ID_DEFAULT = DefaultValue().DEFAULT_UUID
        val GROUP_ID_DEFAULT = DefaultValue().DEFAULT_UUID
    }
}
