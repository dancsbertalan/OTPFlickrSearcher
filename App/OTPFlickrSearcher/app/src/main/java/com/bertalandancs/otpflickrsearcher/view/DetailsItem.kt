package com.bertalandancs.otpflickrsearcher.view

import android.content.Context
import android.text.util.Linkify
import android.util.AttributeSet
import android.util.Patterns
import android.view.LayoutInflater
import android.webkit.URLUtil
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.isVisible
import com.bertalandancs.otpflickrsearcher.R
import com.bertalandancs.otpflickrsearcher.databinding.DetailsItemBinding

class DetailsItem(context: Context?, attrs: AttributeSet?) : LinearLayout(context, attrs) {

    private var binding: DetailsItemBinding =
        DetailsItemBinding.inflate(LayoutInflater.from(context), this, false)

    init {
        addView(binding.root)
        val attributes = context?.obtainStyledAttributes(attrs, R.styleable.DetailsItem)
        if (attributes != null) {
            setText(attributes.getString(R.styleable.DetailsItem_title), binding.title)
            val valueString = attributes.getString(R.styleable.DetailsItem_value)
            setText(valueString, binding.value)
            if (!valueString.isNullOrEmpty() && (URLUtil.isValidUrl(valueString) && Patterns.WEB_URL.matcher(
                    valueString
                ).matches())
            ) Linkify.addLinks(binding.value, Linkify.WEB_URLS)

            attributes.recycle()
        }
    }

    private fun setText(text: String?, textView: TextView) =
        if (text.isNullOrEmpty()) textView.isVisible = false else textView.text = text
}