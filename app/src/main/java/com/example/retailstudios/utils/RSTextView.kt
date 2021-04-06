package com.example.retailstudios.utils

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

class RSTextView(context: Context, attrs: AttributeSet) : AppCompatTextView(context,attrs) {

    init {
        //Call the function to apply the font to the components.
        applyFont()
    }

    private fun applyFont() {
        //This is used to get the file from from the assests folder and set it to the title textView.
        val typeface: Typeface =
                Typeface.createFromAsset(context.assets,"Montserrat-Regular.ttf")
        setTypeface(typeface)
    }
}