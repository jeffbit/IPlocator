package com.example.jeff.iplocator.util

import android.content.Context
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.jeff.iplocator.R
import com.squareup.picasso.Picasso


//load image from retrofit call
fun loadImageToDisplay(url: String?, imageView: ImageView, widthResize: Int, heightResize: Int) {
    if (url != null) {
        Picasso.get().load(url).resizeDimen(widthResize, heightResize)
            .placeholder(R.drawable.ic_image_black_24dp)
            .error(R.drawable.ic_broken_image_black_24dp)
            .into(imageView)
    } else {
        imageView.visibility = View.GONE
    }

}

//Utility to hide keyboard from view
fun AppCompatActivity.closeKeyBoard() {
    val view = this.currentFocus
    if (view != null) {
        val iim = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        iim.hideSoftInputFromWindow(view.windowToken, 0)
    } else {
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
    }

}

fun Fragment.hideKeyBoard() {
    val activity = this.activity
    if (activity is AppCompatActivity) {
        activity.closeKeyBoard()
    }
}