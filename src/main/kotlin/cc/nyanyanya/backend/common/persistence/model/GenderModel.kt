package cc.nyanyanya.backend.common.persistence.model

import cc.nyanyanya.backend.common.util.bo.DefaultValue
import com.baomidou.mybatisplus.annotation.IdType
import com.baomidou.mybatisplus.annotation.TableField
import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.annotation.TableName

@TableName(schema = "user_management_", value = "gender_")
class GenderModel(
    @TableId(value = "id_", type = IdType.AUTO)
    var id: Short,

    @TableField(value = "name_")
    var name: String,

    @TableField(value = "alias_")
    var alias: String,
) {
    companion object {
        val ID_DEFAULT = DefaultValue.DEFAULT_SHORT
        const val NAME_DEFAULT = ""
        const val ALIAS_DEFAULT = ""
    }

    constructor() : this(
        id = ID_DEFAULT,
        name = NAME_DEFAULT,
        alias = ALIAS_DEFAULT
    )
}
