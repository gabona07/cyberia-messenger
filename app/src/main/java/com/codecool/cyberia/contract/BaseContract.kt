package com.codecool.cyberia.contract

import java.lang.Exception

interface BaseContract {

    interface BasePresenter {
        fun onAttach(view: BaseView)
        fun onDetach()
    }

    interface BaseView {
        fun showLoading()
        fun hideLoading()
        fun onError(exception: Exception)
    }
}