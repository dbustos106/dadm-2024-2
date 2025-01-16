package com.example.reto_8.di

import android.content.Context
import androidx.room.Room
import com.example.reto_8.data.local.AppDatabase
import com.example.reto_8.data.local.EnterpriseDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context = context,
            AppDatabase::class.java,
            name = "enterprise_database"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideEnterpriseDao(appDatabase: AppDatabase): EnterpriseDao {
        return appDatabase.enterpriseDao()
    }

}
