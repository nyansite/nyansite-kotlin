package cc.nyanyanya.backend.common.persistence.repository

import cc.nyanyanya.backend.common.persistence.mapper.FanMapper
import cc.nyanyanya.backend.common.persistence.model.FanModel
import cc.nyanyanya.backend.common.util.UuidGenerator
import com.baomidou.mybatisplus.extension.kotlin.KtQueryWrapper
import com.github.yulichang.extension.kt.toolkit.KtWrappers
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class FanRepo(private val fanMapper: FanMapper) {
    fun insertByFanId(
        fanId: UUID,
        followUserId: UUID,
        groupId: UUID
    ): Int {
        val fanModel = FanModel(
            id = UuidGenerator.genUuidV7(),
            fanId = fanId,
            userId = followUserId,
            groupId = groupId
        )
        val result = fanMapper.insert(fanModel)
        return result
    }

    fun deleteById(id: UUID) {
        fanMapper.deleteById(id)
    }

    fun deleteByFanIdAndGroupId(fanId: UUID, groupId: UUID): Int {
        val queryWrapper = KtQueryWrapper(FanModel::class.java)
            .eq(FanModel::fanId, fanId)
            .eq(FanModel::groupId, groupId)
        return fanMapper.delete(queryWrapper)
    }

    fun deleteByUserId(fanID: UUID, userId: UUID): Int {
        val queryWrapper = KtQueryWrapper(FanModel::class.java)
            .eq(FanModel::fanId, fanID)
            .eq(FanModel::userId, userId)
        return fanMapper.delete(queryWrapper)
    }

    fun getAllFans(id: UUID): List<FanModel> {
        val queryWrapper = KtWrappers.query(FanModel::class.java)
            .selectAll(FanModel::class.java)
            .eq(FanModel::userId, id)
        val followListRaw = fanMapper.selectList(queryWrapper)
        val followList = mutableListOf<FanModel>()
        followListRaw.forEach() { it ->
            followList.add(it ?: FanModel())
        }
        return followList
    }
}