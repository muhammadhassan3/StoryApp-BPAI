package com.muhammhassan.storyapp.view.login

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.muhammhassan.storyapp.data.usecase.LoginUseCase
import com.muhammhassan.storyapp.utils.DummyData.userLoginRequest
import com.muhammhassan.storyapp.utils.DummyData.userLoginResponse
import com.muhammhassan.storyapp.utils.api.Status
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
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
internal class LoginViewModelTest {

    private lateinit var viewModel: LoginViewModel
    private val dispatcher = UnconfinedTestDispatcher()

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var useCase: LoginUseCase

    @Mock
    private lateinit var app: Application

    @Before
    fun before() {
        viewModel = LoginViewModel(useCase, app)
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun after() {
        Dispatchers.resetMain()
    }

    @Test
    fun login() = runTest {
        `when`(useCase.login(userLoginRequest)).thenReturn(flow {
            userLoginResponse
        })

        viewModel.login(userLoginRequest.email, userLoginRequest.password)

        verify(useCase, times(1)).login(userLoginRequest)

        val response = viewModel.response
        println(response)

        response.observeForever {
            assertNotNull(it)
            assertEquals(Status.SUCCESS, it.status)
            assertEquals(userLoginResponse.token, it.data?.token)
            assertEquals(userLoginResponse.name, it.data?.name)
            assertEquals(userLoginResponse.userId, it.data?.userId)
        }
    }

    @Test
    fun setToken() {
        viewModel.setToken(userLoginResponse.token)
        verify(useCase, times(1)).saveToken(userLoginResponse.token)
    }
}