package com.quick.freshfoodgo.fragment

import android.os.Bundle
import android.view.LayoutInflater
import com.quick.freshfoodgo.base.BaseFragment
import com.quick.freshfoodgo.databinding.FragmentFoundPageBinding

class FoundPageFragment: BaseFragment<FragmentFoundPageBinding>() {
    override fun inflateBinding(inflater: LayoutInflater): FragmentFoundPageBinding {
        return FragmentFoundPageBinding.inflate(inflater)
    }


    companion object {
        fun instance():FoundPageFragment{
            val fragment = FoundPageFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            return fragment

        }
    }

}