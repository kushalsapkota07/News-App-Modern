package com.example.newsappmodern.adapters


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.newsappmodern.R
import com.example.newsappmodern.databinding.ArticleItemCollapsedBinding
import com.example.newsappmodern.models.Article

class NewsAdapterCollapsed: RecyclerView.Adapter<NewsAdapterCollapsed.ArticleItemCollapsedViewHolder>() {

    inner class ArticleItemCollapsedViewHolder(
         val articleItemCollapsedBinding: ArticleItemCollapsedBinding
    ): RecyclerView.ViewHolder(articleItemCollapsedBinding.root) {

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
    ): NewsAdapterCollapsed.ArticleItemCollapsedViewHolder {
        val articleItemCollapsedBinding = ArticleItemCollapsedBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return ArticleItemCollapsedViewHolder(articleItemCollapsedBinding)
    }

    override fun onBindViewHolder(
        holder: NewsAdapterCollapsed.ArticleItemCollapsedViewHolder,
        position: Int
    ) {

        val article : Article = differ.currentList[position]
        holder.articleItemCollapsedBinding.apply {
            Glide.with(this.root).load(article.imageUrl).placeholder(R.drawable.article_placeholder_image).into(articleImage)

            articleHeadline.text = article.title
            articleDescription.text = article.description
            articlePublishedDate.text = article.pubDate
            root.setOnClickListener { v ->
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