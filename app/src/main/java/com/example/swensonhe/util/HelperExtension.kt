package com.example.swensonhe.util

import android.widget.EditText
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

fun EditText.onChangeTextNew(dispossible: CompositeDisposable,callBackWhenUserSearch :(String?,String?)->Unit) {
  // keyword
        dispossible.add(
            RxTextView.textChanges(this)
                .debounce(500, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .map { it.toString() }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({keyword->
                    if (keyword.isNotEmpty()) // has value so we need to query and check the user info
                        callBackWhenUserSearch.invoke(keyword,null)
                }, {
                    callBackWhenUserSearch.invoke(null,it.message)

                }))


}