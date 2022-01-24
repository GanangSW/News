package com.gsw.news.ui.sources.adapter

import android.graphics.Color
import android.graphics.Typeface
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import com.amulyakhare.textdrawable.TextDrawable
import com.gsw.news.databinding.ItemsSourcesBinding
import com.gsw.news.models.response.SourcesItem
import com.gsw.news.other.Tools.Companion.tText
import com.amulyakhare.textdrawable.util.ColorGenerator
import com.gsw.news.other.Tools.Companion.toGone
import java.util.*


class AdapterSources(private val customListeners: CustomListeners) :
    RecyclerView.Adapter<AdapterSources.VHSources>() {

    private val diffUtilItemCallback = object : DiffUtil.ItemCallback<SourcesItem>() {

        override fun areItemsTheSame(oldItem: SourcesItem, newItem: SourcesItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: SourcesItem, newItem: SourcesItem): Boolean {
            return oldItem == newItem
        }

    }

    private val listDiffer = AsyncListDiffer(this, diffUtilItemCallback)

    private lateinit var binding: ItemsSourcesBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VHSources {
        binding = ItemsSourcesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VHSources(binding, customListeners)
    }

    override fun onBindViewHolder(holder: VHSources, position: Int) {
        holder.bind(listDiffer.currentList[position])
    }

    override fun getItemCount(): Int {
        return listDiffer.currentList.size
    }

    fun submitList(list: List<SourcesItem>) {
        listDiffer.submitList(list)
    }

    class VHSources
    constructor(
        private val binding: ItemsSourcesBinding,
        private val customListeners: CustomListeners
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: SourcesItem) {

            binding.root.setOnClickListener {
                customListeners.onItemSelected(item)
            }
            binding.apply {
                item.apply {

                    val generator = ColorGenerator.MATERIAL
                    val color1 = generator.randomColor
                    val drawable = TextDrawable.builder()
                        .beginConfig()
                        .withBorder(4)
                        .endConfig()
                        .buildRoundRect(name?.take(1), color1, 10);

                    ivSource.setImageDrawable(drawable)

                    tvItemSourcesName.tText(name)
                    tvItemSourcesCategory.tText(category?.replaceFirstChar {
                        if (it.isLowerCase()) it.titlecase(
                            Locale.getDefault()
                        ) else it.toString()
                    })
                    tvItemSourcesCountry.tText("$country - $language")
                }
            }
        }
    }

    interface CustomListeners {
        fun onItemSelected(item: SourcesItem)
    }
}