package cc.nyanyanya.backend.user_management.entity

import cc.nyanyanya.backend.common.persistence.model.Fan
import cc.nyanyanya.backend.common.util.bo.DefaultValue
import java.util.*

data class FollowGroup (
    var userId: UUID,
    var name: String,
    var follows: MutableList<Fan>,
) {
    constructor() : this(USER_ID_DEFAULT, NAME_DEFAULT, FOLLOWS_DEFAULT)

    companion object {
        val USER_ID_DEFAULT = DefaultValue().DEFAULT_UUID
        const val NAME_DEFAULT = ""
        val FOLLOWS_DEFAULT = mutableListOf<Fan>()
    }
}
