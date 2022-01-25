package com.gsw.news.ui.search.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.gsw.news.databinding.ItemsArticlesBinding
import com.gsw.news.models.response.ArticlesItem
import com.gsw.news.other.Tools.Companion.dateFormat
import com.gsw.news.other.Tools.Companion.loadImageGlide
import com.gsw.news.other.Tools.Companion.tText

class AdapterSearch(private val customListeners: CustomListeners) :
    RecyclerView.Adapter<AdapterSearch.VHSearch>() {

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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VHSearch {
        binding = ItemsArticlesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VHSearch(binding, customListeners)
    }

    override fun onBindViewHolder(holder: VHSearch, position: Int) {
        holder.bind(listDiffer.currentList[position])
    }

    override fun getItemCount(): Int {
        return listDiffer.currentList.size
    }

    fun submitList(list: List<ArticlesItem>) {
        listDiffer.submitList(list)
    }

    class VHSearch
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
                    if (source != null) {
                        tvItemArticleSource.tText(source.name)
                    }
                    tvItemArticlePublishAt.tText(dateFormat(publishedAt))
                }
            }
        }
    }

    interface CustomListeners {
        fun onItemSelected(item: ArticlesItem)
    }
}