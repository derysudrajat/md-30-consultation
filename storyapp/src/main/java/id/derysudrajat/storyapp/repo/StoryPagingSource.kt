package id.derysudrajat.storyapp.repo

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import id.derysudrajat.storyapp.data.model.Story
import id.derysudrajat.storyapp.repo.remote.RemoteDataSource
import id.derysudrajat.storyapp.repo.remote.response.toStories

class StoryPagingSource constructor(
    private val token: String,
    private val remoteDataSource: RemoteDataSource
) : PagingSource<Int, Story>() {

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }

    override fun getRefreshKey(state: PagingState<Int, Story>): Int? =
        state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Story> {
        return try {
            val position = params.key ?: INITIAL_PAGE_INDEX
            val responseData =
                remoteDataSource.getAllStory(token, position, params.loadSize).story?.toStories() ?: listOf()
            Log.d("TAG", "load: responseData = $responseData")
            LoadResult.Page(
                data = responseData,
                prevKey = if (position == INITIAL_PAGE_INDEX) null else position - 1,
                nextKey = if (responseData.isNullOrEmpty()) null else position + 1
            )
        } catch (exception: Exception) {
            return LoadResult.Error(exception)
        }

    }
}