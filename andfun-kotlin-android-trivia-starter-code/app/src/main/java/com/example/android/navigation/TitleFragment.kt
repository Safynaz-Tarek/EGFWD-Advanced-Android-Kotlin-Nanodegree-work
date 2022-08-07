package com.example.android.navigation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.example.android.navigation.databinding.FragmentTitleBinding



class TitleFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding : FragmentTitleBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_title,container, false)
        binding.playButton.setOnClickListener{ view: View ->
            Navigation.findNavController(view).navigate(R.id.action_titleFragment_to_gameFragment)
        }

        return binding.root
    }

}