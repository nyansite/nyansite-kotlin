package cc.nyanyanya.backend.common.persistence.repository

import cc.nyanyanya.backend.common.persistence.mapper.HomepagePrivacySettingMapper
import cc.nyanyanya.backend.common.persistence.model.HomepagePrivacySettingModel
import com.baomidou.mybatisplus.extension.kotlin.KtQueryWrapper
import com.baomidou.mybatisplus.extension.kotlin.KtUpdateWrapper
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class HomepagePrivacySettingRepo(private val homepagePrivacySettingMapper: HomepagePrivacySettingMapper) {
    fun updateByUserId(
        userId: UUID,
        isDisplayPhone: String,
        isDisplayMail: String,
        isDisplayBirthday: String,
        isDisplayFollowsAndFans: String,
    ): Int {
        val dbHomepagePrivacySetting = selectByUserId(userId)
        if (isDisplayPhone != "") {dbHomepagePrivacySetting.isDisplayPhone = isDisplayPhone.toBoolean()}
        if (isDisplayMail != "") {dbHomepagePrivacySetting.isDisplayMail = isDisplayMail.toBoolean()}
        if (isDisplayBirthday != "") {dbHomepagePrivacySetting.isDisplayBirthday = isDisplayBirthday.toBoolean()}
        if (isDisplayFollowsAndFans != "") {
            dbHomepagePrivacySetting.isDisplayFollowsAndFans = isDisplayFollowsAndFans.toBoolean()
        }

        val queryWrapper = KtUpdateWrapper(HomepagePrivacySettingModel::class.java)
            .eq(HomepagePrivacySettingModel::userId, userId)
        val result = homepagePrivacySettingMapper.update(dbHomepagePrivacySetting, queryWrapper)
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