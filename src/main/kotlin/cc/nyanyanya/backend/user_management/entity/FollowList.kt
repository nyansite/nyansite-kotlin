package cc.nyanyanya.backend.user_management.entity

import cc.nyanyanya.backend.common.util.bo.DefaultValue
import java.util.*

data class FollowList (
    var userId: UUID,
    var groups: MutableList<FollowGroup>,
) {
    constructor() : this(USER_ID_DEFAULT, GROUPS_DEFAULT)

    companion object {
        val USER_ID_DEFAULT = DefaultValue().DEFAULT_UUID
        val GROUPS_DEFAULT = mutableListOf<FollowGroup>()
    }
}
