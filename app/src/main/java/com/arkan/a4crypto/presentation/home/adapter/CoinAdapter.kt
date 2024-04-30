package com.arkan.a4crypto.presentation.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.arkan.a4crypto.data.model.Coin
import com.arkan.a4crypto.databinding.ItemHomeListCointBinding
import com.arkan.aresto.utils.toDollarFormat

class CoinAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val asyncDataDiffer =
        AsyncListDiffer(
            this,
            object : DiffUtil.ItemCallback<Coin>() {
                override fun areItemsTheSame(
                    oldItem: Coin,
                    newItem: Coin,
                ): Boolean {
                    return oldItem.id == newItem.id
                }

                override fun areContentsTheSame(
                    oldItem: Coin,
                    newItem: Coin,
                ): Boolean {
                    return oldItem.hashCode() == newItem.hashCode()
                }
            },
        )

    fun submitData(items: List<Coin>) {
        asyncDataDiffer.submitList(items)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): CoinViewHolder {
        return CoinViewHolder(
            ItemHomeListCointBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            ),
        )
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
    ) {
        (holder as ViewHolderBinder<Coin>).bind(asyncDataDiffer.currentList[position])
    }

    override fun getItemCount(): Int = asyncDataDiffer.currentList.size

    class CoinViewHolder(private val binding: ItemHomeListCointBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Coin) {
            binding.ivImgItemHome.load(item.image)
            binding.tvItemHomeTitle.text = item.name
            binding.tvItemHomeDesk.text = item.desc
            binding.tvItemHomePrice.text = item.price.toDollarFormat()
        }
    }
}

interface ViewHolderBinder<T> {
    fun bind(item: T)
}
