package cc.nyanyanya.backend.common.persistence.repository

import cc.nyanyanya.backend.common.persistence.mapper.UserMapper
import cc.nyanyanya.backend.common.persistence.model.UserModel
import com.baomidou.mybatisplus.extension.kotlin.KtQueryWrapper
import com.baomidou.mybatisplus.extension.kotlin.KtUpdateWrapper
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class UserRepo(private val userMapper: UserMapper) {
    fun insert(user: UserModel): Int {
        val result = userMapper.insert(user)
        return result
    }

    fun delete(user: UserModel): Int {
        val result = userMapper.deleteById(user.id)
        return result
    }

    fun update(
        user: UserModel,
    ): Int {
        val queryWrapper = KtUpdateWrapper(UserModel::class.java)
            .eq(UserModel::id, user.id)
        val result = userMapper.update(user, queryWrapper)
        return result
    }

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

    fun selectByNickname(name: String): UserModel {
        val queryWrapper = KtQueryWrapper(UserModel::class.java)
            .eq(UserModel::nickName, name)
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
}