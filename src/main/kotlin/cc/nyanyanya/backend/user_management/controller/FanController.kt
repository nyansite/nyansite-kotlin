package cc.nyanyanya.backend.user_management.controller


import cc.nyanyanya.backend.user_management.entity.FollowGroup
import cc.nyanyanya.backend.user_management.entity.FollowList
import cc.nyanyanya.backend.user_management.service.FanService
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
