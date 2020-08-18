package com.codecool.cyberia.misc

import com.codecool.cyberia.presenter.ChatLogPresenter
import com.codecool.cyberia.view.ChatLogActivity
import org.koin.dsl.module.Module
import org.koin.dsl.module.module

val appModule: Module = module {
    single { ChatLogPresenter() }
    single { ChatLogActivity() }
}