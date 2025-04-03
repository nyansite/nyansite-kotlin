package cc.nyanyanya.backend.user_management.service

import cc.nyanyanya.backend.common.persistence.model.Fan
import cc.nyanyanya.backend.common.persistence.repository.FanRepo
import org.springframework.stereotype.Service
import java.util.*

@Service
class FanService(val fanRepo: FanRepo) {
    fun fetchAllFans(
        id: UUID
    ): List<Fan> {
        return fanRepo.getAllFans(id)
    }
}