package com.example.githubuser.ui.viewbinding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.githubuser.R
import com.example.githubuser.databinding.FragmentSampleViewBindingBinding

class SampleViewBindingFragment : Fragment() {

    private var _binding: FragmentSampleViewBindingBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSampleViewBindingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val buttonExample = view.findViewById<Button>(R.id.btn_example)
        val imageExample = view.findViewById<ImageView>(R.id.iv_example)
        val tvExampleOne = view.findViewById<TextView>(R.id.tv_example_one)
        val tvExampleTwo = view.findViewById<TextView>(R.id.tv_example_two)
        buttonExample.setOnClickListener { }
        imageExample.setImageResource(0)
        tvExampleOne.text = ""
        tvExampleTwo.text = ""


        with(binding) {
            btnExample.setOnClickListener {}
            ivExample.setImageResource(0)
            tvExampleOne.text = ""
            tvExampleTwo.text = " "
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}