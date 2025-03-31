package cc.nyanyanya.backend.user_management.service

import cc.nyanyanya.backend.common.persistence.model.User
import cc.nyanyanya.backend.common.persistence.repository.UserRepo
//import org.example.springdemo.util.CustomException
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.RequestParam

@Service
class UserService(userRepo: UserRepo) {
    var userRepo: UserRepo = userRepo

    fun verifyUserByUsername(username: String): User {
        return userRepo.selectByUsername(username)
    }

    fun verifyUserByEmail(email: String): User {
        return userRepo.selectByEmail(email)
    }

    fun verifyUserByPhone(phone: String): User {
        return userRepo.selectByPhone(phone)
    }

    fun login(user: User): Int {
        val dbUser: User = userRepo.selectByUsername(user.username)

        if (user.password != dbUser.password) {
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
}