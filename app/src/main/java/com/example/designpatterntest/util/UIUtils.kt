package com.example.designpatterntest.util

import android.view.View
import com.google.android.material.snackbar.Snackbar

fun makeSnackBar(msg:String,view: View){
    Snackbar.make(view,msg,Snackbar.LENGTH_SHORT).show()

}