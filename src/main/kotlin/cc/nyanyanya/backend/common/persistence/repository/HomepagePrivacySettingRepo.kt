package cc.nyanyanya.backend.common.persistence.repository

import cc.nyanyanya.backend.common.persistence.mapper.HomepagePrivacySettingMapper
import cc.nyanyanya.backend.common.persistence.model.HomepagePrivacySettingModel
import com.baomidou.mybatisplus.extension.kotlin.KtQueryWrapper
import com.baomidou.mybatisplus.extension.kotlin.KtUpdateWrapper
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class HomepagePrivacySettingRepo(private val homepagePrivacySettingMapper: HomepagePrivacySettingMapper) {
    fun insert(homepagePrivacySetting: HomepagePrivacySettingModel): Int {
        val result = homepagePrivacySettingMapper.insert(homepagePrivacySetting)
        return result
    }

    fun deleteById(id: UUID): Int {
        val result = homepagePrivacySettingMapper.deleteById(id)
        return result
    }

    fun update(
        homepagePrivacySetting: HomepagePrivacySettingModel,
    ): Int {
        val queryWrapper = KtUpdateWrapper(HomepagePrivacySettingModel::class.java)
            .eq(HomepagePrivacySettingModel::id, homepagePrivacySetting.id)
        val result = homepagePrivacySettingMapper.update(homepagePrivacySetting, queryWrapper)
        return result
    }

    fun selectByUserId(
        userId: UUID
    ): HomepagePrivacySettingModel {
        val queryWrapper = KtQueryWrapper(HomepagePrivacySettingModel::class.java)
            .eq(HomepagePrivacySettingModel::userId, userId)
        val homepagePrivacySetting = homepagePrivacySettingMapper.selectOne(queryWrapper)
            ?: HomepagePrivacySettingModel()
        return homepagePrivacySetting
    }
}