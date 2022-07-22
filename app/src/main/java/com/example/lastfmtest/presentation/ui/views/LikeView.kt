package com.example.lastfmtest.presentation.ui.views

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import com.example.lastfmtest.R

class LikeView(context: Context, attrs: AttributeSet?): AppCompatImageView(context, attrs) {
    init {
        setLiked(false)
    }

    fun setLiked(isLiked: Boolean) {
        when (isLiked) {
            true -> {
                setImageDrawable(
                    ContextCompat.getDrawable(
                        context,
                        R.drawable.ic_round_favorite_24
                    )
                )
                setColorFilter(
                    ContextCompat.getColor(
                        context,
                        R.color.favorite
                    )
                )
            }
            false -> {
                setImageDrawable(
                    ContextCompat.getDrawable(
                        context,
                        R.drawable.ic_baseline_favorite_border_24
                    )
                )
                setColorFilter(
                    ContextCompat.getColor(
                        context,
                        R.color.not_favorite
                    )
                )
            }
        }
    }
}