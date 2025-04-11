package cc.nyanyanya.backend.common.persistence.repository

import cc.nyanyanya.backend.common.persistence.mapper.FanGroupMapper
import cc.nyanyanya.backend.common.persistence.model.FanGroupModel
import cc.nyanyanya.backend.common.util.bo.DefaultValue
import com.baomidou.mybatisplus.extension.kotlin.KtQueryWrapper
import com.github.yulichang.extension.kt.toolkit.KtWrappers
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class FanGroupRepo(private val fanGroupMapper: FanGroupMapper) {
    fun insert(fanGroup: FanGroupModel) : Int {
        val result = fanGroupMapper.insert(fanGroup)
        return result
    }

    fun delete(fanGroup: FanGroupModel): Int {
        val result = fanGroupMapper.deleteById(fanGroup.id)
        return result
    }

    fun selectById(id: UUID): FanGroupModel {
        val queryWrapper = KtQueryWrapper(FanGroupModel::class.java)
            .eq(FanGroupModel::id, id)

        val fanGroupModel =  fanGroupMapper.selectOne(queryWrapper) ?: FanGroupModel()
        return fanGroupModel
    }

    fun selectByNameAndUserId(name: String, userId: UUID): FanGroupModel {
        val queryWrapper = KtQueryWrapper(FanGroupModel::class.java)
            .and({
                it.eq(FanGroupModel::name, name).eq(FanGroupModel::userId, userId)
            })

        val fanGroupModel =  fanGroupMapper.selectOne(queryWrapper) ?: FanGroupModel()
        return fanGroupModel
    }

    fun selectByNumberAndUserId(number: Short, userId: UUID): FanGroupModel {
        val queryWrapper = KtQueryWrapper(FanGroupModel::class.java)
            .and({
                it.eq(FanGroupModel::number, number).eq(FanGroupModel::userId, userId)
            })

        val fanGroupModel =  fanGroupMapper.selectOne(queryWrapper) ?: FanGroupModel()
        return fanGroupModel
    }

    fun getMaxNumberByUserId(userId: UUID): Short {
        val queryWrapper = KtWrappers.query(FanGroupModel::class.java)
            .selectMax(FanGroupModel::number)
            .eq(FanGroupModel::userId, userId)

        val groupNumberMax = queryWrapper.one()?.number ?: DefaultValue.DEFAULT_SHORT

        return groupNumberMax
    }
}