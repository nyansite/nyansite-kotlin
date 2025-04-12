package cc.nyanyanya.backend.common.persistence.model

import cc.nyanyanya.backend.common.util.bo.DefaultValue
import cc.nyanyanya.backend.common.util.type_handler.UuidTypeHandler
import com.baomidou.mybatisplus.annotation.IdType
import com.baomidou.mybatisplus.annotation.TableField
import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.annotation.TableName
import org.apache.ibatis.type.JdbcType
import java.util.UUID

@TableName(schema = "user_management_", value = "fan_group_")
class FanGroupModel(
    @TableId(value = "id_", type = IdType.AUTO)
    @TableField(jdbcType = JdbcType.VARCHAR, typeHandler = UuidTypeHandler::class)
    var id: UUID = ID_DEFAULT,

    @TableField(value = "user_id_", jdbcType = JdbcType.VARCHAR, typeHandler = UuidTypeHandler::class)
    var userId: UUID = USER_ID_DEFAULT,

    @TableField(value = "number_")
    var number: Short = NUMBER_DEFAULT,

    @TableField(value = "name_")
    var name: String = NAME_DEFAULT
) {
    companion object {
        val ID_DEFAULT = DefaultValue().DEFAULT_UUID
        val USER_ID_DEFAULT = DefaultValue().DEFAULT_UUID
        const val NUMBER_DEFAULT: Short = DefaultValue.DEFAULT_SHORT
        const val NAME_DEFAULT = ""
    }
}
