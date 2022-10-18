package com.muhammhassan.storyapp.data.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.asLiveData
import androidx.paging.PagingSource
import com.muhammhassan.storyapp.data.datasource.RemoteDataSource
import com.muhammhassan.storyapp.data.pagingsource.StoryPagingSource
import com.muhammhassan.storyapp.utils.Constant
import com.muhammhassan.storyapp.utils.DummyData.desc
import com.muhammhassan.storyapp.utils.DummyData.image
import com.muhammhassan.storyapp.utils.DummyData.latitude
import com.muhammhassan.storyapp.utils.DummyData.listResponse
import com.muhammhassan.storyapp.utils.DummyData.longitude
import com.muhammhassan.storyapp.utils.DummyData.saveResponse
import com.muhammhassan.storyapp.utils.LiveDataUtil.getValue
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

@RunWith(MockitoJUnitRunner::class)
@OptIn(ExperimentalCoroutinesApi::class)
class StoryRepositoryTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var remote: RemoteDataSource
    private lateinit var repository: FakeStoryRepository

    private val dispatcher = UnconfinedTestDispatcher()

    @Before
    fun before() {
        repository = FakeStoryRepository(remote)
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun after() {
        Dispatchers.resetMain()
    }

    @Test
    fun `save story and return success`() = runTest {
        `when`(remote.saveStory(image, desc, latitude, longitude)).thenReturn(saveResponse)

        val result = repository.saveStory(image, desc, latitude, longitude)

        val data = getValue(result.asLiveData())


        assertNotNull(data)
        assertEquals(Status.SUCCESS, data.status)
    }

    @Test
    fun getPagingStories() = runTest {
        val storiesPagingSource = StoryPagingSource(remote)
        `when`(remote.getAllStories(Constant.STARTING_PAGE, Constant.PAGE_SIZE)).thenReturn(
            listResponse
        )

        val actual = storiesPagingSource.load(
            PagingSource.LoadParams.Refresh(
                loadSize = 1,
                placeholdersEnabled = false,
                key = null
            )
        )

        val expected = PagingSource.LoadResult.Page(
            data = listResponse.data!!,
            prevKey = null,
            nextKey = 2
        )

        verify(remote, times(1)).getAllStories(Constant.STARTING_PAGE, Constant.PAGE_SIZE)

        assertEquals(expected, actual)
    }

    @Test
    fun `get Stories With Location and return success`() = runTest {
        `when`(remote.getStoriesWithLocation()).thenReturn(listResponse)

        val response = repository.getStoriesWithLocation()
        val data = getValue(response.asLiveData())

        assertNotNull(data.data)
        assertEquals(data.status, Status.SUCCESS)
        assertEquals(listResponse.data?.size, data.data?.size)
    }

}