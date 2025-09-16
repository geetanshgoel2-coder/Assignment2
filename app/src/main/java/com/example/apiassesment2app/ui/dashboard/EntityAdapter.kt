package com.example.apiassesment2app.ui.dashboard

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.apiassesment2app.databinding.ItemEntityBinding

class EntityAdapter(
    private val onClick: (Map<String, Any?>) -> Unit
) : ListAdapter<Map<String, Any?>, EntityAdapter.VH>(Diff) {

    object Diff : DiffUtil.ItemCallback<Map<String, Any?>>() {
        override fun areItemsTheSame(old: Map<String, Any?>, new: Map<String, Any?>) = old == new
        override fun areContentsTheSame(old: Map<String, Any?>, new: Map<String, Any?>) = old == new
    }

    inner class VH(val b: ItemEntityBinding) : RecyclerView.ViewHolder(b.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = ItemEntityBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = getItem(position)

        val pairs = item
            .filterKeys { it != "description" }
            .entries
            .toList()

        val title = pairs.getOrNull(0)?.let { "${it.key}: ${it.value ?: ""}" } ?: "(no title)"
        val subtitle = pairs.getOrNull(1)?.let { "${it.key}: ${it.value ?: ""}" } ?: ""

        holder.b.tvTitle.text = title
        holder.b.tvSubtitle.text = subtitle
        holder.itemView.setOnClickListener { onClick(item) }
    }
}
