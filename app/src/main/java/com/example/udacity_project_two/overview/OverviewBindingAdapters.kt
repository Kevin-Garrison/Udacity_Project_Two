package com.example.udacity_project_two.overview

import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.udacity_project_two.R

//Bind the Picture of the Day and its content description in the overview
@BindingAdapter("imageUrl")
fun bindPodImage(imageView: ImageView, picture: PictureOfTheDay?) {
    if (picture?.mediaType == "image") {
        val url = picture.url
        url.let {
            //use Glide to download the image display it in imgView
            val imgUri = url.toUri().buildUpon().scheme("https").build()
            Glide.with(imageView.context)
                    .load(imgUri)
                    .apply(RequestOptions()
                            .placeholder(R.drawable.loading_animation)
                            .error(R.drawable.something_went_wrong))
                    .into(imageView)

            imageView.contentDescription = picture.title
        }
    } else {
        imageView.setImageResource(R.drawable.something_went_wrong)
        imageView.contentDescription = imageView.context.getString(R.string.pod_error)
    }
}

//Bind the status icon and its content description in the list item
@BindingAdapter("statusIcon")
fun bindAsteroidStatusImage(imageView: ImageView, isHazardous: Boolean) {
    if (isHazardous) {
        imageView.setImageResource(R.drawable.ic_status_potentially_hazardous)
        imageView.contentDescription = imageView.context.getString(R.string.hazardous_content_description)
    } else {
        imageView.setImageResource(R.drawable.ic_status_normal)
        imageView.contentDescription = imageView.context.getString(R.string.safe_content_description)
    }
}

//Bind the asteroid name in the list item
@BindingAdapter("asteroidName")
fun bindAsteroidName(textView: TextView, name: String) {
    textView.text = name
}

//Bind the asteroid date in the list item
@BindingAdapter("asteroidDate")
fun bindAsteroidDate(textView: TextView, date: String) {
    textView.text = date
}
