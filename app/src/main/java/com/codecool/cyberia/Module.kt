package com.codecool.cyberia


import com.codecool.cyberia.contract.*
import com.codecool.cyberia.presenter.*
import com.codecool.cyberia.view.ChatLogActivity
import org.koin.dsl.module

val appModule = module {
    single<ChatLogContract.ChatLogPresenter> {ChatLogPresenter()}
    single<LatestMessagesContract.LatestMessagesPresenter> { LatestMessagesPresenter() }
    single<LoginContract.LoginPresenter> { LoginPresenter() }
    single<NewMessageContract.NewMessagePresenter> { NewMessagePresenter() }
    single<RegistrationContract.RegistrationPresenter> { RegistrationPresenter() }
}