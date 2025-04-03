package cc.nyanyanya.backend.common.persistence.repository

import cc.nyanyanya.backend.common.persistence.mapper.UserMapper
import cc.nyanyanya.backend.common.persistence.model.UserModel
import com.baomidou.mybatisplus.extension.kotlin.KtQueryWrapper
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class UserRepo(private val userMapper: UserMapper) {
    fun selectById(id: UUID): UserModel {
        val queryWrapper = KtQueryWrapper(UserModel::class.java)
            .eq(UserModel::id, id)
        return userMapper.selectOne(queryWrapper) ?: UserModel()
    }

    fun selectByUsername(username: String): UserModel {
        val queryWrapper = KtQueryWrapper(UserModel::class.java)
            .eq(UserModel::username, username)
        return userMapper.selectOne(queryWrapper) ?: UserModel()
    }

    fun selectByEmail(email: String): UserModel {
        val queryWrapper = KtQueryWrapper(UserModel::class.java)
            .eq(UserModel::email, email)
        return userMapper.selectOne(queryWrapper) ?: UserModel()
    }

    fun selectByPhone(phone: String): UserModel {
        val queryWrapper = KtQueryWrapper(UserModel::class.java)
            .eq(UserModel::phone, phone)
        return userMapper.selectOne(queryWrapper) ?: UserModel()
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