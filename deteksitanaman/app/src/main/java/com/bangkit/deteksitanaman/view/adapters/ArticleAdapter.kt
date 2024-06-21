package com.bangkit.deteksitanaman.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.deteksitanaman.databinding.ItemArticleBinding
import com.bumptech.glide.Glide

class ArticleAdapter(private val slides: List<Article>) : RecyclerView.Adapter<ArticleAdapter.ViewPagerViewHolder>() {

    class ViewPagerViewHolder(val binding: ItemArticleBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPagerViewHolder {
        val binding = ItemArticleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewPagerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewPagerViewHolder, position: Int) {
        val slide = slides[position]
        with(holder.binding) {
            Glide.with(holder.itemView.context)
                .load(slide.image)
                .into(articleImage)

            articleText.text = slide.desc
        }
    }

    override fun getItemCount() = slides.size
}

data class Article(
    val image: Int,
    val desc: String,
)