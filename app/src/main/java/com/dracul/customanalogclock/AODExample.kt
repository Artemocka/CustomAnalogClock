package com.dracul.customanalogclock

import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.dracul.customanalogclock.databinding.FragmentAodExampleBinding


class AODExample : Fragment() {
    private lateinit var binding: FragmentAodExampleBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAodExampleBinding.inflate(layoutInflater)

        binding.second.setOnClickListener {
            findNavController().navigate(R.id.homeWidgetExample)
        }
        binding.third.setOnClickListener {
            findNavController().navigate(R.id.timeZoneExample)
        }

        return binding.root
    }


}