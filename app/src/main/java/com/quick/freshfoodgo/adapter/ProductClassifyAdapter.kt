package com.quick.freshfoodgo.adapter

import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.graphics.toColorInt
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.quick.freshfoodgo.R
import com.quick.freshfoodgo.bean.ProductClassify
import com.quick.freshfoodgo.databinding.ItemClassifyBinding

class ProductClassifyAdapter : RecyclerView.Adapter<ProductClassifyAdapter.MyHolder>() {

    private var classifyList: List<ProductClassify> = emptyList()
    private var onClickListener: ((ProductClassify) -> Unit)? = null

    fun setData(newList: List<ProductClassify>) {
        classifyList = newList
        notifyDataSetChanged()
    }

    fun setOnClickListener(listener: (ProductClassify) -> Unit) {
        onClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val binding = ItemClassifyBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MyHolder(binding)
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        val item = classifyList[position]
        holder.binding.img.load(item.imageUrl) {
            placeholder(R.mipmap.food_display)
            error(R.mipmap.food_display)
            crossfade(false)
        }
        holder.binding.name.text = item.name

        val background = ContextCompat.getDrawable(
            holder.itemView.context,
            R.drawable.product_classify
        )?.mutate() as GradientDrawable
        background.setColor(item.bgColor.toColorInt())
        holder.binding.root.background = background
        holder.binding.root.setOnClickListener {
            onClickListener?.invoke(item)
        }
    }

    override fun getItemCount(): Int = classifyList.size

    class MyHolder(val binding: ItemClassifyBinding) :
        RecyclerView.ViewHolder(binding.root)
}
