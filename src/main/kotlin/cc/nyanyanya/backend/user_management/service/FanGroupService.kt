package cc.nyanyanya.backend.user_management.service

import cc.nyanyanya.backend.common.persistence.model.FanGroupModel
import cc.nyanyanya.backend.common.persistence.repository.FanGroupRepo
import cc.nyanyanya.backend.common.persistence.repository.FanRepo
import cc.nyanyanya.backend.common.persistence.repository.UserRepo
import cc.nyanyanya.backend.common.util.UuidGenerator
import cc.nyanyanya.backend.user_management.repository.FollowRepo
import org.springframework.stereotype.Service
import java.util.*

@Service
class FanGroupService(
    private val fanGroupRepo: FanGroupRepo,
    private val userRepo: UserRepo,
    private val fanRepo: FanRepo,
    private val followRepo: FollowRepo
) {
    fun createInitiallyFanGroups(
        userId: UUID,
    ): Boolean {
        val fanGroupSpecial = FanGroupModel(
            id = UuidGenerator.genUuidV7(),
            userId = userId,
            number = 1.toShort(),
            name = "special",
        )
        val resultSpecial = fanGroupRepo.insert(fanGroupSpecial)

        val fanGroupDefault = FanGroupModel(
            id = UuidGenerator.genUuidV7(),
            userId = userId,
            number = 1.toShort(),
            name = "default",
        )
        val resultDefault = fanGroupRepo.insert(fanGroupDefault)

        return if (resultSpecial > 0 && resultDefault > 0) true else false
    }

    fun fetchFanGroupByNameAndUsername(name: String, username: String): FanGroupModel {
        val dbUser = userRepo.selectByUsername(username)
        val fanGroupModel = fanGroupRepo.selectByNameAndUserId(name, dbUser.id)
        return fanGroupModel
    }

    fun addFanGroupByFanUsername(fanUsername: String, groupName: String): Boolean {
        val fanUser = userRepo.selectByUsername(fanUsername)
        val maxNumber = fanGroupRepo.getMaxNumberByUserId(fanUser.id)

        val fanGroupModel = FanGroupModel(
            id = UuidGenerator.genUuidV7(),
            userId = fanUser.id,
            number = (maxNumber + 1).toShort(),
            name = groupName,
        )
        val result = fanGroupRepo.insert(fanGroupModel)
        return if (result > 1) true else false
    }

    fun removeFanGroupByFanGroupName(fanUsername: String, groupName: String): Boolean {
        val fanUser = userRepo.selectByUsername(fanUsername)
        val fanGroup = fanGroupRepo.selectByNameAndUserId(groupName, fanUser.id)
        val resultDeleteFans = fanRepo.deleteByFanIdAndGroupId(fanUser.id, fanGroup.id)
        val resultDeleteFanGroup = fanGroupRepo.delete(fanGroup)
        return if (resultDeleteFans > 0 && resultDeleteFanGroup > 0) true else false
    }

    fun removeAllFanGroup(fanId: UUID): Boolean {
        val allFollows = followRepo.getAllFollows(fanId)
        var resultDeleteFanGroup = 0
        val resultDeleteFans = MutableList(allFollows.groups.size) { 0 }

        allFollows.groups.forEachIndexed() { i, it ->
            it.follows.forEach() { follow ->
                val resultDeleteFan = fanRepo.deleteByFanIdAndGroupId(fanId, follow.groupId)
                if (resultDeleteFan == 1) {
                    resultDeleteFans[i] += 1
                }
            }
            val fanGroupToDelete = fanGroupRepo.selectById(it.follows[0].groupId)
            resultDeleteFanGroup = fanGroupRepo.delete(fanGroupToDelete)
            if (resultDeleteFanGroup == 1) {
                resultDeleteFanGroup += 1
            }
        }

        var success = true
        if (resultDeleteFanGroup != allFollows.groups.size) {
            success = false
        }
        resultDeleteFans.forEachIndexed() { i, it ->
            if (it != allFollows.groups[i].follows.size) {
                success = false
            }
        }

        return success
    }
}