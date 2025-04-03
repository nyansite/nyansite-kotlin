package cc.nyanyanya.backend.common.persistence.repository

import cc.nyanyanya.backend.common.persistence.mapper.LevelMapper
import cc.nyanyanya.backend.common.persistence.model.LevelModel
import com.github.yulichang.extension.kt.toolkit.KtWrappers
import org.springframework.stereotype.Repository

@Repository
class LevelRepo(
    private val levelMapper: LevelMapper
) {
    fun getAllLevels(): List<LevelModel> {
        val queryWrapper = KtWrappers.query(LevelModel::class.java)
            .selectAll(LevelModel::class.java)
        val followListRaw = levelMapper.selectList(queryWrapper)
        val followList = mutableListOf<LevelModel>()
        followListRaw.forEach() { it ->
            followList.add(it ?: LevelModel())
        }
        return followList
    }
}