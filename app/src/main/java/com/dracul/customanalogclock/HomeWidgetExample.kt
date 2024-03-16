package com.dracul.customanalogclock

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dracul.customanalogclock.databinding.FragmentHomeWidgetExampleBinding


class HomeWidgetExample : Fragment() {

    lateinit var binding: FragmentHomeWidgetExampleBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeWidgetExampleBinding.inflate(layoutInflater)
        return binding.root
    }


}