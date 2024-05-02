package com.arkan.a4crypto.presentation.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.arkan.a4crypto.data.model.Coin
import com.arkan.a4crypto.databinding.ItemHomeListCoinBinding
import com.arkan.aresto.utils.toDollarFormat

class CoinAdapter(
    private val listener: OnItemCLickedListener<Coin>,
) : RecyclerView.Adapter<CoinAdapter.CoinViewHolder>() {
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
            ItemHomeListCoinBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            ),
            listener,
        )
    }

    override fun getItemCount(): Int = asyncDataDiffer.currentList.size

    override fun onBindViewHolder(
        holder: CoinViewHolder,
        position: Int,
    ) {
        holder.bind(asyncDataDiffer.currentList[position])
    }

    class CoinViewHolder(
        private val binding: ItemHomeListCoinBinding,
        private val listener: OnItemCLickedListener<Coin>,
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Coin) {
            binding.ivImgItemHome.load(item.image)
            binding.tvItemHomeTitle.text = item.name
            binding.tvItemHomeDesk.text = item.desc
            binding.tvItemHomePrice.text = item.price.toDollarFormat()
            itemView.setOnClickListener {
                listener.onItemClicked(item)
            }
        }
    }
}

interface OnItemCLickedListener<T> {
    fun onItemClicked(item: T)
}
