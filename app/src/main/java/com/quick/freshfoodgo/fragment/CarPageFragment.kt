package com.quick.freshfoodgo.fragment

import android.os.Bundle
import android.view.LayoutInflater
import com.quick.freshfoodgo.base.BaseFragment
import com.quick.freshfoodgo.databinding.FragmentCarPageBinding

class CarPageFragment: BaseFragment<FragmentCarPageBinding>() {
    override fun inflateBinding(inflater: LayoutInflater): FragmentCarPageBinding {
        return FragmentCarPageBinding.inflate(inflater)
    }

    companion object {
        fun instance():CarPageFragment{
            val fragment = CarPageFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            return fragment

        }
    }
}