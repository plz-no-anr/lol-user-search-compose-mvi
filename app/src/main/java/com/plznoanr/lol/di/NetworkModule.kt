//package com.plznoanr.lol.di
//
//import android.content.Context
//import com.plznoanr.lol.utils.NetworkManager
//import dagger.Module
//import dagger.Provides
//import dagger.hilt.InstallIn
//import dagger.hilt.android.qualifiers.ApplicationContext
//import dagger.hilt.components.SingletonComponent
//import javax.inject.Singleton
//
//@Module
//@InstallIn(SingletonComponent::class)
//object NetworkModule {
//    @Provides
//    @Singleton
//    fun provideNetworkManager(@ApplicationContext context: Context): NetworkManager = NetworkManager(context)
//}