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
        val dbHomepagePrivacySetting = homepagePrivacySettingRepo.selectByUserId(userId)
        if (isDisplayPhone != "") {dbHomepagePrivacySetting.isDisplayPhone = isDisplayPhone.toBoolean()}
        if (isDisplayMail != "") {dbHomepagePrivacySetting.isDisplayMail = isDisplayMail.toBoolean()}
        if (isDisplayBirthday != "") {dbHomepagePrivacySetting.isDisplayBirthday = isDisplayBirthday.toBoolean()}
        if (isDisplayFollowsAndFans != "") {
            dbHomepagePrivacySetting.isDisplayFollowsAndFans = isDisplayFollowsAndFans.toBoolean()
        }

        val result = homepagePrivacySettingRepo.update(dbHomepagePrivacySetting)
        return if (result > 0) true else false
    }
}