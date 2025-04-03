package cc.nyanyanya.backend.common.persistence.repository

import cc.nyanyanya.backend.common.persistence.mapper.FanMapper
import cc.nyanyanya.backend.common.persistence.model.FanModel
import com.baomidou.mybatisplus.extension.kotlin.KtQueryWrapper
import com.github.yulichang.extension.kt.toolkit.KtWrappers
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class FanRepo(private val fanMapper: FanMapper) {
    fun deleteById(id: UUID) {
        fanMapper.deleteById(id)
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