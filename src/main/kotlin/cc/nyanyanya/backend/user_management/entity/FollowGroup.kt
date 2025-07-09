package cc.nyanyanya.backend.user_management.entity

import cc.nyanyanya.backend.common.persistence.model.FanModel
import cc.nyanyanya.backend.common.util.bo.DefaultValue
import java.util.*

data class FollowGroup (
    var userId: UUID = USER_ID_DEFAULT,
    var name: String = NAME_DEFAULT,
    var follows: MutableList<FanModel> = FOLLOWS_DEFAULT,
) {
    companion object {
        val USER_ID_DEFAULT = DefaultValue().DEFAULT_UUID
        const val NAME_DEFAULT = ""
        val FOLLOWS_DEFAULT = mutableListOf<FanModel>()
    }
}
