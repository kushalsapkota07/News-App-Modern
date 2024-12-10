package com.example.newsappmodern.ui

import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewPropertyAnimator
import android.view.animation.TranslateAnimation
import androidx.annotation.Dimension
import com.example.newsappmodern.R
import com.example.newsappmodern.databinding.CustomTextSizeSelectorBinding
import com.example.newsappmodern.util.TextSize


import com.google.android.material.card.MaterialCardView



class CustomTextSizeSelector(
    context: Context,
    attributeSet: AttributeSet
): MaterialCardView(
    context,
    attributeSet
) {
    //The default text size is set to 16sp (Medium)
    private var DEFAULT_TEXT_SIZE:Float = 16f
    var textSize = TextSize.MEDIUM.size

    // Set the view in collapsed mode by default
    private var state: Int = 0

    //Stroke Width (Selected Button)
    private var SELECTED_BUTTON_STROKE_WIDTH = 4
    //Stroke Width (Unselected Button)
    private var UNSELECTED_BUTTON_STROKE_WIDTH = 0


    init {
        val customTextSizeSelectorBinding = CustomTextSizeSelectorBinding.inflate(LayoutInflater.from(context),this, true)
        val attributes = context.obtainStyledAttributes(attributeSet, R.styleable.CustomTextSizeSelector)
        customTextSizeSelectorBinding.ivOptionItem.setImageDrawable(attributes.getDrawable(R.styleable.CustomTextSizeSelector_srcImageTextSize))
        customTextSizeSelectorBinding.tvOptionTitle.text = attributes.getString(R.styleable.CustomTextSizeSelector_textSizeText)
        customTextSizeSelectorBinding.buttonGroup.visibility = attributes.getInt(R.styleable.CustomTextSizeSelector_buttonGroupVisibility, state)

        customTextSizeSelectorBinding.ivOptionItemExpand.setOnClickListener {
            customTextSizeSelectorBinding.buttonGroup.visibility = View.VISIBLE
            customTextSizeSelectorBinding.ivOptionItemExpand.visibility = View.GONE
            customTextSizeSelectorBinding.ivOptionItemCollapse.visibility = View.VISIBLE
            customTextSizeSelectorBinding.parent.strokeWidth = 4
        }
        customTextSizeSelectorBinding.ivOptionItemCollapse.setOnClickListener {
            customTextSizeSelectorBinding.buttonGroup.visibility = View.GONE
            customTextSizeSelectorBinding.ivOptionItemCollapse.visibility = View.GONE
            customTextSizeSelectorBinding.ivOptionItemExpand.visibility = View.VISIBLE
            customTextSizeSelectorBinding.parent.strokeWidth = 0
        }
        //Text Size
        val defaultTextSize = attributes.getInt(R.styleable.CustomTextSizeSelector_defaultTextSize,
            DEFAULT_TEXT_SIZE.toInt()
        )
        customTextSizeSelectorBinding.btnMedium.strokeWidth = 2
        customTextSizeSelectorBinding.sampleText.textSize = defaultTextSize.toFloat()
        attributes.recycle()

        customTextSizeSelectorBinding.btnSmall.setOnClickListener{
            customTextSizeSelectorBinding.sampleText.textSize = TextSize.SMALL.size
            textSize = TextSize.SMALL.size
            handleButtonClick(1)
            customTextSizeSelectorBinding.btnSmall.strokeWidth = SELECTED_BUTTON_STROKE_WIDTH
            customTextSizeSelectorBinding.btnMedium.strokeWidth = UNSELECTED_BUTTON_STROKE_WIDTH
            customTextSizeSelectorBinding.btnLarge.strokeWidth = UNSELECTED_BUTTON_STROKE_WIDTH
        }
        customTextSizeSelectorBinding.btnLarge.setOnClickListener {
            customTextSizeSelectorBinding.sampleText.textSize = TextSize.LARGE.size
            textSize = TextSize.LARGE.size
            handleButtonClick(2)
            customTextSizeSelectorBinding.btnLarge.strokeWidth = SELECTED_BUTTON_STROKE_WIDTH
            customTextSizeSelectorBinding.btnSmall.strokeWidth = UNSELECTED_BUTTON_STROKE_WIDTH
            customTextSizeSelectorBinding.btnMedium.strokeWidth = UNSELECTED_BUTTON_STROKE_WIDTH
        }
        customTextSizeSelectorBinding.btnMedium.setOnClickListener {
            customTextSizeSelectorBinding.sampleText.textSize = TextSize.MEDIUM.size
            textSize = TextSize.MEDIUM.size
            handleButtonClick(3)
            customTextSizeSelectorBinding.btnMedium.strokeWidth = SELECTED_BUTTON_STROKE_WIDTH
            customTextSizeSelectorBinding.btnSmall.strokeWidth = UNSELECTED_BUTTON_STROKE_WIDTH
            customTextSizeSelectorBinding.btnLarge.strokeWidth = UNSELECTED_BUTTON_STROKE_WIDTH
        }

    }

    private fun handleButtonClick(buttonId: Int){
        onButtonClickListener?.onButtonClicked(buttonId)
    }

    interface OnButtonClickListener{
        fun onButtonClicked(buttonId: Int)
    }

    private var onButtonClickListener: OnButtonClickListener?=null

    fun setOnButtonClickListener(listener: OnButtonClickListener){
        this.onButtonClickListener = listener
    }

}