package dev.luteoos.delegaterecyclerview.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

/**
 * Delegate providing specific item view for [DelegateRecyclerViewAdapter]
 *
 * @param Data type used within [DelegateRecyclerViewAdapter] to recognize appropriate [RecyclerViewDelegate]
 * @param HolderType extends [DelegateRecyclerViewHolder]
 *
 * @author [Luteoos](https://github.com/Luteoos)
 */
abstract class RecyclerViewDelegate<
    Data,
    HolderType : DelegateRecyclerViewHolder<in Data, in Binding>,
    Binding : ViewDataBinding>
(val clazz: Class<Data>) {

    var viewType: Int = 0
    protected abstract val layoutId: Int

    protected abstract fun createViewHolder(view: View): HolderType

    protected open fun bindViewHolder(
        item: Data,
        holder: HolderType,
        position: Int,
        itemsSize: Int
    ) {
        holder.bind(item, position, itemsSize)
    }

    open fun onViewRecycled(holderType: HolderType) {}

    open fun getItemId(item: Data): Long = -1

    fun onCreateViewHolder(parent: ViewGroup): HolderType {
        val binding = DataBindingUtil.inflate<Binding>(LayoutInflater.from(parent.context), layoutId, parent, false)
        return createViewHolder(binding.root).also {
            it.initBinding(binding)
        }
    }

    fun isMatchingViewType(items: List<*>, position: Int): Boolean = clazz.isInstance(items[position])

    fun onBindViewHolder(
        item: Data,
        holder: RecyclerView.ViewHolder,
        position: Int,
        itemsSize: Int
    ) {
        @Suppress("UNCHECKED_CAST")
        bindViewHolder(item, holder as HolderType, position, itemsSize)
    }
}
