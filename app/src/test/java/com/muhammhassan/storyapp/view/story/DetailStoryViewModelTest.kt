package com.muhammhassan.storyapp.view.story

import android.app.Application
import android.location.Location
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.muhammhassan.storyapp.data.usecase.DetailStoryUseCase
import com.muhammhassan.storyapp.utils.DummyData.desc
import com.muhammhassan.storyapp.utils.DummyData.image
import com.muhammhassan.storyapp.utils.DummyData.latitude
import com.muhammhassan.storyapp.utils.DummyData.longitude
import com.muhammhassan.storyapp.utils.LiveDataUtil.getValue
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
class DetailStoryViewModelTest {
    private lateinit var viewModel: DetailStoryViewModel
    private val dispatcher = UnconfinedTestDispatcher()

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var useCase: DetailStoryUseCase

    @Mock
    private lateinit var app: Application

    @Before
    fun before() {
        viewModel = DetailStoryViewModel(useCase, app)
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun after() {
        Dispatchers.resetMain()
    }

    @Test
    fun testSave() {
        val location = mock(Location::class.java)
        location.latitude = latitude
        location.longitude = longitude
        `when`(
            useCase.save(
                image,
                desc,
                location.latitude,
                location.longitude
            )
        ).thenReturn(flowOf(ApiResponse.success(Any())))


        viewModel.image = image
        viewModel.setText(desc)
        viewModel.setLocation(location)
        viewModel.save()
        verify(useCase, times(1)).save(image, desc, location.latitude, location.longitude)

        val response = getValue(viewModel.data)
        assertNotNull(response)
        assertEquals(Status.SUCCESS, response.status)
    }
}