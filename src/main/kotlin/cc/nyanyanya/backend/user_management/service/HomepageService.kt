package cc.nyanyanya.backend.user_management.service

import cc.nyanyanya.backend.common.persistence.model.HomepagePrivacySettingModel
import cc.nyanyanya.backend.common.persistence.repository.HomepagePrivacySettingRepo
import org.springframework.stereotype.Service
import java.util.*

@Service
class HomepageService(
    private val homepagePrivacySettingRepo: HomepagePrivacySettingRepo,
) {
    fun fetchHomepagePrivacySetting(
        userId: UUID
    ): HomepagePrivacySettingModel {
        return homepagePrivacySettingRepo.selectByUserId(userId)
    }

    fun modifyHomepagePrivacySetting(
        userId: UUID,
        isDisplayPhone: String,
        isDisplayMail: String,
        isDisplayBirthday: String,
        isDisplayFollowsAndFans: String,
    ): Boolean {
        val result = homepagePrivacySettingRepo.updateByUserId(
            userId,
            isDisplayPhone,
            isDisplayMail,
            isDisplayBirthday,
            isDisplayFollowsAndFans,
        )
        return if (result > 0) true else false
    }
}