package cc.nyanyanya.backend.common.persistence.repository

import cc.nyanyanya.backend.user_management.entity.FollowGroup
import cc.nyanyanya.backend.user_management.entity.FollowList
import cc.nyanyanya.backend.common.persistence.mapper.FanMapper
import cc.nyanyanya.backend.common.persistence.model.Fan
import com.baomidou.mybatisplus.extension.kotlin.KtQueryWrapper
import com.github.yulichang.extension.kt.toolkit.KtWrappers
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class FanRepo(val fanMapper: FanMapper) {
    fun deleteById(id: UUID) {
        fanMapper.deleteById(id)
    }

    fun getAllFans(id: UUID): List<Fan> {
        val queryWrapper = KtWrappers.query(Fan::class.java)
            .selectAll(Fan::class.java)
            .eq(Fan::userId, id)
        val followListRaw = fanMapper.selectList(queryWrapper)
        val followList = mutableListOf<Fan>()
        followListRaw.forEach() { it ->
            followList.add(it ?: Fan())
        }
        return followList
    }
}