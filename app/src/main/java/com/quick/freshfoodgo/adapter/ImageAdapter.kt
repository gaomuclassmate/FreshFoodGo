package com.quick.freshfoodgo.adapter

import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.quick.freshfoodgo.bean.BannerDataBean
import com.youth.banner.adapter.BannerAdapter

class ImageAdapter(mDatas: MutableList<BannerDataBean?>?) : BannerAdapter<BannerDataBean?, ImageAdapter.BannerViewHolder?>(mDatas) {


    //创建ViewHolder，可以用viewType这个字段来区分不同的ViewHolder
    override fun onCreateHolder(parent: ViewGroup, viewType: Int): BannerViewHolder {
        val imageView = ImageView(parent.getContext())

        imageView.setLayoutParams(
            ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        )
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP)
        return BannerViewHolder(imageView)
    }

    override fun onBindView(
        holder: BannerViewHolder?,
        data: BannerDataBean?,
        position : Int,
        size : Int
    ) {
        holder?.imageView?.setImageResource(data!!.imageRes);
    }


    class BannerViewHolder(var imageView: ImageView) : RecyclerView.ViewHolder(
        imageView
    )
}