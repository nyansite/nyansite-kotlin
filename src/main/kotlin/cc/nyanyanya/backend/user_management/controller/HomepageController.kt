package cc.nyanyanya.backend.user_management.controller


import cc.nyanyanya.backend.common.persistence.model.UserModel
import cc.nyanyanya.backend.common.util.bo.DefaultValue
import cc.nyanyanya.backend.common.util.bo.ResultErrorCode
import cc.nyanyanya.backend.user_management.service.HomepageService
import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.servlet.http.HttpSession
import org.springframework.aot.hint.annotation.RegisterReflectionForBinding
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.*
import java.util.*


@RestController
@RequestMapping("/user")
class HomepageController(
    private val homepageService: HomepageService,
) {
    @RegisterReflectionForBinding
    @Component
    @Scope("prototype")
    data class HomepagePrivacySettingsInfo(
        @JsonProperty("is_display_mail") var isDisplayMail: Boolean = false,
        var is_display_birthday: Boolean = false,
        var is_display_fans: Boolean = false,
        var error: Byte = DefaultValue.DEFAULT_BYTE
    )

    @PostMapping("/get-user-homepage-privacy-settings")
    fun getUserHomepagePrivacySettings(
        session: HttpSession,
    ): Any {
        val homepagePrivacySettingsInfo = HomepagePrivacySettingsInfo()

        val isLogin = session.getAttribute("isLogin") as? Boolean ?: false
        if (!isLogin) {
            homepagePrivacySettingsInfo.error = 1.toByte()

            return homepagePrivacySettingsInfo
        }

        val sessionId = session.getAttribute("id") as? UUID ?: UserModel.ID_DEFAULT
        val dbHomepagePrivacySetting = homepageService.fetchHomepagePrivacySetting(sessionId)

        homepagePrivacySettingsInfo.isDisplayMail = dbHomepagePrivacySetting.isDisplayMail
        homepagePrivacySettingsInfo.is_display_birthday = dbHomepagePrivacySetting.isDisplayBirthday
        homepagePrivacySettingsInfo.is_display_fans = dbHomepagePrivacySetting.isDisplayFollowsAndFans
        homepagePrivacySettingsInfo.error = 0.toByte()
        return homepagePrivacySettingsInfo
    }

    @PostMapping("/set-user-homepage-privacy-settings")
    fun setUserHomepagePrivacySettings(
        @RequestParam(name = "is_display_mail") isDisplayMail: String,
        @RequestParam(name = "is_display_birthday") isDisplayBirthday: String,
        @RequestParam(name = "is_display_fans") isDisplayFans: String,
        session: HttpSession,
    ): Any {
        if (isDisplayMail == "" && isDisplayBirthday == "" && isDisplayFans == "") {
            return ResultErrorCode(2)
        }

        val isLogin = session.getAttribute("isLogin") as? Boolean ?: false
        if (!isLogin) {
            return ResultErrorCode(1)
        }

        val sessionId = session.getAttribute("id") as? UUID ?: UserModel.ID_DEFAULT
        homepageService.modifyHomepagePrivacySetting(
            sessionId,
            "",
            isDisplayMail,
            isDisplayBirthday,
            isDisplayFans,
        )
        return ResultErrorCode(0)
    }
}
