package com.quick.freshfoodgo.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding

abstract class BaseFragment<VB: ViewBinding>: Fragment() {


    private var _binding:VB? = null
    protected val binding :VB
        get() = _binding!!

    protected val instance :Context
        get() = requireContext()



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = inflateBinding(inflater)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initView()
        initAction()
        initListener()
    }

    open fun initAction(){

    }

    open fun initView(){

    }
    open fun initListener(){

    }

    abstract fun inflateBinding(inflater: LayoutInflater):VB
}