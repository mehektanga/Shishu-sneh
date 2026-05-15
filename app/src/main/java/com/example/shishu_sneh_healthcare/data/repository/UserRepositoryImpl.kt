package com.example.shishu_sneh_healthcare.data.repository

import com.example.shishu_sneh_healthcare.data.local.dao.UserDao
import com.example.shishu_sneh_healthcare.data.local.entity.UserEntity
import com.example.shishu_sneh_healthcare.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userDao: UserDao
) : UserRepository {
    override fun getUserById(userId: String): Flow<UserEntity?> =
        userDao.getUserById(userId)

    override suspend fun insertUser(user: UserEntity): Long =
        userDao.insertUser(user)

    override suspend fun updateUser(user: UserEntity): Int =
        userDao.updateUser(user)
}
