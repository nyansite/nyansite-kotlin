package cc.nyanyanya.backend.common.persistence.repository

import cc.nyanyanya.backend.common.persistence.mapper.FanMapper
import cc.nyanyanya.backend.common.persistence.mapper.LevelMapper
import cc.nyanyanya.backend.common.persistence.model.Level
import com.github.yulichang.extension.kt.toolkit.KtWrappers
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class LevelRepo(
    private val levelMapper: LevelMapper
) {
    fun getAllLevels(): List<Level> {
        val queryWrapper = KtWrappers.query(Level::class.java)
            .selectAll(Level::class.java)
        val followListRaw = levelMapper.selectList(queryWrapper)
        val followList = mutableListOf<Level>()
        followListRaw.forEach() { it ->
            followList.add(it ?: Level())
        }
        return followList
    }
}