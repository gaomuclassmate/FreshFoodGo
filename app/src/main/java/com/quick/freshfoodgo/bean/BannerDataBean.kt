package com.quick.freshfoodgo.bean

import com.quick.freshfoodgo.R

data class BannerDataBean(
   var imageRes: Int
) {
    companion object {
        fun getTestData(): MutableList<BannerDataBean?>? {
            val list = mutableListOf<BannerDataBean?>()
            list.add(BannerDataBean(R.mipmap.banner1))
            list.add(BannerDataBean(R.mipmap.banner1))
            list.add(BannerDataBean(R.mipmap.banner1))

            return list

        }
    }
}