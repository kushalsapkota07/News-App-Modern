package com.example.newsappmodern.ui

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import com.example.newsappmodern.R
import com.example.newsappmodern.databinding.CustomTextSizeSelectorBinding
import com.example.newsappmodern.util.TextSize
import com.google.android.material.card.MaterialCardView

class CustomPersonalizeFeed(
    context: Context,
    attributeSet: AttributeSet
): MaterialCardView(
    context,
    attributeSet
) {
    init {
        val customTextSizeSelectorBinding = CustomTextSizeSelectorBinding.inflate(LayoutInflater.from(context),this, true)
        val attributes = context.obtainStyledAttributes(attributeSet, R.styleable.CustomPersonalizeFeed)
        customTextSizeSelectorBinding.ivOptionItem.setImageDrawable(attributes.getDrawable(R.styleable.CustomPersonalizeFeed_srcImagePersonalize))
        customTextSizeSelectorBinding.tvOptionTitle.text = attributes.getString(R.styleable.CustomPersonalizeFeed_personalizeFeedText)
        customTextSizeSelectorBinding.ivOptionItemExpand.setImageDrawable(attributes.getDrawable(R.styleable.CustomPersonalizeFeed_srcIconNavigate))

        attributes.recycle()

    }
}