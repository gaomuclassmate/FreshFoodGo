package com.quick.freshfoodgo.adapter

import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.quick.freshfoodgo.R
import com.quick.freshfoodgo.bean.BannerDataBean
import com.youth.banner.adapter.BannerAdapter

class ImageAdapter(
    data: MutableList<BannerDataBean> = mutableListOf()
) : BannerAdapter<BannerDataBean, ImageAdapter.BannerViewHolder>(data) {

    override fun onCreateHolder(parent: ViewGroup, viewType: Int): BannerViewHolder {
        val imageView = ImageView(parent.context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            scaleType = ImageView.ScaleType.CENTER_CROP
        }
        return BannerViewHolder(imageView)
    }

    override fun onBindView(
        holder: BannerViewHolder?,
        data: BannerDataBean?,
        position: Int,
        size: Int
    ) {
        holder?.imageView?.load(data?.imageUrl.orEmpty()) {
            placeholder(R.mipmap.banner1)
            error(R.mipmap.banner1)
            crossfade(false)
        }
    }

    class BannerViewHolder(val imageView: ImageView) : RecyclerView.ViewHolder(imageView)
}
