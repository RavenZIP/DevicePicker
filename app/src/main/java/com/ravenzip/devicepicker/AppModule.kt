package com.ravenzip.devicepicker

import com.ravenzip.devicepicker.repositories.AuthRepository
import com.ravenzip.devicepicker.repositories.DeviceRepository
import com.ravenzip.devicepicker.repositories.ImageRepository
import com.ravenzip.devicepicker.repositories.SharedRepository
import com.ravenzip.devicepicker.repositories.UserRepository
import com.ravenzip.devicepicker.sources.AuthSources
import com.ravenzip.devicepicker.sources.DeviceSources
import com.ravenzip.devicepicker.sources.ImageSources
import com.ravenzip.devicepicker.sources.UserSources
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideDeviceSources(): DeviceSources {
        return DeviceSources()
    }

    @Provides
    @Singleton
    fun provideImageSources(): ImageSources {
        return ImageSources()
    }

    @Provides
    @Singleton
    fun provideAuthSources(): AuthSources {
        return AuthSources()
    }

    @Provides
    @Singleton
    fun provideUserSources(): UserSources {
        return UserSources()
    }

    @Provides
    @Singleton
    fun provideDeviceRepository(): DeviceRepository {
        return DeviceRepository(provideDeviceSources())
    }

    @Provides
    @Singleton
    fun provideImageRepository(): ImageRepository {
        return ImageRepository(provideImageSources())
    }

    @Provides
    @Singleton
    fun provideAuthRepository(): AuthRepository {
        return AuthRepository(provideAuthSources())
    }

    @Provides
    @Singleton
    fun provideUserRepository(): UserRepository {
        return UserRepository(provideUserSources())
    }

    @Provides
    @Singleton
    fun provideSharedRepository(): SharedRepository {
        return SharedRepository(
            provideDeviceRepository(),
            provideImageRepository(),
            provideAuthRepository(),
            provideUserRepository(),
        )
    }
}
