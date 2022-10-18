package com.muhammhassan.storyapp.view.list

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.PagingData
import com.muhammhassan.storyapp.data.usecase.StoryListUseCase
import com.muhammhassan.storyapp.utils.DummyData.listResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
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
internal class ListStoryViewModelTest {

    private lateinit var viewModel: ListStoryViewModel
    private val dispatcher = UnconfinedTestDispatcher()

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var useCase: StoryListUseCase

    @Before
    fun before() {
        viewModel = ListStoryViewModel(useCase)
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun after() {
        Dispatchers.resetMain()
    }

    @Test
    fun getStoryList() = runTest {
        `when`(useCase.getPagingData()).thenReturn(flow {
            PagingData.from(listResponse.data!!)
        })

        viewModel.getStoryList()
        verify(useCase, times(1)).getPagingData()

        viewModel.getStoryList().collect {
            assertNotNull(it)
        }
    }

    @Test
    fun logout() {
        viewModel.logout()
        verify(useCase, times(1)).logout()
    }
}