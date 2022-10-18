package com.muhammhassan.storyapp.data.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.asLiveData
import com.muhammhassan.storyapp.data.datasource.LocalDataSource
import com.muhammhassan.storyapp.data.datasource.RemoteDataSource
import com.muhammhassan.storyapp.utils.DummyData.userLoginRequest
import com.muhammhassan.storyapp.utils.DummyData.userLoginResponse
import com.muhammhassan.storyapp.utils.DummyData.userRegisterRequest
import com.muhammhassan.storyapp.utils.LiveDataUtil.getValue
import com.muhammhassan.storyapp.utils.api.ApiResponse
import com.muhammhassan.storyapp.utils.api.Status
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class UserRepositoryTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var remote: RemoteDataSource

    @Mock
    private lateinit var local: LocalDataSource
    private lateinit var repository: FakeUserRepository

    private val dispatcher = UnconfinedTestDispatcher()

    @Before
    fun before() {
        repository = FakeUserRepository(remote, local)
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun after() {
        Dispatchers.resetMain()
    }

    @Test
    fun `user login and return success response`() = runTest {
        `when`(remote.login(userLoginRequest)).thenReturn(ApiResponse.success(userLoginResponse))

        val response = repository.login(userLoginRequest)
        val data = getValue(response.asLiveData())

        assertNotNull(data)
        assertEquals(Status.SUCCESS, data.status)
        assertEquals(userLoginResponse.userId, data.data?.userId)
        assertEquals(userLoginResponse.name, data.data?.name)
        assertEquals(userLoginResponse.token, data.data?.token)
    }

    @Test
    fun `user register and return success response`() = runTest {
        `when`(remote.register(userRegisterRequest)).thenReturn(ApiResponse.success(Any()))

        val response = repository.register(userRegisterRequest)
        val data = getValue(response.asLiveData())

        assertNotNull(data)
        assertEquals(Status.SUCCESS, data.status)
    }


    @Test
    fun `save token`() = runTest {
        repository.saveToken(userLoginResponse.token)
        verify(local, times(1)).saveToken(userLoginResponse.token)
    }

    @Test
    fun `remove token`() = runTest {
        repository.logout()
        verify(local, times(1)).removeToken()
    }

    @Test
    fun `get token`() = runTest {
        `when`(local.getToken()).thenReturn(userLoginResponse.token)

        val token = repository.getToken()
        verify(local, times(1)).getToken()

        assertNotNull(token)
        assertEquals(userLoginResponse.token, token)
    }
}