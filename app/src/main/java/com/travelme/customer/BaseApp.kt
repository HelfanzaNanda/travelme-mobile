package com.travelme.customer

import android.app.Application
import com.travelme.customer.activities.hour_activity.HourViewModel
import com.travelme.customer.activities.login_activity.LoginViewModel
import com.travelme.customer.activities.order_activity.OrderActivityViewModel
import com.travelme.customer.activities.owner_activity.OwnerViewModel
import com.travelme.customer.activities.register_activity.RegisterViewModel
import com.travelme.customer.fragments.home_fragment.destination_other_fragment.DestinationOtherViewModel
import com.travelme.customer.fragments.home_fragment.destination_tegal_fragment.DestinationTegalViewModel
import com.travelme.customer.fragments.notification_fragment.NotificationViewModel
import com.travelme.customer.fragments.order_fragment.OrderFragmentViewModel
import com.travelme.customer.fragments.profile_fragment.ProfileViewModel
import com.travelme.customer.repositories.*
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
    factory { OrderRepository(get()) }
    factory { UserRepository(get()) }
    factory { OwnerRepository(get()) }
    factory { HourRepository(get()) }
}

val viewModelModules = module {
    viewModel { LoginViewModel(get()) }
    viewModel { RegisterViewModel(get()) }

    viewModel { HourViewModel(get()) }
    viewModel { OwnerViewModel(get()) }
    viewModel { OrderActivityViewModel(get(), get()) }

    viewModel { DestinationOtherViewModel(get()) }
    viewModel { DestinationTegalViewModel(get()) }

    viewModel { OrderFragmentViewModel(get()) }
    viewModel { NotificationViewModel(get()) }
    viewModel { ProfileViewModel(get()) }
}