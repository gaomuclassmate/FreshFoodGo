package com.quick.freshfoodgo.fragment

import android.os.Bundle
import android.view.LayoutInflater
import com.quick.freshfoodgo.base.BaseFragment
import com.quick.freshfoodgo.databinding.FragmentHomePageBinding

class HomePageFragment: BaseFragment<FragmentHomePageBinding>() {


    override fun inflateBinding(inflater: LayoutInflater): FragmentHomePageBinding {
        return FragmentHomePageBinding.inflate(inflater)
    }

    override fun initView() {
        super.initView()
    }

    companion object {
        fun instance():HomePageFragment{
            val fragment = HomePageFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            return fragment

        }
    }


}