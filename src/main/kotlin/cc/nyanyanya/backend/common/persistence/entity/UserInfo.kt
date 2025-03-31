package cc.nyanyanya.backend.common.persistence.entity

import cc.nyanyanya.backend.common.persistence.model.Fan

data class UserInfo(
    var id: Long,
    var username: String,
    var name: String,
    var level: Short,
    var gender: String,
    var mail: String,
    var avatarPath: String,
    var birthday: String,
    var FollowList: List<FollowList>,
    var FanList: List<Fan>,
)