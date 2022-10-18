package com.muhammhassan.storyapp.view.map

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.muhammhassan.storyapp.data.usecase.MapUseCase
import com.muhammhassan.storyapp.utils.DummyData.listResponse
import com.muhammhassan.storyapp.utils.DummyData.userLoginResponse
import com.muhammhassan.storyapp.utils.LiveDataUtil.getValue
import com.muhammhassan.storyapp.utils.LiveDataUtil.value
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
internal class MapViewModelTest {

    private lateinit var viewModel: MapViewModel
    private val dispatcher = UnconfinedTestDispatcher()

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var useCase: MapUseCase

    @Before
    fun before() {
        viewModel = MapViewModel(useCase)
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun after() {
        Dispatchers.resetMain()
    }

    @Test
    fun getData() {
        `when`(useCase.getStoryWithLocation()).thenReturn(flowOf(listResponse))

        viewModel.getStoriesList()
        verify(useCase, times(1)).getStoryWithLocation()
        val data = getValue(viewModel.data)
        assertNotNull(data)
        assertEquals(Status.SUCCESS, data.status)
        assertEquals(listResponse.data?.size, data.data?.size)
    }

    @Test
    fun setStatus() {
        viewModel.setStatus(userLoginResponse.token)

        val data = viewModel.status.value()
        assertEquals(userLoginResponse.token, data)
    }
}