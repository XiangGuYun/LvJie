package com.kotlinlib.common.listener

import android.text.Editable
import android.text.TextWatcher
import android.util.Log

interface OnTextWatcher : TextWatcher {
    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        Log.d("OnTextWatcher", "s is $s, slength is ${s?.length}, start is $start, before is $before, count is $count")
    }

    override fun afterTextChanged(s: Editable?) {
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        Log.d("beforeTextChanged", "s is $s, slength is ${s?.length}, start is $start, after is $after, count is $count")
    }
}