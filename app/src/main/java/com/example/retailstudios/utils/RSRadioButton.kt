package com.example.retailstudios.utils

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatRadioButton

class RSRadioButton(context: Context, attributeSet: AttributeSet): AppCompatRadioButton(context,attributeSet) {

    init {
        applyFont()
    }

    private fun applyFont() {
        //This is used to get the file from from the assests folder and set it to the title textView.
        val typeface: Typeface =
            Typeface.createFromAsset(context.assets,"Montserrat-Bold.ttf")
        setTypeface(typeface)
    }
}