package com.quick.freshfoodgo.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.quick.freshfoodgo.R
import com.quick.freshfoodgo.bean.FruitBean
import com.quick.freshfoodgo.databinding.ItemCategoryProductBinding
import java.util.Locale

class CategoryProductAdapter : RecyclerView.Adapter<CategoryProductAdapter.MyHolder>() {

    private var products: List<FruitBean> = emptyList()
    private var onClickListener: ((String) -> Unit)? = null

    fun setData(newProducts: List<FruitBean>) {
        products = newProducts
        notifyDataSetChanged()
    }

    fun setOnProductClickListener(listener: (String) -> Unit) {
        onClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val binding = ItemCategoryProductBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MyHolder(binding)
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        val product = products[position]
        holder.binding.productImage.load(product.imageUrl) {
            placeholder(R.mipmap.food_display)
            error(R.mipmap.food_display)
            crossfade(false)
        }
        holder.binding.productName.text = product.type
        holder.binding.productUnit.text = product.unit
        holder.binding.productPrice.text = String.format(
            Locale.CHINA,
            "¥%s",
            product.price
        )
        holder.binding.root.setOnClickListener {
            onClickListener?.invoke(product.id)
        }
    }

    override fun getItemCount(): Int = products.size

    class MyHolder(val binding: ItemCategoryProductBinding) :
        RecyclerView.ViewHolder(binding.root)
}
