package com.arkan.a4crypto.presentation.favorite.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.arkan.a4crypto.data.model.Favorite
import com.arkan.a4crypto.databinding.ItemFavoriteListBinding
import com.arkan.aresto.utils.formatToIDRCurrency

class FavoriteListAdapter(private val favoriteListener: FavoriteListener? = null) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val dataDiffer =
        AsyncListDiffer(
            this,
            object : DiffUtil.ItemCallback<Favorite>() {
                override fun areItemsTheSame(
                    oldItem: Favorite,
                    newItem: Favorite,
                ): Boolean {
                    return oldItem.id == newItem.id && oldItem.id == newItem.id
                }

                override fun areContentsTheSame(
                    oldItem: Favorite,
                    newItem: Favorite,
                ): Boolean {
                    return oldItem.hashCode() == newItem.hashCode()
                }
            },
        )

    fun submitData(data: List<Favorite>) {
        dataDiffer.submitList(data)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): RecyclerView.ViewHolder {
        return if (favoriteListener != null) {
            FavoriteViewHolder(
                ItemFavoriteListBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false,
                ),
                favoriteListener,
            )
        } else {
            FavoriteViewHolder(
                ItemFavoriteListBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false,
                ),
                favoriteListener,
            )
        }
    }

    override fun getItemCount(): Int = dataDiffer.currentList.size

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
    ) {
        (holder as ViewHolderBinder<Favorite>).bind(dataDiffer.currentList[position])
    }
}

class FavoriteViewHolder(
    private val binding: ItemFavoriteListBinding,
    private val favoriteListener: FavoriteListener?,
) : RecyclerView.ViewHolder(binding.root), ViewHolderBinder<Favorite> {
    override fun bind(item: Favorite) {
        setCartData(item)
        setClickListeners(item)
    }

    private fun setCartData(item: Favorite) {
        with(binding) {
            binding.ivImgItemFavorite.load(item.catalogImgUrl) {
                crossfade(true)
            }
            tvItemFavoriteTitle.text = item.catalogName
            tvItemFavoritePrice.text = item.catalogPrice.formatToIDRCurrency()
        }
    }

    private fun setClickListeners(item: Favorite) {
        with(binding) {
            ivRemoveFavorite.setOnClickListener { favoriteListener?.onRemoveCartClicked(item) }
        }
    }
}

interface FavoriteListener {
    fun onRemoveCartClicked(cart: Favorite)
}

interface ViewHolderBinder<T> {
    fun bind(item: T)
}
