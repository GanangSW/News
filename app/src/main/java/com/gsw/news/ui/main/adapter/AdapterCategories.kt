package com.gsw.news.ui.main.adapter

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import com.amulyakhare.textdrawable.util.ColorGenerator
import com.gsw.news.databinding.ItemsCategoriesBinding
import com.gsw.news.models.response.ModelCategories
import com.gsw.news.other.Tools.Companion.tText
import java.util.*

class AdapterCategories(private val customListeners: CustomListeners) :
    RecyclerView.Adapter<AdapterCategories.VHCategories>() {

    private val diffUtilItemCallback = object : DiffUtil.ItemCallback<ModelCategories>() {

        override fun areItemsTheSame(oldItem: ModelCategories, newItem: ModelCategories): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: ModelCategories,
            newItem: ModelCategories
        ): Boolean {
            return oldItem == newItem
        }

    }

    private val listDiffer = AsyncListDiffer(this, diffUtilItemCallback)

    private lateinit var binding: ItemsCategoriesBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VHCategories {
        binding = ItemsCategoriesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VHCategories(binding, customListeners)
    }

    override fun onBindViewHolder(holder: VHCategories, position: Int) {
        holder.bind(listDiffer.currentList[position])
    }

    override fun getItemCount(): Int {
        return listDiffer.currentList.size
    }

    fun submitList(list: List<ModelCategories>) {
        listDiffer.submitList(list)
    }

    class VHCategories
    constructor(
        private val binding: ItemsCategoriesBinding,
        private val customListeners: CustomListeners
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ModelCategories) {


            binding.root.setOnClickListener {
                customListeners.onItemSelected(item)
            }
            binding.apply {
                tvItemCategories.tText(item.categories?.replaceFirstChar {
                    if (it.isLowerCase()) it.titlecase(
                        Locale.getDefault()
                    ) else it.toString()
                })
                ivItemCategories.setImageDrawable(item.image)

//                val generator = ColorGenerator.MATERIAL
//                val color1 = generator.randomColor
//                llcItemCategories.setBackgroundColor(color1)
            }

        }
    }

    interface CustomListeners {
        fun onItemSelected(item: ModelCategories)
    }
}