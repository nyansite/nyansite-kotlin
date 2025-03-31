package cc.nyanyanya.backend.common.persistence.entity

import java.util.*

data class FollowList (
    var userId: UUID,
    var groups: MutableList<FollowGroup>,
)
