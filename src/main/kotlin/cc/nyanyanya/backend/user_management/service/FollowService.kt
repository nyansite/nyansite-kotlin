package cc.nyanyanya.backend.user_management.service

import cc.nyanyanya.backend.user_management.entity.FollowList
import cc.nyanyanya.backend.user_management.repository.FollowRepo
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class FollowService(followRepo: FollowRepo) {
    var followRepo: FollowRepo = followRepo

    fun fetchAllFollows(id: UUID): FollowList {
        return followRepo.getAllFollows(id)
    }
}