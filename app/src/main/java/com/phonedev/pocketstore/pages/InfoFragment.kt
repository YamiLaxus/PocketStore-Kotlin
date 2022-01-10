package com.phonedev.pocketstore.pages

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.phonedev.pocketstore.R
import com.phonedev.pocketstore.databinding.FragmentInfoBinding

class InfoFragment : Fragment() {

    private var binding: FragmentInfoBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentInfoBinding.inflate(inflater, container, false)
        binding?.let {
            it.root
        }
        return inflater.inflate(R.layout.fragment_info, container, false)
    }
}