package com.example.retailstudios.utils

import android.content.Context
import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.retailstudios.R
import java.io.IOException

class GlideLoader(val context: Context) {

    fun loadedUserPicture(image: Any,imageView: ImageView){
        try {
            //load the user image in the imageview
            Glide
                .with(context)
                .load(Uri.parse(image.toString())) //URI of the image
                .centerCrop() //scale type of the image
                .placeholder(R.drawable.ic_user_placeholder) //default placeholder if image is failed to load
                .into(imageView) //the view in which the image will be loaded
        }catch (e:IOException){
            e.printStackTrace()
        }
    }
}