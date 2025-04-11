package com.example.cs388_group12_stockpath.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.fragment.app.activityViewModels
import com.example.cs388_group12_stockpath.GlobalUserView
import com.example.cs388_group12_stockpath.databinding.FragmentHomeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class HomeFragment : Fragment() {
    //*********************************************************************
    //User Login Section
    private val globalUserViewModel: GlobalUserView by activityViewModels()
    //*********************************************************************

    private var _binding: FragmentHomeBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textHome
        homeViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }


        //*********************************************************************
        //User Login Section
        var uid: String
        var email: String
        globalUserViewModel.user.observe(viewLifecycleOwner) {
            if (it == null) {
                uid = "Guest"
                email = "Guest@StockPath"
            } else {
                uid = it.uid
                email = it.email ?: "Guest@StockPath"
            }

            Log.d("HomeFragment", "Welcome to StockPath! User ID: ${uid} Email: ${email}")
        }
        //----textview section----


        //*********************************************************************

//        val auth = FirebaseAuth.getInstance()
//        val user: FirebaseUser? = auth.getCurrentUser()
//        val isGuest = user == null // true if user is a guest/not logged in | false if user is logged in
//        val uid = user?.uid ?: "Guest"
//        val email = user?.email ?: "Guest@StockPath"
//        Log.d("HomeFragment", "Welcome to StockPath! User ID: $uid Email: $email")
    //    if(!isGuest){
    //        uid = user.uid
    //    } else {
    //        uid = "Guest"
    //    }
        return root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}