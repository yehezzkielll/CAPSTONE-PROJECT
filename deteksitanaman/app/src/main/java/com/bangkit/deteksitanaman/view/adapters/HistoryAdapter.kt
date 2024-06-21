package com.bangkit.deteksitanaman.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.deteksitanaman.data.local.DetectEntity
import com.bangkit.deteksitanaman.databinding.ItemHistoryBinding
import com.bumptech.glide.Glide

class HistoryAdapter(
    private val onItemClick: (DetectEntity) -> Unit
) : ListAdapter<DetectEntity, HistoryAdapter.HistoryViewHolder>(ScanHistoryDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemHistoryBinding.inflate(inflater, parent, false)
        return HistoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val scanHistoryItem = getItem(position)
        holder.bind(scanHistoryItem)
    }

    inner class HistoryViewHolder(private val binding: ItemHistoryBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val scanHistoryItem = getItem(position)
                    onItemClick.invoke(scanHistoryItem)
                }
            }
        }

        fun bind(scanHistoryItem: DetectEntity) {
            with(binding) {
                Glide.with(itemView.context)
                    .load(scanHistoryItem.scanImage)
                    .into(historyImage)

                name.text = scanHistoryItem.plantName
                latinName.text = scanHistoryItem.plantType
            }
        }
    }

    class ScanHistoryDiffCallback : DiffUtil.ItemCallback<DetectEntity>() {
        override fun areItemsTheSame(oldItem: DetectEntity, newItem: DetectEntity): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: DetectEntity, newItem: DetectEntity): Boolean {
            return oldItem == newItem
        }
    }
}