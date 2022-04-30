package id.derysudrajat.storyapp.widget

import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import androidx.core.os.bundleOf
import id.derysudrajat.storyapp.R
import id.derysudrajat.storyapp.data.model.Story
import id.derysudrajat.storyapp.repo.StoryRepository
import id.derysudrajat.storyapp.utils.CameraUtils.getBitmapFromURL
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

internal class StoryRemoteViewFactory(
    private val mContext: Context,
    private val repository: StoryRepository
) : RemoteViewsService.RemoteViewsFactory {

    private val listOfWidget = mutableListOf<Story>()
    private val job = SupervisorJob()
    private val scope: CoroutineScope by lazy { CoroutineScope(Dispatchers.IO + job) }


    override fun onCreate() {
        getAllStories()
    }

    override fun onDataSetChanged() {
        getAllStories()
    }

    private fun getAllStories() {
        scope.launch {
            repository.getLocalStories().collect {
                listOfWidget.clear()
                listOfWidget.addAll(it)
            }
        }
    }

    override fun onDestroy() {
        job.cancel()
    }

    override fun getCount(): Int = listOfWidget.size

    override fun getViewAt(position: Int): RemoteViews {
        val rv = RemoteViews(mContext.packageName, R.layout.item_widget)
        val story = listOfWidget[position]

        val bitmap = story.photoUrl.getBitmapFromURL()
        rv.setImageViewBitmap(R.id.ivItemWidget, bitmap)
        rv.setTextViewText(R.id.tvUserNameWidget, story.name)
        rv.setTextViewText(R.id.tvDescWidget, story.description)

        val extras = bundleOf(
            StoryWidget.EXTRA_ITEM to position
        )
        val fillInIntent = Intent()
        fillInIntent.putExtras(extras)

        rv.setOnClickFillInIntent(R.id.ivItemWidget, fillInIntent)
        return rv
    }

    override fun getLoadingView(): RemoteViews? = null

    override fun getViewTypeCount(): Int = 1

    override fun getItemId(i: Int): Long = 0

    override fun hasStableIds(): Boolean = false
}