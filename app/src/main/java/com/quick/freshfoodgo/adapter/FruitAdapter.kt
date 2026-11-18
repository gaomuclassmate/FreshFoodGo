package com.quick.freshfoodgo.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.quick.freshfoodgo.bean.FruitBean
import com.quick.freshfoodgo.databinding.ItemFruitBinding

class FruitAdapter: RecyclerView.Adapter<FruitAdapter.MyViewHolder>() {

    private var list: List<FruitBean> = emptyList()

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
        holder.binding.fruit.setImageResource(beanItem.imgRes)
        holder.binding.type.setText(beanItem.type)
        holder.binding.des.setText(beanItem.des)
        holder.binding.price.setText("￥${beanItem.price}")
        holder.binding.addCar.setOnClickListener { it: View? ->
            //加入到购物车

        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class MyViewHolder(binding: ItemFruitBinding): RecyclerView.ViewHolder(binding.root) {
        val binding = binding
    }


}