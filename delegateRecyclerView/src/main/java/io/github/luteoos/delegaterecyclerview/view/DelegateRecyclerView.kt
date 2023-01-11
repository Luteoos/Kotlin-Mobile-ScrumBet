package dev.luteoos.delegaterecyclerview.view

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.RecyclerView

/**
 * ViewWidget for implementing delegate-based [RecyclerView]
 *
 * @author [Luteoos](https://github.com/Luteoos)
 */
class DelegateRecyclerView(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttributes: Int = 0
) : RecyclerView(context, attributeSet, defStyleAttributes)
