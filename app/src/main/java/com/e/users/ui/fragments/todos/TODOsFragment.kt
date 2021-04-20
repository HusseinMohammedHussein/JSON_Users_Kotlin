package com.e.users.ui.fragments.todos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.e.users.R
import com.e.users.databinding.FragmentTodosBinding
import com.e.users.ui.activity.main.MainActivity
import com.e.users.utils.SharedPref
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class TODOsFragment @Inject constructor() : Fragment() {
    private var viewModel: TODOsViewModel = TODOsViewModel()
    private lateinit var binding: FragmentTodosBinding

    private val todoAdapter: TODOsAdapter = TODOsAdapter()
    private lateinit var sharedPref: SharedPref

    private var getUserId: Int = 0

    companion object {
        fun newInstance() = TODOsFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTodosBinding.inflate(inflater, container, false)
        init()
        return binding.root
    }

    private fun init() {
        setupToolbar()
        sharedPref = SharedPref(this.requireContext())
        getUserId = sharedPref.getInt("userId")
        getTodos()
        binding.rvTodos.run {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
        }

    }

    private fun setupToolbar() {
        (activity as MainActivity).setSupportActionBar(binding.appbar.toolbar)
        (activity as MainActivity).supportActionBar?.setDisplayShowTitleEnabled(false)
        (activity as MainActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
        binding.appbar.tvTitleApp.text = "User TODOs"
        binding.appbar.toolbar.setNavigationIcon(R.drawable.btn_back_toolbar)
        binding.appbar.toolbar.setNavigationOnClickListener { (activity as MainActivity).supportFragmentManager.popBackStack() }
    }

    private fun getTodos() {
        viewModel.getTodos(getUserId).observe(this.requireActivity(), {
            todoAdapter.setData(it)
            binding.rvTodos.adapter = todoAdapter
            todoAdapter.notifyDataSetChanged()
        })
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(TODOsViewModel::class.java)
    }
}