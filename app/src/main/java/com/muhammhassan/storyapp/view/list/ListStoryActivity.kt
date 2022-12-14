package com.muhammhassan.storyapp.view.list

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.muhammhassan.storyapp.R
import com.muhammhassan.storyapp.databinding.ActivityListStoryBinding
import com.muhammhassan.storyapp.utils.Extension.gone
import com.muhammhassan.storyapp.utils.Extension.show
import com.muhammhassan.storyapp.utils.NoDataException
import com.muhammhassan.storyapp.utils.api.Status
import com.muhammhassan.storyapp.view.adapter.StoryListAdapter
import com.muhammhassan.storyapp.view.adapter.loadstate.StoryLoadState
import com.muhammhassan.storyapp.view.login.LoginActivity
import com.muhammhassan.storyapp.view.story.DetailStoryActivity
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import androidx.core.util.Pair
import com.muhammhassan.storyapp.utils.Extension.startActivityWithAnimation
import com.muhammhassan.storyapp.view.map.MapActivity

class ListStoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityListStoryBinding
    private val viewModel: ListStoryViewModel by viewModel()
    private val listAdapter = StoryListAdapter { data, image ->
        val intent = Intent(this, DetailStoryActivity::class.java)
        intent.putExtra(DetailStoryActivity.DATA, data)
        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
            this,
            Pair(image, "photo")
        ).toBundle()
        startActivity(intent, options)
    }

    private val addCallbackResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        if(it.resultCode == RESULT_OK){
            binding.swipeList.post{
                binding.swipeList.isRefreshing = true
                listAdapter.refresh()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initList()

        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            title = getString(R.string.app_name)
        }

        binding.btnAdd.setOnClickListener {
            val intent = Intent(this, DetailStoryActivity::class.java)
            addCallbackResult.launch(intent)
        }

        binding.swipeList.apply {
            setOnRefreshListener {
                listAdapter.refresh()
            }
        }
        binding.btnRetry.setOnClickListener{
            listAdapter.refresh()
        }
    }

    private fun initList() {
        val concatAdapter = listAdapter.withLoadStateFooter(
            footer = StoryLoadState {
                listAdapter.retry()
            }
        )

        var state: LoadState = LoadState.NotLoading(endOfPaginationReached = false)
        listAdapter.addLoadStateListener {
            if (state != it.source.refresh) {
                if (listAdapter.itemCount > 0 && it.source.refresh is LoadState.NotLoading) {
                    showStatus(Status.SUCCESS)
                    binding.rvList.scrollToPosition(0)
                    binding.swipeList.isRefreshing = false
                } else if (it.source.refresh is LoadState.Loading) {
                    showStatus(Status.LOADING)
                } else if (it.source.refresh is LoadState.Error) {
                    val errorType = (it.refresh as LoadState.Error).error
                    Log.e("PagingSource", errorType.message.toString())
                    if (errorType is NoDataException) {
                        showStatus(Status.NO_DATA)
                    } else {
                        showStatus(Status.ERROR)
                    }
                }
                state = it.source.refresh
            }
        }

        binding.rvList.apply {
            adapter = concatAdapter
            layoutManager =
                LinearLayoutManager(this@ListStoryActivity, LinearLayoutManager.VERTICAL, false)
        }

        lifecycleScope.launch {
            viewModel.getStoryList().collect {
                Log.e(TAG, "initList: Data submitted")
                listAdapter.submitData(it)
            }
        }
    }

    private fun showStatus(status: Status) {
        binding.apply {
            when (status) {
                Status.LOADING -> {
                    noDataContent.gone()
                    errorContent.gone()
                    shimmer.apply {
                        startShimmer()
                        show()
                    }
                    rvList.gone()
                }
                Status.SUCCESS -> {
                    noDataContent.gone()
                    errorContent.gone()
                    shimmer.apply {
                        stopShimmer()
                        gone()
                    }
                    rvList.show()
                }
                Status.NO_DATA -> {
                    noDataContent.show()
                    errorContent.gone()
                    shimmer.apply {
                        stopShimmer()
                        gone()
                    }
                    rvList.gone()
                }
                Status.ERROR -> {
                    noDataContent.gone()
                    errorContent.show()
                    shimmer.apply {
                        stopShimmer()
                        gone()
                    }
                    rvList.gone()
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.options_logout, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.localization -> {
                val intent = Intent(Settings.ACTION_LOCALE_SETTINGS)
                startActivityWithAnimation(intent)
            }
            R.id.logout -> {
                viewModel.logout()
                val intent = Intent(this@ListStoryActivity, LoginActivity::class.java)
                startActivityWithAnimation(intent)
                finish()
            }
            R.id.optMap -> {
                val intent = Intent(this, MapActivity::class.java)
                startActivityWithAnimation(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        private const val TAG = "ListStoryActivity"
    }
}