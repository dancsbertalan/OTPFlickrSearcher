package com.bertalandancs.otpflickrsearcher.ui.details

import org.koin.androidx.fragment.dsl.fragment
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val detailsModule = module {
    fragment {
        DetailsFragment(get())
    }

    viewModel {
        DetailsViewModel(get())
    }
}