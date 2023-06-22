package dev.luteoos.delegaterecyclerview.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

/**
 * [RecyclerView.ViewHolder] implementation for delegate support
 *
 * @param Data payload type provided to view
 * @param Binding ViewDataBinding for this holder
 *
 * @author [Luteoos](https://github.com/Luteoos)
 */
abstract class DelegateRecyclerViewHolder<Data, Binding : ViewBinding>(view: View) :
    RecyclerView.ViewHolder(view) {

    protected lateinit var binding: Binding

    /**
     * any usage of [binding] should be implemented here
     * @param item data to be displayed by this holder
     */
    abstract fun bind(item: Data, position: Int, itemsSize: Int)

    /**
     * Assigns proper [Binding] after layout inflation in [RecyclerViewDelegate]
     */
    fun initBinding(binding: Binding) {
        this.binding = binding
    }
}
