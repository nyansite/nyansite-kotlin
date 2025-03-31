package cc.nyanyanya.backend.user_management.controller


import cc.nyanyanya.backend.common.persistence.entity.FollowGroup
import cc.nyanyanya.backend.common.persistence.entity.FollowList
import cc.nyanyanya.backend.common.persistence.model.User
import cc.nyanyanya.backend.user_management.service.FanService
import cc.nyanyanya.backend.user_management.service.UserService
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/user")
class FanController(val fanService: FanService) {
    @GetMapping("/selectFansAll")
    fun selectAll(
        @RequestParam(defaultValue = "-1") id: String
    ): FollowList {
//        val list = fanService.fetchAllFollows(UUID.fromString(id))
        val list = FollowList(UUID.fromString(id), arrayListOf<FollowGroup>())
        return list
    }
}
