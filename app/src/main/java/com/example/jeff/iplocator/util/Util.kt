package com.example.jeff.iplocator.util

import android.annotation.SuppressLint
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.jeff.iplocator.R
import java.text.SimpleDateFormat


fun validateIpAddress(ip: String): Boolean {
    val pattern =
        "^((0|1\\d?\\d?|2[0-4]?\\d?|25[0-5]?|[3-9]\\d?)\\.){3}(0|1\\d?\\d?|2[0-4]?\\d?|25[0-5]?|[3-9]\\d?)$"
    return ip.matches(pattern.toRegex())
}

//load image from retrofit call
fun loadImageToDisplay(
    url: String?,
    imageView: ImageView,
    view: View,
    widthResize: Int,
    heightResize: Int
) {
    if (url != null) {
        imageView.visibility = View.VISIBLE
        Glide.with(view).load(url).override(widthResize, heightResize)
            .placeholder(R.drawable.ic_image_black_24dp)
            .error(R.drawable.ic_broken_image_black_24dp)
            .into(imageView)

    } else {
        imageView.visibility = View.GONE
    }
}

@SuppressLint("SimpleDateFormat")
fun convertTime(time: String): String? {
    val parser = SimpleDateFormat("yyy-MM-dd'T'HH:mm:ss")
    val formatter = SimpleDateFormat("MM.dd.yyyy HH:mm")
    val converted = formatter.format(parser.parse(time))
    return converted
}


//In Future get mobile cellular address







