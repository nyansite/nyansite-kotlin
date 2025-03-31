package cc.nyanyanya.backend.common.persistence.entity

import cc.nyanyanya.backend.common.persistence.model.Fan
import java.util.*

data class FollowGroup (
    var userId: UUID,
    var groupId: Short,
    var follows: MutableList<Fan>,
)
