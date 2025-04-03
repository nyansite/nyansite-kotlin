package cc.nyanyanya.backend.user_management.service

import cc.nyanyanya.backend.common.persistence.model.UserModel
import cc.nyanyanya.backend.common.persistence.repository.FileRepo
import cc.nyanyanya.backend.common.persistence.repository.LevelRepo
import cc.nyanyanya.backend.common.persistence.repository.UserRepo
import org.apache.commons.io.FilenameUtils
import org.springframework.stereotype.Service
import java.util.*

@Service
class UserService(
    private val userRepo: UserRepo,
    private val fileRepo: FileRepo,
    private val repo: FileRepo,
    private val levelRepo: LevelRepo
) {
    fun fetchUserById(id: UUID): UserModel {
        return userRepo.selectById(id)
    }

    fun fetchUserByUsername(username: String): UserModel {
        return userRepo.selectByUsername(username)
    }

    fun fetchUserByEmail(email: String): UserModel {
        return userRepo.selectByEmail(email)
    }

    fun fetchUserByPhone(phone: String): UserModel {
        return userRepo.selectByPhone(phone)
    }

    fun login(userModel: UserModel, password: String): Int {
        val dbUserModel: UserModel = userRepo.selectByUsername(userModel.username)

        if (password != dbUserModel.password) {
            return 1
        }
        return 0
    }

    fun verifyUsernameFormat(username: String): Boolean {
        return userRepo.verifyUsernameFormat(username)
    }

    fun verifyEmailFormat(email: String): Boolean {
        return userRepo.verifyEmailFormat(email)
    }

    fun verifyPhoneFormat(phone: String): Boolean {
        return userRepo.verifyPhoneFormat(phone)
    }

    fun verifyPasswordFormat(password: String): Boolean {
        return userRepo.verifyPasswordFormat(password)
    }

    fun readAvatar(avatorPath: String): String {
        val avatorBase64String = fileRepo.readFileToBase64String(avatorPath)
        val fileExtension = FilenameUtils.getExtension(avatorPath)
        val encodeString = "data:image/${fileExtension};base64,${avatorBase64String}"

        return encodeString
    }
}