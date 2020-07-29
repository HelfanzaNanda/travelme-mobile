package com.travelme.customer

import android.app.Application
import com.travelme.customer.activities.forgot_password.ForgotPasswordViewModel
import com.travelme.customer.activities.hour.HourViewModel
import com.travelme.customer.activities.login.LoginViewModel
import com.travelme.customer.activities.order.OrderActivityViewModel
import com.travelme.customer.activities.owner.OwnerViewModel
import com.travelme.customer.activities.register.RegisterViewModel
import com.travelme.customer.activities.update_profile.UpdateProfilViewModel
import com.travelme.customer.fragments.home.destination_other_fragment.DestinationOtherViewModel
import com.travelme.customer.fragments.home.destination_tegal_fragment.DestinationTegalViewModel
import com.travelme.customer.fragments.order.OrderFragmentViewModel
import com.travelme.customer.fragments.profile.ProfileViewModel
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

val retrofitModule = module {
    single { ApiClient.instance() }
    single { FirebaseRepository() }
}

val repositoryModules = module {
    factory { OrderRepository(get()) }
    factory { UserRepository(get()) }
    factory { OwnerRepository(get()) }
    factory { HourRepository(get()) }
    factory { DepartureRepository(get()) }
}

val viewModelModules = module {
    viewModel { LoginViewModel(get()) }
    viewModel { RegisterViewModel(get(),get()) }

    viewModel { HourViewModel(get()) }
    viewModel { OwnerViewModel(get()) }
    viewModel { OrderActivityViewModel(get(), get()) }

    viewModel { DestinationOtherViewModel(get()) }
    viewModel { DestinationTegalViewModel(get()) }

    viewModel { OrderFragmentViewModel(get()) }
    viewModel { ProfileViewModel(get()) }
    viewModel { UpdateProfilViewModel(get()) }
    viewModel { ForgotPasswordViewModel(get()) }
}