package cc.nyanyanya.backend.common.persistence.repository

import cc.nyanyanya.backend.common.persistence.mapper.FanGroupMapper
import cc.nyanyanya.backend.user_management.entity.FollowGroup
import cc.nyanyanya.backend.user_management.entity.FollowList
import cc.nyanyanya.backend.common.persistence.mapper.FanMapper
import cc.nyanyanya.backend.common.persistence.model.Fan
import cc.nyanyanya.backend.common.persistence.model.FanGroup
import cc.nyanyanya.backend.common.util.bo.DefaultValue
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
import com.baomidou.mybatisplus.extension.kotlin.KtQueryWrapper
import com.github.yulichang.extension.kt.KtLambdaWrapper
import com.github.yulichang.extension.kt.toolkit.KtWrappers
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class FanGroupRepo(val fanGroupMapper: FanGroupMapper) {
    fun selectById(id: UUID): FanGroup {
        val queryWrapper = KtQueryWrapper(FanGroup::class.java)
            .eq(FanGroup::id, id)

        val fanGroup =  fanGroupMapper.selectOne(queryWrapper) ?: FanGroup()
        return fanGroup
    }

    fun selectByNumberAndUserId(number: Short, userId: UUID): FanGroup {
        val queryWrapper = KtQueryWrapper(FanGroup::class.java)
            .and({
                it.eq(FanGroup::number, number).eq(FanGroup::userId, userId)
            })

        val fanGroup =  fanGroupMapper.selectOne(queryWrapper) ?: FanGroup()
        return fanGroup
    }

    fun getMaxNumberByUserId(userId: UUID): Short {
//        val queryWrapper = QueryWrapper(FanGroup::class.java)
//            .select("max(number_) as number")
//            .eq("user_id_", userId)

        val queryWrapper = KtWrappers.query(FanGroup::class.java)
            .selectMax(FanGroup::number)
            .eq(FanGroup::userId, userId)

        val groupNumberMax = queryWrapper.one()?.number ?: DefaultValue.DEFAULT_SHORT

        return groupNumberMax
    }
}