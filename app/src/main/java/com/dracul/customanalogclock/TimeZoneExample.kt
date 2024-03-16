package com.dracul.customanalogclock

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dracul.customanalogclock.databinding.FragmentTimeZoneExampleBinding
import java.util.TimeZone


class TimeZoneExample : Fragment() {

    private lateinit var binding: FragmentTimeZoneExampleBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTimeZoneExampleBinding.inflate(layoutInflater)

        if (savedInstanceState == null) {
            binding.londonClock.timeZone = TimeZone.getTimeZone("GMT+1:00")
            binding.moscowClock.timeZone = TimeZone.getTimeZone("GMT+3:00")
            binding.tokyoClock.timeZone = TimeZone.getTimeZone("GMT+9:00")
        }

        return binding.root
    }


}