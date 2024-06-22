package com.bangkit.deteksitanaman.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.deteksitanaman.data.local.DetectEntity
import com.bangkit.deteksitanaman.databinding.ItemAllHistoryBinding
import com.bumptech.glide.Glide

class AllHistoryAdapter(
    private val onItemClick: (DetectEntity) -> Unit,
    private val onDeleteClick: (DetectEntity) -> Unit
) : ListAdapter<DetectEntity, AllHistoryAdapter.AllHistoryViewHolder>(ScanHistoryDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllHistoryViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemAllHistoryBinding.inflate(inflater, parent, false)
        return AllHistoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AllHistoryViewHolder, position: Int) {
        val scanHistoryItem = getItem(position)
        holder.bind(scanHistoryItem)
    }

    inner class AllHistoryViewHolder(private val binding: ItemAllHistoryBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val scanHistoryItem = getItem(position)
                    onItemClick.invoke(scanHistoryItem)
                }
            }
            binding.deleteBtn.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val scanHistoryItem = getItem(position)
                    onDeleteClick.invoke(scanHistoryItem)
                }
            }
        }

        fun bind(scanHistoryItem: DetectEntity) {
            with(binding) {
                Glide.with(itemView.context)
                    .load(scanHistoryItem.scanImage)
                    .into(scanImageView)

                namePlant.text = scanHistoryItem.plantName
                statusPlant.text = scanHistoryItem.plantType
                descPlant.text = scanHistoryItem.plantDesc
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