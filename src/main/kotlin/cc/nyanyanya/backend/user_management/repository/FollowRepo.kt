package cc.nyanyanya.backend.user_management.repository

import cc.nyanyanya.backend.user_management.entity.FollowGroup
import cc.nyanyanya.backend.user_management.entity.FollowList
import cc.nyanyanya.backend.common.persistence.mapper.FanMapper
import cc.nyanyanya.backend.common.persistence.model.FanModel
import cc.nyanyanya.backend.common.persistence.repository.FanGroupRepo
import cc.nyanyanya.backend.common.util.bo.DefaultValue
import com.baomidou.mybatisplus.extension.kotlin.KtQueryWrapper
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class FollowRepo(
    private val fanMapper: FanMapper,
    private val fanGroupRepo: FanGroupRepo
) {
    fun getAllFollows(id: UUID): FollowList {
        val followGroupNumberMax = fanGroupRepo.getMaxNumberByUserId(id)

        if (followGroupNumberMax == DefaultValue.DEFAULT_SHORT) {
            return FollowList(id, mutableListOf())
        }

        val followList = FollowList(id, mutableListOf())
        (0..followGroupNumberMax).forEach { it ->
            followList.groups.add(getFollowGroupByGroupNumber(id, it.toShort()))
        }
        return followList
    }

    fun getFollowGroupByGroupNumber(id: UUID, groupNumber: Short): FollowGroup {
        val group = fanGroupRepo.selectByNumberAndUserId(groupNumber, id)
        val queryWrapper = KtQueryWrapper(FanModel::class.java)
            .select(FanModel::userId)
            .and({
                it.eq(FanModel::fanId, id).eq(FanModel::groupId, group.id)
            })

        val followList = mutableListOf<FanModel>()
        val followListRaw = fanMapper.selectList(queryWrapper)
        followListRaw.forEach() { it ->
            followList.add(it ?: FanModel())
        }

        val followGroup = FollowGroup(id, group.name, followList)
        return followGroup
    }
}