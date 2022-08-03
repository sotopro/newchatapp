package com.example.newchatapp.ui.myaccount

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.newchatapp.databinding.FragmentMyaccountBinding
import com.example.newchatapp.databinding.FragmentNotificationsBinding
import com.example.newchatapp.databinding.FragmentPeopleBinding
import com.example.newchatapp.ui.myaccount.MyaccountViewModel

class MyaccountFragment : Fragment() {

    private var _binding: FragmentMyaccountBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val myaccountViewModel =
            ViewModelProvider(this).get(MyaccountViewModel::class.java)

        _binding = FragmentMyaccountBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textMyaccount
        myaccountViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}