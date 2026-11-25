package com.quick.freshfoodgo.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.quick.freshfoodgo.R
import com.quick.freshfoodgo.bean.CarDataBean
import com.quick.freshfoodgo.databinding.ItemBuyCarBinding

class MyCarAdapter(var listData: List<CarDataBean>): RecyclerView.Adapter<MyCarAdapter.MyHolder>() {


    fun setData(newList: List<CarDataBean>){
        listData = newList
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(
        p0: ViewGroup,
        p1: Int
    ): MyHolder {
        val binding = ItemBuyCarBinding.inflate(LayoutInflater.from(p0.context), p0, false)
        return MyHolder(binding)

    }

    override fun onBindViewHolder(
        holder: MyHolder,
        posiotion: Int
    ) {
        val item = listData[posiotion]

        holder.binding.apply {
            img.load(item.imgUrl){
                placeholder(R.mipmap.imgloading)
                crossfade(false)
            }
            name.text = item.name
            count.text = item.currentCount.toString()

        }

    }

    override fun getItemCount(): Int {
        return listData.size
    }


    inner class MyHolder(val binding: ItemBuyCarBinding): RecyclerView.ViewHolder(binding.root){}
}