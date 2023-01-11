package dev.luteoos.delegaterecyclerview.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

/**
 * [RecyclerView.Adapter] implementation with support for delegate-based view building
 *
 * @author [Luteoos](https://github.com/Luteoos)
 */
class DelegateRecyclerViewAdapter : RecyclerView.Adapter<DelegateRecyclerViewHolder<Any, *>>() {

    val delegates = mutableListOf<RecyclerViewDelegate<Any, in DelegateRecyclerViewHolder<Any, *>, *>>()
    private val items: MutableList<Any> = mutableListOf()
    private var nextViewType = 0

    /**
     * Add items to list
     *
     * @param list list of items to add
     */
    fun addItems(list: List<Any>) {
        items.let {
            val start = it.size + 1
            it.addAll(list)
            val end = it.size
            if (start == 1)
                notifyDataSetChanged()
            else
                notifyItemRangeChanged(start, end)
        }
    }
    /**
     * Add items to list
     *
     * @param item list of items to add
     */
    fun addItems(vararg item: Any) {
        items.let {
            val start = it.size + 1
            it.addAll(item)
            val end = it.size
            if (start == 1)
                notifyDataSetChanged()
            else
                notifyItemRangeChanged(start, end)
        }
    }

    /**
     * Update [item] in [position]
     *
     * @param position of update item
     * @param item used to update
     * @param notify [notifyItemChanged]
     */
    fun updateItem(position: Int, item: Any, notify: Boolean = true) {
        items[position] = item
        if (notify) notifyItemChanged(position)
    }

    /**
     * Insert [item] into [position]
     *
     * @param position where to insert
     * @param item to insert
     */
    fun addItem(position: Int, item: Any) {
        items.add(position, item)
        notifyItemInserted(position)
        if (position <= items.size - 1)
            notifyItemRangeChanged(position + 1, items.size)
    }

    /**
     * Remove item from [position]
     *
     * @param position to remove item from
     */
    fun removeItem(position: Int) {
        items.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, items.size)
    }

    /**
     * Clear items and reload from [list]
     *
     * @param list of new items
     */
    fun reloadItems(list: List<Any>) {
        items.clear()
        addItems(list)
    }

    /**
     * Get current items
     */
    fun getItems(): List<Any> = items.toList()

    /**
     * Add [RecyclerViewDelegate] to [delegates]
     */
    fun <T : RecyclerViewDelegate<*, *, *>> addDelegate(delegate: T) {
        if (nextViewType == Int.MAX_VALUE)
            nextViewType = 0
        delegate.viewType = nextViewType++
        @Suppress("UNCHECKED_CAST")
        delegates.add(delegate as RecyclerViewDelegate<Any, in DelegateRecyclerViewHolder<Any, *>, *>)
    }

    override fun onBindViewHolder(holder: DelegateRecyclerViewHolder<Any, *>, position: Int) {
        delegates.filter { it.viewType == holder.itemViewType }
            .forEach {
                it.onBindViewHolder(items[position], holder, position, items.size)
            }
    }

    override fun getItemViewType(position: Int): Int =
        delegates.firstOrNull { it.isMatchingViewType(items, position) }?.viewType ?: -1

    @Suppress("UNCHECKED_CAST")
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DelegateRecyclerViewHolder<Any, *> {
        delegates.firstOrNull { it.viewType == viewType }
            ?.let { return it.onCreateViewHolder(parent) as DelegateRecyclerViewHolder<Any, *> }

        throw IllegalArgumentException(
            "No matching delegate found for view type $viewType. " +
                "Current view types : \n" + delegates.joinToString("\n") { "${it.clazz.simpleName}[${it.viewType}]" }
        )
    }

    override fun onViewRecycled(holder: DelegateRecyclerViewHolder<Any, *>) {
        super.onViewRecycled(holder)
        delegates.filter { it.viewType == holder.itemViewType }
            .forEach { it.onViewRecycled(holder) }
    }

    override fun getItemCount(): Int = items.size
}
