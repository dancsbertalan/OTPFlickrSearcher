package com.bertalandancs.otpflickrsearcher.view

import android.content.Context
import android.text.Html
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

    var title: String?
        set(value) {
            setText(value, binding.title)
        }
        get() = binding.title.text.toString()

    var value: String
        set(value) {
            setText(value, binding.value)
        }
        get() = binding.value.text.toString()

    init {
        addView(binding.root)
        val attributes = context?.obtainStyledAttributes(attrs, R.styleable.DetailsItem)

        if (attributes != null) {
            title = attributes.getString(R.styleable.DetailsItem_title) ?: ""
            value = attributes.getString(R.styleable.DetailsItem_value) ?: ""

            val isCentered = attributes.getBoolean(R.styleable.DetailsItem_centered, false)
            if (isCentered) {
                binding.value.textAlignment = TEXT_ALIGNMENT_CENTER
                binding.title.textAlignment = TEXT_ALIGNMENT_CENTER
            }

            attributes.recycle()
        }
    }

    private fun setText(text: String?, textView: TextView) =
        if (text.isNullOrEmpty())
            textView.isVisible = false
        else {
            textView.isVisible = true
            textView.text = Html.fromHtml(text)
        }
}