package com.quick.freshfoodgo.fragment

import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.quick.freshfoodgo.adapter.MyCarAdapter
import com.quick.freshfoodgo.base.BaseFragment
import com.quick.freshfoodgo.databinding.FragmentCarPageBinding
import com.quick.freshfoodgo.viewmodel.CarPageViewModel
import com.quick.freshfoodgo.viewmodel.FoodDetailViewModel
import com.quick.freshfoodgo.viewmodel.VmFactory
import kotlinx.coroutines.launch
import kotlin.apply
import kotlin.getValue

class CarPageFragment: BaseFragment<FragmentCarPageBinding>() {


    private val viewModel: CarPageViewModel by viewModels {
        VmFactory { CarPageViewModel("") }
    }

    override fun inflateBinding(inflater: LayoutInflater): FragmentCarPageBinding {
        return FragmentCarPageBinding.inflate(inflater)
    }

    override fun initAction() {
        super.initAction()
    }


    override fun initListener() {
        super.initListener()
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){

            }
        }




    }


    override fun initView() {
        super.initView()


//        initAdapter()

    }

//    private fun initAdapter(): MyCarAdapter {
//
//
//        return MyCarAdapter().apply {
//
//        }
//
//    }

    companion object {
        fun instance():CarPageFragment{
            val fragment = CarPageFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            return fragment

        }
    }
}