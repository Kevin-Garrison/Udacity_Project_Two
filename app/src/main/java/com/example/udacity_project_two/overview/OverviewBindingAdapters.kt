package com.example.udacity_project_two.overview

import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import com.example.udacity_project_two.R
import com.squareup.picasso.Picasso

@BindingAdapter("itemUrl")
fun bindPodImage(imageView: ImageView, picture: PictureOfTheDay?) {
    if (picture?.mediaType == "image") {
        val imgURI = picture.url.toUri().buildUpon().scheme("https").build()
        Picasso.get().load(imgURI)
            .placeholder(R.drawable.loading_animation)
            .error(R.drawable.ic_broken_image)
            .into(imageView)
        imageView.contentDescription = picture.title
    } else if(picture?.mediaType == "video") {
        imageView.setImageResource(R.drawable.ic_play_arrow)
        val context = imageView.context
        imageView.contentDescription = context.getString(R.string.pod_error)
    }
}

@BindingAdapter("imageUrl")
fun loadPictureOfTheDay(imageView: ImageView, url: String?) {
    url?.let {
        Picasso.get().load(it)
            .error(R.drawable.ic_broken_image)//placeholder_picture_of_day)
            .into(imageView)
    }
}

@BindingAdapter("asteroidName")
fun bindAsteroidName(textView: TextView, name: String) {
    textView.text = name
}

@BindingAdapter("asteroidDate")
fun bindAsteroidDate(textView: TextView, date: String) {
    textView.text = date
}

@BindingAdapter("asteroidStatusImage")
fun bindDetailsStatusImage(imageView: ImageView, isHazardous: Boolean) {
    if (isHazardous) {
        imageView.setImageResource(R.drawable.asteroid_hazardous)
        imageView.contentDescription = imageView.context.getString(R.string.hazardous_content_description)
    } else {
        imageView.setImageResource(R.drawable.asteroid_safe)
        imageView.contentDescription = imageView.context.getString(R.string.safe_content_description)
    }
}

@BindingAdapter("podDescription")
fun bindPictureDescription(imageView: ImageView, picture: PictureOfTheDay?) {
    if (null != picture) {
        imageView.contentDescription =
            imageView.context.getString(
                R.string.pod_content_description_format, picture.title
            )
    } else {
        imageView.contentDescription =
            imageView.context.getString(R.string.pod_blank)
    }
}

@BindingAdapter("statusIcon")
fun bindAsteroidStatusImage(imageView: ImageView, isHazardous: Boolean) {
    if (isHazardous) {
        imageView.setImageResource(R.drawable.ic_status_potentially_hazardous)
    } else {
        imageView.setImageResource(R.drawable.ic_status_normal)
    }
}