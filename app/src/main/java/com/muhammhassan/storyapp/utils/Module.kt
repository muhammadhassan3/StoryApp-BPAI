package com.muhammhassan.storyapp.utils

import android.content.Context
import com.muhammhassan.storyapp.data.datasource.RemoteDataSource
import com.muhammhassan.storyapp.data.interactor.DetailStoryInteractor
import com.muhammhassan.storyapp.data.interactor.LoginInteractor
import com.muhammhassan.storyapp.data.interactor.RegisterInteractor
import com.muhammhassan.storyapp.data.interactor.StoryListInteractor
import com.muhammhassan.storyapp.data.repository.StoryRepository
import com.muhammhassan.storyapp.data.repository.StoryRepositoryImpl
import com.muhammhassan.storyapp.data.repository.UserRepository
import com.muhammhassan.storyapp.data.repository.UserRepositoryImpl
import com.muhammhassan.storyapp.data.usecase.DetailStoryUseCase
import com.muhammhassan.storyapp.data.usecase.LoginUseCase
import com.muhammhassan.storyapp.data.usecase.RegisterUseCase
import com.muhammhassan.storyapp.data.usecase.StoryListUseCase
import com.muhammhassan.storyapp.utils.api.ApiInterface
import com.muhammhassan.storyapp.utils.widget.StackRemoteViewsFactory
import com.muhammhassan.storyapp.view.list.ListStoryViewModel
import com.muhammhassan.storyapp.view.login.LoginViewModel
import com.muhammhassan.storyapp.view.register.RegisterViewModel
import com.muhammhassan.storyapp.view.story.DetailStoryViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Module {
    val apiModule = module {
        val httpInterceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        fun getClient(context: Context) = OkHttpClient.Builder()
            .addInterceptor(AuthorizationInterceptor(context))
//            .addInterceptor(httpInterceptor)
            .build()

        single {
            val retrofit = Retrofit.Builder()
                .baseUrl("https://story-api.dicoding.dev/v1/")
                .client(getClient(androidContext()))
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            retrofit.create(ApiInterface::class.java)
        }
    }

    val dataSourceModule = module {
        single { RemoteDataSource.getInstance(get()) }
    }

    val repositoryModule = module {
        single<UserRepository> { UserRepositoryImpl.getInstance(get()) }
        single<StoryRepository> { StoryRepositoryImpl.getInstance(get()) }
    }

    val viewModelModule = module {
        viewModel { LoginViewModel(get(), get()) }
        viewModel { RegisterViewModel(get(), get()) }
        viewModel { ListStoryViewModel(get(), get()) }
        viewModel { DetailStoryViewModel(get(), get()) }
    }

    val useCaseModule = module {
        factory<LoginUseCase> { LoginInteractor.getInstance(get()) }
        factory<RegisterUseCase> { RegisterInteractor.getInstance(get()) }
        factory<StoryListUseCase> { StoryListInteractor.getInstance(get()) }
        factory<DetailStoryUseCase> { DetailStoryInteractor.getInstance(get()) }
    }
}