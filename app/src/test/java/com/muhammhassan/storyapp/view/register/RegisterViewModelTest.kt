package com.muhammhassan.storyapp.view.register

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.muhammhassan.storyapp.data.usecase.RegisterUseCase
import com.muhammhassan.storyapp.utils.DummyData.userRegisterRequest
import com.muhammhassan.storyapp.utils.api.ApiResponse
import com.muhammhassan.storyapp.utils.api.Status
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
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
class RegisterViewModelTest {
    private lateinit var viewModel: RegisterViewModel
    private val dispatcher = UnconfinedTestDispatcher()

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var useCase: RegisterUseCase

    @Mock
    private lateinit var app: Application

    @Before
    fun before() {
        viewModel = RegisterViewModel(useCase, app)
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun after() {
        Dispatchers.resetMain()
    }

    @Test
    fun testRegister() {
        `when`(useCase.register(userRegisterRequest)).thenReturn(flowOf(ApiResponse.success(Any())))

        viewModel.register(
            userRegisterRequest.name,
            userRegisterRequest.email,
            userRegisterRequest.password
        )
        verify(useCase, times(1)).register(userRegisterRequest)

        viewModel.response.observeForever {
            assertNotNull(it)
            assertEquals(Status.SUCCESS, it.status)
        }
    }
}