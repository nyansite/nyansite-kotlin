package cc.nyanyanya.backend.common.persistence.repository

import cc.nyanyanya.backend.common.persistence.mapper.UserMapper
import cc.nyanyanya.backend.common.persistence.model.User
import com.baomidou.mybatisplus.extension.kotlin.KtQueryWrapper
import com.github.yulichang.extension.kt.toolkit.KtWrappers
import org.springframework.stereotype.Repository

@Repository
class UserRepo(userMapper: UserMapper) {
    var userMapper = userMapper

    fun selectByUsername(username: String): User {
        val queryWrapper = KtQueryWrapper(User::class.java)
            .eq(User::username, username)
        return userMapper.selectOne(queryWrapper) ?: User()
    }

    fun selectByEmail(email: String): User {
        val queryWrapper = KtQueryWrapper(User::class.java)
            .eq(User::email, email)
        return userMapper.selectOne(queryWrapper) ?: User()
    }

    fun selectByPhone(phone: String): User {
        val queryWrapper = KtQueryWrapper(User::class.java)
            .eq(User::phone, phone)
        return userMapper.selectOne(queryWrapper) ?: User()
    }

    fun verifyUsernameFormat(username: String): Boolean {
        val regex = Regex("^[A-Za-z0-9_]{6,20}$")
        return regex.matches(username)
    }

    fun verifyEmailFormat(email: String): Boolean {
        val regexConditionOne = Regex("(?=.{1,253}\$)" +
                "(?:[\\p{L}\\p{N}\\-]{0,61}[\\p{L}\\p{N}]\\.)+[\\p{L}\\p{N}]{1,62}\\p{L}")
        val regexConditionTwo = Regex("(?=.{1,254}\$)" +
                "(?:[\\p{L}\\p{N}\\-]{0,61}[\\p{L}\\p{N}]\\.)+[\\p{L}\\p{N}]{1,62}\\p{L}\\.")

        val regex = Regex("^(?=.{6,320}\$)" +
                "[\\p{L}\\p{N}._%+\\-]{1,64}" +
                "@" +
                "(?:" +
                regexConditionOne.pattern +
                "|" +
                regexConditionTwo.pattern +
                ")$")

        return regex.matches(email)
    }

    fun verifyPhoneFormat(phone: String): Boolean {
        val regex = Regex("^[\\d]{12,14}$")
        return regex.matches(phone)
    }

    fun verifyPasswordFormat(password: String): Boolean {
        val regex = Regex("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[A-Za-z0-9_]{6,20}$")
        return regex.matches(password)
    }
}