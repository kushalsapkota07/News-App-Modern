package com.example.newsappmodern.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.newsappmodern.R
import com.example.newsappmodern.databinding.ArticleItemCardviewBinding
import com.example.newsappmodern.models.Article
import com.example.newsappmodern.models.NewsResponse

class NewsAdapterCardView: RecyclerView.Adapter<NewsAdapterCardView.ArticleItemCardViewViewHolder>() {

    inner class ArticleItemCardViewViewHolder(
        val articleItemCardViewBinding: ArticleItemCardviewBinding
    ): RecyclerView.ViewHolder(articleItemCardViewBinding.root) {

    }

    // Always use diff util instead of just the plain old recycler view adapter

    private val differCallback: DiffUtil.ItemCallback<Article> = object : DiffUtil.ItemCallback<Article>() {
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.link == newItem.link
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }

    }

    //Async list differ - it will take two lists and compares them (all in background)
    // async list differ for vertical recycler view
    val differ: AsyncListDiffer<Article> = AsyncListDiffer(this,differCallback)


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NewsAdapterCardView.ArticleItemCardViewViewHolder {
        val articleItemCardViewBinding = ArticleItemCardviewBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return ArticleItemCardViewViewHolder(articleItemCardViewBinding)
    }

    override fun onBindViewHolder(
        holder: NewsAdapterCardView.ArticleItemCardViewViewHolder,
        position: Int
    ) {

        val article : Article = differ.currentList[position]
        holder.articleItemCardViewBinding.apply {
            Glide.with(this.root).load(article.imageUrl).placeholder(R.drawable.article_placeholder_image).into(articleImage)

            articleHeadline.text = article.title
            root.setOnClickListener {
                onItemClickListener?.let { it(article) }

            }
        }

    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private var onItemClickListener: ((Article) -> Unit)? = null

    fun setOnItemClickListener (listener : (Article) -> Unit){
        onItemClickListener = listener
    }


}