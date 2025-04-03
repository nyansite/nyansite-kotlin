package cc.nyanyanya.backend.user_management.service

import cc.nyanyanya.backend.common.persistence.model.FanGroupModel
import cc.nyanyanya.backend.common.persistence.repository.FanGroupRepo
import cc.nyanyanya.backend.common.persistence.repository.FanRepo
import cc.nyanyanya.backend.common.persistence.repository.UserRepo
import org.springframework.stereotype.Service

@Service
class FanGroupService(
    private val fanGroupRepo: FanGroupRepo,
    private val userRepo: UserRepo,
    private val fanRepo: FanRepo
) {
    fun fetchFanGroupByNameAndUsername(name: String, username: String): FanGroupModel {
        val dbUserModel = userRepo.selectByUsername(username)
        val fanGroupModel = fanGroupRepo.selectByNameAndUserId(name, dbUserModel.id)
        return fanGroupModel
    }

    fun addFanGroupByFanUsername(fanUsername: String, groupName: String): Boolean {
        val fanUserModel = userRepo.selectByUsername(fanUsername)
        val result = fanGroupRepo.insertByGroupName(fanUserModel.id, groupName)
        return if (result > 1) true else false
    }

    fun removeFanGroupByFanGroupName(fanUsername: String, groupName: String): Boolean {
        val fanUserModel = userRepo.selectByUsername(fanUsername)
        val fanGroupModel = fanGroupRepo.selectByNameAndUserId(groupName, fanUserModel.id)
        val resultDeleteFans = fanRepo.deleteByFanIdAndGroupId(fanUserModel.id, fanGroupModel.id)
        val resultDeleteFanGroup = fanGroupRepo.deleteById(fanGroupModel.id)
        return if (resultDeleteFans > 0 && resultDeleteFanGroup > 0) true else false
    }
}