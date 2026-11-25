package com.quick.freshfoodgo.adapter

import android.graphics.Color
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.text.style.StrikethroughSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.quick.freshfoodgo.R
import com.quick.freshfoodgo.bean.FruitBean
import com.quick.freshfoodgo.databinding.ItemFruitBinding

class FruitAdapter: RecyclerView.Adapter<FruitAdapter.MyViewHolder>() {

    private var list: List<FruitBean> = emptyList()
    private var clickListener: OnClickListener? = null


    fun setData(newList: List<FruitBean>) {
        list = newList
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(
        p0: ViewGroup,
        p1: Int
    ): MyViewHolder {
        val binding = ItemFruitBinding.inflate(LayoutInflater.from(p0.context), p0, false)
        return MyViewHolder(binding)

    }


    override fun onBindViewHolder(
        holder: MyViewHolder,
        position: Int
    ) {
        val beanItem = list[position]
        Log.d("ly", "beanItem:${beanItem} ")
        if (beanItem.imageUrl.isNotBlank()) {
            holder.binding.fruit.load(beanItem.imageUrl) {
                crossfade(false)
                error(beanItem.resolvedImageRes())
            }
        } else {
            holder.binding.fruit.setImageResource(beanItem.resolvedImageRes())
        }
        holder.binding.type.setText(beanItem.type)
        holder.binding.des.text = if (beanItem.badge.isBlank()) {
            beanItem.des
        } else {
            "${beanItem.des} · ${beanItem.badge}"
        }
        holder.binding.price.text = beanItem.priceText()
        holder.binding.addCar.setOnClickListener { it: View? ->
            //加入到购物车



        }
        holder.binding.root.setOnClickListener { it: View? ->
            this.clickListener?.let { it: OnClickListener ->
                it.clickEnter(beanItem.id)
            }
        }
    }
    fun setOnClickListener(listener: OnClickListener){
        this.clickListener = listener
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class MyViewHolder(binding: ItemFruitBinding): RecyclerView.ViewHolder(binding.root) {
        val binding = binding
    }



    interface OnClickListener{
        fun clickEnter(foodId: String)
    }

    private fun FruitBean.resolvedImageRes(): Int {
        if (imgRes != 0) return imgRes

        return when (imageKey) {
            "banana" -> R.mipmap.bannaner
            "apple" -> R.mipmap.pingguo
            "pepper" -> R.mipmap.lajiao
            "ginger" -> R.mipmap.shengjiang
            else -> R.mipmap.food_display
        }
    }

    private fun FruitBean.priceText(): CharSequence {
        val currentPrice = "￥$price"
        val original = originalPrice?.takeIf { it.isNotBlank() } ?: return currentPrice
        val fullText = "$currentPrice  ￥$original"
        val originalStart = currentPrice.length + 1

        return SpannableString(fullText).apply {
            setSpan(
                ForegroundColorSpan(Color.parseColor("#7C7C7C")),
                originalStart,
                length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            setSpan(
                StrikethroughSpan(),
                originalStart,
                length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
    }

}
