package com.quick.freshfoodgo.fragment

import android.os.Bundle
import android.view.LayoutInflater
import com.quick.freshfoodgo.base.BaseFragment
import com.quick.freshfoodgo.databinding.FragmentCollectPageBinding

class CollectPageFragment: BaseFragment<FragmentCollectPageBinding>() {
    override fun inflateBinding(inflater: LayoutInflater): FragmentCollectPageBinding {
        return FragmentCollectPageBinding.inflate(inflater)
    }

    companion object {
        fun instance():CollectPageFragment{
            val fragment = CollectPageFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            return fragment

        }
    }
}