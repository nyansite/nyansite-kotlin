package cc.nyanyanya.backend.common.persistence.repository

import cc.nyanyanya.backend.common.persistence.mapper.GenderMapper
import cc.nyanyanya.backend.common.persistence.model.GenderModel
import com.baomidou.mybatisplus.extension.kotlin.KtQueryWrapper
import org.springframework.stereotype.Repository

@Repository
class GenderRepo(private val genderMapper: GenderMapper) {
    fun selectByAlias(alias: String): GenderModel {
        val queryWrapper = KtQueryWrapper(GenderModel::class.java)
            .eq(GenderModel::alias, alias)

        val genderModel = genderMapper.selectOne(queryWrapper) ?: GenderModel()
        return genderModel
    }

    fun selectById(id: Short): GenderModel {
        val queryWrapper = KtQueryWrapper(GenderModel::class.java)
            .eq(GenderModel::id, id)

        val genderModel = genderMapper.selectOne(queryWrapper) ?: GenderModel()
        return genderModel
    }
}