package com.muhammhassan.storyapp.data.pagingsource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.muhammhassan.storyapp.data.datasource.RemoteDataSource
import com.muhammhassan.storyapp.data.model.response.StoriesResponseModel
import com.muhammhassan.storyapp.utils.Constant.STARTING_PAGE
import com.muhammhassan.storyapp.utils.NoDataException
import com.muhammhassan.storyapp.utils.api.Status
import okio.IOException

class StoryPagingSource(private val dataSource: RemoteDataSource) :
    PagingSource<Int, StoriesResponseModel>() {
    override fun getRefreshKey(state: PagingState<Int, StoriesResponseModel>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)
                ?.prevKey?.plus(1) ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, StoriesResponseModel> {
        val pageIndex = params.key ?: STARTING_PAGE
        return try {
            val response = dataSource.getAllStories(pageIndex, 10)
            when (response.status) {
                Status.SUCCESS -> {
                    if (response.data != null) {
                        val nextKey = if (response.data.isEmpty()) {
                            null
                        } else {
                            pageIndex + (response.data.size / 10)
                        }
                        LoadResult.Page(
                            data = response.data,
                            prevKey = if (pageIndex == STARTING_PAGE) null else pageIndex,
                            nextKey = nextKey
                        )
                    } else LoadResult.Invalid()
                }
                Status.ERROR -> LoadResult.Error(IllegalStateException(response.message))
                Status.NO_DATA -> LoadResult.Error(NoDataException())
                else -> LoadResult.Invalid()
            }
        } catch (ioException: IOException) {
            LoadResult.Error(ioException)
        } catch (runtimeExcepion: RuntimeException) {
            LoadResult.Error(runtimeExcepion)
        }
    }
}