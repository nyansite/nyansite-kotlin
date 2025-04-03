package cc.nyanyanya.backend.user_management.service

import cc.nyanyanya.backend.common.persistence.model.User
import cc.nyanyanya.backend.common.persistence.repository.FileRepo
import cc.nyanyanya.backend.common.persistence.repository.LevelRepo
import cc.nyanyanya.backend.common.persistence.repository.UserRepo
import org.apache.commons.io.FilenameUtils
import org.springframework.stereotype.Service

@Service
class UserService(
    userRepo: UserRepo,
    private val fileRepo: FileRepo,
    repo: FileRepo,
    private val levelRepo: LevelRepo
) {
    var userRepo: UserRepo = userRepo

    fun fetchUserByUsername(username: String): User {
        return userRepo.selectByUsername(username)
    }

    fun fetchUserByEmail(email: String): User {
        return userRepo.selectByEmail(email)
    }

    fun fetchUserByPhone(phone: String): User {
        return userRepo.selectByPhone(phone)
    }

    fun login(user: User, password: String): Int {
        val dbUser: User = userRepo.selectByUsername(user.username)

        if (password != dbUser.password) {
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