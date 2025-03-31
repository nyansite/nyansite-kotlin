package cc.nyanyanya.backend.common.persistence.repository

import cc.nyanyanya.backend.common.persistence.entity.FollowGroup
import cc.nyanyanya.backend.common.persistence.entity.FollowList
import cc.nyanyanya.backend.common.persistence.mapper.FanMapper
import cc.nyanyanya.backend.common.persistence.model.Fan
import com.github.yulichang.extension.kt.KtLambdaWrapper
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class FanRepo(val fanMapper: FanMapper) {
    fun deleteById(id: UUID) {
        fanMapper.deleteById(id)
    }

    fun getAllFans(id: Long): List<Fan> {
        val queryWrapper = KtLambdaWrapper<Fan>()
            .eq(Fan::userId, id)
        val followListRaw = fanMapper.selectList(queryWrapper)
        val followList = mutableListOf<Fan>()
        followListRaw.forEach() { it ->
            followList.add(it ?: Fan())
        }
        return followList
    }

//    fun getAllFollows(id: UUID): FollowList {
//        val queryWrapper = KtLambdaWrapper<Fan>()
//            .selectMax(Fan::groupNumber)
//            .eq(Fan::fanId, id)
//        val followGroupMax =  fanMapper.selectOne(queryWrapper)?.groupNumber ?: DEFAULT_SHORT
//
//        if (followGroupMax == DEFAULT_SHORT) {
//            return FollowList(id, mutableListOf())
//        }
//
//        val followList = FollowList(id, mutableListOf())
//        (0..followGroupMax).forEach { it ->
//            followList.groups.add(getFollowGroupByGroup(id, it.toShort()))
//        }
//        return followList
//    }
//
//    fun getFollowGroupByGroup(id: UUID, group: Short): FollowGroup {
//        val queryWrapper = KtLambdaWrapper(Fan::class.java)
//            .
//            .and({
//                it.eq(Fan::fanId, id).eq(Fan::groupId, group)
//            })
//
//        val followList = mutableListOf<Fan>()
//        fanMapper.selectList(queryWrapper).forEach() { it ->
//            followList.add(it ?: Fan())
//        }
//
//        return FollowGroup(id, group, followList)
//    }
}