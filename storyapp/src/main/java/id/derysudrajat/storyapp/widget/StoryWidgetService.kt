package id.derysudrajat.storyapp.widget

import android.content.Intent
import android.widget.RemoteViewsService
import dagger.hilt.android.AndroidEntryPoint
import id.derysudrajat.storyapp.repo.StoryRepository
import javax.inject.Inject

@AndroidEntryPoint
class StoryWidgetService : RemoteViewsService() {
    @Inject
    lateinit var repository: StoryRepository

    override fun onGetViewFactory(intent: Intent): RemoteViewsFactory {
        return StoryRemoteViewFactory(this.applicationContext, repository)
    }
}