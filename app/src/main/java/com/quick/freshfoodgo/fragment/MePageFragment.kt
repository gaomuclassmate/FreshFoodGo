package com.quick.freshfoodgo.fragment

import android.os.Bundle
import android.view.LayoutInflater
import com.quick.freshfoodgo.base.BaseFragment
import com.quick.freshfoodgo.databinding.FragmentMePageBinding

class MePageFragment: BaseFragment<FragmentMePageBinding>() {
    override fun inflateBinding(inflater: LayoutInflater): FragmentMePageBinding {
       return FragmentMePageBinding.inflate(inflater)
    }


    companion object {
        fun instance():MePageFragment{
            val fragment = MePageFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            return fragment

        }
    }
}