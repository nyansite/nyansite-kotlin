package cc.nyanyanya.backend.common.persistence.model

import cc.nyanyanya.backend.common.util.bo.DefaultValue
import cc.nyanyanya.backend.common.util.type_handler.UuidTypeHandler
import com.baomidou.mybatisplus.annotation.IdType
import com.baomidou.mybatisplus.annotation.TableField
import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.annotation.TableName
import org.apache.ibatis.type.JdbcType

@TableName(schema = "user_management_", value = "gender_")
class GenderModel(
    @TableId(value = "id_", type = IdType.AUTO)
    @TableField(jdbcType = JdbcType.VARCHAR, typeHandler = UuidTypeHandler::class)
    var id: Short? = null,

    @TableField(value = "name_")
    var name: String = NAME_DEFAULT,

    @TableField(value = "alias_")
    var alias: String = ALIAS_DEFAULT,
) {
    companion object {
        val ID_DEFAULT = DefaultValue.DEFAULT_SHORT
        const val NAME_DEFAULT = ""
        const val ALIAS_DEFAULT = ""
    }
}
