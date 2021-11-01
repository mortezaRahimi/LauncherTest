package com.mortex.launchertest.di

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.mortex.launchertest.BuildConfig
import com.mortex.launchertest.local.AppDatabase
//import com.mortex.launchertest.local.AppDatabase
import com.mortex.launchertest.local.Constants
//import com.mortex.mortext.local.MortextDao
import com.mortex.launchertest.local.SessionManager
import com.mortex.launchertest.network.URLConstants.BASE_URL
import com.mortex.launchertest.ui.login.LoginRepository
import com.mortex.launchertest.ui.login.remote.LoginRemoteDataSource
import com.mortex.launchertest.ui.login.remote.LoginService
//import com.mortex.mortext.ui.splash.remote.SplashRemoteDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideOkHttpClient() = if (BuildConfig.DEBUG) {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    } else {
        OkHttpClient
            .Builder()
            .build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient, gson: Gson): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    @Provides
    fun provideGson(): Gson = GsonBuilder().create()

    @Singleton
    @Provides
    fun provideSharedPreferences(@ApplicationContext context: Context) =
        context.getSharedPreferences(
            Constants.PREF_NAME, Context.MODE_PRIVATE
        )!!

    @Singleton
    @Provides
    fun provideSessionManager(preferences: SharedPreferences) =
        SessionManager(preferences)


    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext appContext: Context) =
        AppDatabase.getDatabase(appContext)

    @Singleton
    @Provides
    fun provideMortextDao(db: AppDatabase) = db.launcherDao()


    @Provides
    fun provideLoginService(retrofit: Retrofit): LoginService =
        retrofit.create(LoginService::class.java)

    @Singleton
    @Provides
    fun provideLoginRemoteDataSource(loginService: LoginService) =
        LoginRemoteDataSource(loginService)

    @Singleton
    @Provides
    fun provideLoginRepository(
        remoteDataSource: LoginRemoteDataSource,
        sessionManager: SessionManager
    ) =
        LoginRepository(remoteDataSource, sessionManager)



}