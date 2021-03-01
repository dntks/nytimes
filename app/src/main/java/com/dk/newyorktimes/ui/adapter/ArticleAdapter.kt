package com.dk.newyorktimes.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.dk.newyorktimes.R
import com.dk.newyorktimes.databinding.ItemLayoutBinding
import com.dk.newyorktimes.ui.model.Article

class ArticleAdapter(val clickListener: (Article) -> Unit) :
    RecyclerView.Adapter<ArticleAdapter.MyViewHolder>() {

    private var articles = emptyList<Article>()

    inner class MyViewHolder(private val binding: ItemLayoutBinding) : RecyclerView.ViewHolder(
        binding.root
    ) {
        fun bind(article: Article) {
            binding.root.setOnClickListener { clickListener(article) }
            binding.articleTitle.text = article.headline
            binding.byLine.text = article.byLine ?: ""
            binding.imageViewAvatar.load(article.thumbnailUrl) {
                crossfade(300)
                error(R.drawable.ic_error_placeholder)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemLayoutBinding.inflate(inflater, parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentArticle = articles[position]
        holder.bind(currentArticle)
    }

    override fun getItemCount(): Int {
        return articles.size
    }

    fun setArticles(newArticles: List<Article>) {
        articles = newArticles
        notifyDataSetChanged()
    }
}