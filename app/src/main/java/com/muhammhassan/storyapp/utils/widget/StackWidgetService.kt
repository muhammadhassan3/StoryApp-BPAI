package com.muhammhassan.storyapp.utils.widget

import android.content.Intent
import android.widget.RemoteViewsService
import com.muhammhassan.storyapp.data.usecase.StoryListUseCase
import org.koin.android.ext.android.inject

class StackWidgetService: RemoteViewsService() {
    private val useCase by inject<StoryListUseCase>()
    override fun onGetViewFactory(intent: Intent?): RemoteViewsFactory = StackRemoteViewsFactory(this.applicationContext, useCase)
}