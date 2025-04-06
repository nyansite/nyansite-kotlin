package cc.nyanyanya.backend.user_management.service

import cc.nyanyanya.backend.common.persistence.model.FanModel
import cc.nyanyanya.backend.common.persistence.repository.FanGroupRepo
import cc.nyanyanya.backend.common.persistence.repository.FanRepo
import cc.nyanyanya.backend.common.persistence.repository.UserRepo
import org.springframework.stereotype.Service
import java.util.*

@Service
class FanService(
    private val fanRepo: FanRepo,
    private val userRepo: UserRepo,
    private val fanGroupRepo: FanGroupRepo,
) {
    fun fetchAllFans(
        id: UUID
    ): List<FanModel> {
        return fanRepo.getAllFans(id)
    }

    fun removeFanByFanUsername(
        fanUsername: String,
        userId: UUID
    ): Boolean {
        val fanUser = userRepo.selectByUsername(fanUsername)
        val deleteNumber = fanRepo.deleteByUserId(fanUser.id, userId)
        var success = false
        if (deleteNumber >= 1) {
            success = true
        }
        return success
    }

    fun addFan(
        fanUsername: String,
        followUserId: UUID,
        groupName: String
    ): Boolean {
        val fanUser = userRepo.selectByUsername(fanUsername)
        val fanGroupModel = fanGroupRepo.selectByNameAndUserId(groupName, fanUser.id)
        val result = fanRepo.insertByFanId(
            fanId = fanUser.id,
            followUserId = followUserId,
            groupId = fanGroupModel.id
        )
        return if (result > 0) true else false
    }
}