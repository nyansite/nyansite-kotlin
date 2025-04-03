package cc.nyanyanya.backend.user_management.service

import cc.nyanyanya.backend.common.persistence.model.FanModel
import cc.nyanyanya.backend.common.persistence.repository.FanRepo
import org.springframework.stereotype.Service
import java.util.*

@Service
class FanService(private val fanRepo: FanRepo) {
    fun fetchAllFans(
        id: UUID
    ): List<FanModel> {
        return fanRepo.getAllFans(id)
    }

    fun removeFanByUserId(
        fanId: UUID,
        userId: UUID
    ): Boolean {
        var deleteNumber = fanRepo.deleteByUserId(fanId, userId)
        var success = false
        if (deleteNumber >= 1) {
            success = true
        }
        return success
    }
}