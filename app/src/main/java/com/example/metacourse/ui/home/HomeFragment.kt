package com.example.metacourse.ui.home

import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.example.metacourse.PreferenceManager
import com.example.metacourse.R
import com.example.metacourse.ResourceHelper
import com.example.metacourse.databinding.FragmentHomeBinding
import com.example.metacourse.network.CourseModel
import com.example.metacourse.network.NetworkApi
import com.example.metacourse.network.RegisterData
import com.example.metacourse.ui.auth.SignupViewModel
import com.example.metacourse.ui.home.adapter.CourseItemAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber

class HomeFragment : Fragment(R.layout.fragment_home) {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val ARGS_SCROLL_STATE = "recyclerState"
    private var recyclerViewState: Parcelable? = null
    private var listState: Parcelable? = null
    private var recycler: RecyclerView.Adapter<CourseItemAdapter.CourseItemViewHolder>? = null

    private val viewModel by viewModels<HomeViewModel> {
        HomeViewModelFactory(
            ResourceHelper(
                requireContext()
            )
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        viewModel.getAllCourses()


        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.homeEvents.collect {
                when(it) {
                    is HomeViewModel.HomeEvents.ShowResult -> {
                        showAllCourses(it.result)
                    }
                }
            }
        }
        return root
    }

    private fun showAllCourses(allCoursesList: List<CourseModel>?) {
        binding.apply {
            if(allCoursesList?.isNotEmpty() == true) {
                modulesRecycler.adapter = CourseItemAdapter(requireActivity().supportFragmentManager, allCoursesList)
                recycler = modulesRecycler.adapter as CourseItemAdapter
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}