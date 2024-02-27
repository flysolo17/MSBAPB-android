package com.danica.msbapb.config

import android.content.Context
import com.danica.msbapb.repository.AuthRepository
import com.danica.msbapb.repository.AuthRepositoryImpl
import com.danica.msbapb.repository.adminservices.LocationRepository
import com.danica.msbapb.repository.adminservices.LocationRepositoryImpl
import com.danica.msbapb.repository.incidents.IncidentReportRepository
import com.danica.msbapb.repository.incidents.IncidentReportRepositoryImpl
import com.danica.msbapb.repository.news.NewsRepository
import com.danica.msbapb.repository.news.NewsRepositoryImpl
import com.danica.msbapb.repository.perosnels.PersonelRepository
import com.danica.msbapb.repository.perosnels.PersonelRepositoryImpl
import com.danica.msbapb.services.AuthService
import com.danica.msbapb.services.IncidentReportService
import com.danica.msbapb.services.LocationService
import com.danica.msbapb.services.NewsService
import com.danica.msbapb.services.PersonelService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModules {

    @Singleton
    @Provides
    fun provideAuthService(): AuthService {
        return ApiInstance.api.create(AuthService::class.java)
    }

    @Singleton
    @Provides
    fun provideAuthRepository(@ApplicationContext context: Context,authService: AuthService): AuthRepository {
        return AuthRepositoryImpl(context,authService)
    }
    @Singleton
    @Provides
    fun provideIncidentReportRepository(): IncidentReportRepository {
        return IncidentReportRepositoryImpl(ApiInstance.api.create(IncidentReportService::class.java))
    }


    @Singleton
    @Provides
    fun providePersonelRepository(): PersonelRepository {
        return PersonelRepositoryImpl(ApiInstance.api.create(PersonelService::class.java))
    }


    @Singleton
    @Provides
    fun provideLocationRepository(): LocationRepository {
        return LocationRepositoryImpl(ApiInstance.api.create(LocationService::class.java))
    }


    @Singleton
    @Provides
    fun provideNewsRepository(): NewsRepository {
        return NewsRepositoryImpl(ApiInstance.api.create(NewsService::class.java))
    }
}