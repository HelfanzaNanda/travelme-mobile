package com.travelme.customer

import android.app.Application
import com.travelme.customer.activities.departure_by_dest_activity.DepartureByDestViewModel
import com.travelme.customer.activities.login_activity.LoginViewModel
import com.travelme.customer.activities.order_activity.OrderActivityViewModel
import com.travelme.customer.activities.register_activity.RegisterViewModel
import com.travelme.customer.fragments.home_fragment.HomeFragment
import com.travelme.customer.fragments.home_fragment.HomeViewModel
import com.travelme.customer.fragments.order_fragment.OrderFragmentViewModel
import com.travelme.customer.fragments.profile_fragment.ProfileViewModel
import com.travelme.customer.repositories.DepartureRepository
import com.travelme.customer.repositories.OrderRepository
import com.travelme.customer.repositories.UserRepository
import com.travelme.customer.webservices.ApiClient
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.dsl.module

class BaseApp : Application(){
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@BaseApp)
            modules(listOf(repositoryModules, viewModelModules, retrofitModule))
        }
    }
}

val retrofitModule = module { single { ApiClient.instance() } }

val repositoryModules = module {
    factory { DepartureRepository(get()) }
    factory { OrderRepository(get()) }
    factory { UserRepository(get()) }
}

val viewModelModules = module {
    viewModel { DepartureByDestViewModel(get()) }
    viewModel { LoginViewModel(get()) }
    viewModel { OrderActivityViewModel(get(), get()) }
    viewModel { RegisterViewModel(get()) }

    viewModel { HomeViewModel(get()) }
    viewModel { OrderFragmentViewModel(get()) }
    viewModel { ProfileViewModel(get()) }
}