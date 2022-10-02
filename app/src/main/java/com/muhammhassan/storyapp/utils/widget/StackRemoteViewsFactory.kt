package com.muhammhassan.storyapp.utils.widget

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.util.Log
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import androidx.core.os.bundleOf
import androidx.lifecycle.asLiveData
import com.bumptech.glide.Glide
import com.muhammhassan.storyapp.R
import com.muhammhassan.storyapp.data.model.response.StoriesResponseModel
import com.muhammhassan.storyapp.data.usecase.StoryListUseCase
import com.muhammhassan.storyapp.utils.api.Status
import com.muhammhassan.storyapp.utils.widget.ImageStoryWidget.Companion.EXTRA_ITEM

class StackRemoteViewsFactory(private val context: Context, private val useCase: StoryListUseCase): RemoteViewsService.RemoteViewsFactory {
    private val list = mutableListOf<StoriesResponseModel>()
    private val listBitmap = mutableListOf<Bitmap>()

    override fun onCreate() {
        useCase.getData().asLiveData().observeForever {
            Log.e("Stack Widget", "onCreate: Retrieving data", )
            when(it.status){
                Status.LOADING -> list.clear()
                Status.SUCCESS -> {
                    it.data?.map { data -> list.add(data) }
                }
                Status.NO_DATA -> list.clear()
                Status.ERROR -> list.clear()
            }
        }
    }

    override fun onDataSetChanged() {
        list.map {
            val future = Glide.with(context)
                .asBitmap()
                .load(it.photoUrl)
                .submit()

            future.get()?.let{ bitmap ->
                listBitmap.add(bitmap)
            }
        }
    }

    override fun onDestroy() {
        list.clear()
        listBitmap.clear()
    }

    override fun getCount(): Int = list.size

    override fun getViewAt(position: Int): RemoteViews {
        val rv = RemoteViews(context.packageName, R.layout.image_story_widget)
        rv.setImageViewBitmap(R.id.imgWidget, listBitmap[position])

        val extras = bundleOf(
            EXTRA_ITEM to position
        )

        val fillInIntent = Intent()
        fillInIntent.putExtras(extras)
        rv.setOnClickFillInIntent(R.id.imgWidget, fillInIntent)

        return rv
    }

    override fun getLoadingView(): RemoteViews? = null

    override fun getViewTypeCount(): Int = 1

    override fun getItemId(position: Int): Long  = position.toLong()

    override fun hasStableIds(): Boolean  = false
}