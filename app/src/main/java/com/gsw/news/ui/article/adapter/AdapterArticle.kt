package com.gsw.news.ui.article.adapter

import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.gsw.news.databinding.ItemsArticlesBinding
import com.gsw.news.models.response.ArticlesItem
import com.gsw.news.other.Tools.Companion.DateFormat
import com.gsw.news.other.Tools.Companion.loadImageGlide
import com.gsw.news.other.Tools.Companion.tText

class AdapterArticle(private val customListeners: CustomListeners) :
    RecyclerView.Adapter<AdapterArticle.VHArticle>() {

    private val diffUtilItemCallback = object : DiffUtil.ItemCallback<ArticlesItem>() {

        override fun areItemsTheSame(oldItem: ArticlesItem, newItem: ArticlesItem): Boolean {
            return oldItem.title == newItem.title
        }

        override fun areContentsTheSame(oldItem: ArticlesItem, newItem: ArticlesItem): Boolean {
            return oldItem == newItem
        }

    }

    private val listDiffer = AsyncListDiffer(this, diffUtilItemCallback)

    private lateinit var binding: ItemsArticlesBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VHArticle {
        binding = ItemsArticlesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VHArticle(binding, customListeners)
    }

    override fun onBindViewHolder(holder: VHArticle, position: Int) {
        holder.bind(listDiffer.currentList[position])
    }

    override fun getItemCount(): Int {
        return listDiffer.currentList.size
    }

    fun submitList(list: List<ArticlesItem>) {
        listDiffer.submitList(list)
    }

    class VHArticle
    constructor(
        private val binding: ItemsArticlesBinding,
        private val customListeners: CustomListeners
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ArticlesItem) {

            binding.root.setOnClickListener {
                customListeners.onItemSelected(item)
            }
            binding.apply {
                item.apply {
                    ivItemArticle.loadImageGlide(root.context, urlToImage)
                    tvItemArticleTitle.tText(title)
                    tvItemArticleAuthor.tText("author : $author")
                    tvItemArticlePublishAt.tText(DateFormat(publishedAt))
                }
            }
        }
    }

    interface CustomListeners {
        fun onItemSelected(item: ArticlesItem)
    }
}