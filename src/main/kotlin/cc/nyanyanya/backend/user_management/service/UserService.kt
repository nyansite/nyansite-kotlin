package cc.nyanyanya.backend.user_management.service

import cc.nyanyanya.backend.common.persistence.model.HomepagePrivacySettingModel
import cc.nyanyanya.backend.common.persistence.model.UserModel
import cc.nyanyanya.backend.common.persistence.repository.*
import cc.nyanyanya.backend.common.util.UuidGenerator
import kotlinx.coroutines.*
import org.apache.commons.io.FilenameUtils
import org.springframework.stereotype.Service
import java.util.*

@Service
class UserService(
    private val userRepo: UserRepo,
    private val genderRepo: GenderRepo,
    private val fanGroupService: FanGroupService,
    private val fanService: FanService,
    private val homepagePrivacySettingRepo: HomepagePrivacySettingRepo
) {
    fun createUser(
        user: UserModel,
    ): Boolean {
        runBlocking {
            val avatar = async { user.genRandomAvatar() }

            user.id = UuidGenerator.genUuidV7()
            do {
                user.genRandomNickname()
                val dbUser = userRepo.selectByNickname(user.nickName)
                val hasSensitiveWords = UserModel.haveSensitiveWords(user.nickName)
            } while (dbUser != UserModel() && !hasSensitiveWords)
            user.genderId = 0
            user.levelId = 1
            saveAvatar(user, avatar.await())
        }
        user.genSignupTime()

        val resultInsertUser = userRepo.insert(user)
        val resultCreateFanGroups = fanGroupService.createInitiallyFanGroups(user.id)
        val homepagePrivacySetting = HomepagePrivacySettingModel()
        homepagePrivacySetting.id = UuidGenerator.genUuidV7()
        homepagePrivacySetting.userId = user.id
        val resultInsertHomepagePrivacySetting = homepagePrivacySettingRepo.insert(homepagePrivacySetting)

        val success = resultCreateFanGroups == true && resultInsertUser > 0 &&
                resultInsertHomepagePrivacySetting > 0
        return success
    }

    fun createUserWithUsername(
        username: String,
        password: String
    ): Boolean {
        val user = UserModel()
        user.username = username
        user.password = password

        val result = this.createUser(user)
        return result
    }

    fun createUserWithEmail(
        email: String,
        password: String
    ): Boolean {
        val user = UserModel()
        user.email = email
        user.password = password
        do {
            user.genRandomUsername()
            val dbUser = userRepo.selectByUsername(user.username)
            val hasSensitiveWords = UserModel.haveSensitiveWords(user.username)
        } while (dbUser != UserModel() && !hasSensitiveWords)

        val result = this.createUser(user)
        return result
    }

    fun removeUser(
        user: UserModel,
    ): Boolean {
        FileRepo.deleteFile(user.avatarPath)
        val resultRemoveFanGroup = fanGroupService.removeAllFanGroup(user.id)
        val resultFan = fanService.removeAllFans(user.id)
        val homepagePrivacySetting = homepagePrivacySettingRepo.selectByUserId(user.id)
        val resultDeleteHomepagePrivacySetting = homepagePrivacySettingRepo.deleteById(homepagePrivacySetting.id)
        val resultDeleteUser = userRepo.delete(user)

        val success = resultRemoveFanGroup == true && resultFan == true &&
                resultDeleteHomepagePrivacySetting > 0 && resultDeleteUser > 0
        return success
    }

    fun modifyUser(
        user: UserModel,
    ): Boolean {
        val result = userRepo.update(user)
        return if (result > 0) true else false
    }

    fun fetchUserById(id: UUID): UserModel {
        return userRepo.selectById(id)
    }

    fun fetchUserByUsername(username: String): UserModel {
        return userRepo.selectByUsername(username)
    }

    fun fetchUserByNickname(name: String): UserModel {
        return userRepo.selectByNickname(name)
    }

    fun fetchUserByEmail(email: String): UserModel {
        return userRepo.selectByEmail(email)
    }

    fun fetchUserByPhone(phone: String): UserModel {
        return userRepo.selectByPhone(phone)
    }

    fun login(user: UserModel, password: String): Int {
        val dbUser: UserModel = userRepo.selectByUsername(user.username)

        if (password != dbUser.password) {
            return 1
        }
        return 0
    }

    fun saveAvatar(
        user: UserModel,
        avatarEncodeString: String,
    ) {
        val avatarFileExtensionList = "jpg|gif|jpeg|png|gif|bmp|webp|svg|tiff|ico|jfif|tif"
        val regex = Regex("(?<=data:image/(${avatarFileExtensionList});base64,)[A-Za-z0-9+/=]+")
        val avatarBase64String = regex.find(avatarEncodeString)?.value ?: ""

        if (avatarBase64String == "") return
        val filePath = user.genAvatarFilePath(avatarEncodeString)
        if (filePath == "") return
        FileRepo.writeFileToBase64String(filePath, avatarBase64String)
    }

    fun loadAvatar(avatarPath: String): String {
        val avatarBase64String = FileRepo.readFileToBase64String(avatarPath)
        val fileExtension = FilenameUtils.getExtension(avatarPath)
        val encodeString = "data:image/${fileExtension};base64,${avatarBase64String}"

        return encodeString
    }

    fun setUserGenderId(
        user: UserModel,
        genderAlias: String
    ) {
        val dbGender = genderRepo.selectByAlias(genderAlias)
        user.genderId = dbGender.id ?: 0
    }
}