package com.plznoanr.lol.core.data.di

import android.content.Context
import com.plznoanr.lol.core.common.di.AppDispatcher
import com.plznoanr.lol.core.common.di.Dispatcher
import com.plznoanr.lol.core.data.utils.JsonParser
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.serialization.json.Json
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object JsonModule {

    @Provides
    @Singleton
    fun provideJsonUtils(
        @ApplicationContext context: Context,
        @Dispatcher(AppDispatcher.Default) coroutineDispatcher: CoroutineDispatcher
    ): JsonParser = JsonParser(
        context,
        Json { ignoreUnknownKeys = true },
        coroutineDispatcher
    )
}