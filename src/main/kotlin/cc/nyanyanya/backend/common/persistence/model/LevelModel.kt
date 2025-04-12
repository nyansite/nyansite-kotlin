package cc.nyanyanya.backend.common.persistence.model

import cc.nyanyanya.backend.common.util.bo.DefaultValue
import com.baomidou.mybatisplus.annotation.IdType
import com.baomidou.mybatisplus.annotation.TableField
import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.annotation.TableName

@TableName(schema = "user_management_", value = "level_")
class LevelModel(
    @TableId(value = "id_", type = IdType.AUTO)
    var id: Short? = ID_DEFAULT,

    @TableField(value = "number_")
    var number: Short = NUMBER_DEFAULT,

    @TableField(value = "name_")
    var name: String = NAME_DEFAULT,
) {
    companion object {
        const val ID_DEFAULT = DefaultValue.DEFAULT_SHORT
        const val NUMBER_DEFAULT = DefaultValue.DEFAULT_SHORT
        const val NAME_DEFAULT = ""
    }
}
