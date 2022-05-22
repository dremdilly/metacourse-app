package com.example.metacourse.ui.course

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.metacourse.R
import com.example.metacourse.ResourceHelper
import com.example.metacourse.databinding.FragmentCourseBinding
import com.example.metacourse.databinding.FragmentDashboardBinding
import com.example.metacourse.network.CourseModel
import com.example.metacourse.ui.course.adapter.ModuleItemAdapter
import com.example.metacourse.ui.dashboard.DashboardViewModel
import com.example.metacourse.ui.home.HomeViewModel
import com.example.metacourse.ui.home.HomeViewModelFactory
import com.example.metacourse.ui.home.adapter.CourseItemAdapter
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class CourseFragment : Fragment() {
    private var _binding: FragmentCourseBinding? = null
    private val binding get() = _binding!!
//    private var recycler: RecyclerView.Adapter<CourseItemAdapter.CourseItemViewHolder>? = null

    private val viewModel by viewModels<CourseViewModel> {
        CourseViewModelFactory(
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
        _binding = FragmentCourseBinding.inflate(inflater, container, false)
        val root: View = binding.root

        viewModel.getCourseById(arguments?.get("id") as Long)

        binding.apply {
            backBtn.setOnClickListener {
                activity?.onBackPressed()
            }
            enrollBtn.setOnClickListener {
                enrollBtn.isEnabled = false
                enrollBtn.setBackgroundColor(Color.GRAY)
            }
            allModulesBtn.setOnClickListener {
                val action = CourseFragmentDirections.actionCourseFragmentToAllModulesFragment(arguments?.get("id") as Long)
                if(findNavController().currentDestination?.id == R.id.courseFragment) {
                    findNavController().navigate(action)
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.courseEvents.collect {
                when(it) {
                    is CourseViewModel.CourseEvents.ShowResult -> {
                        showCourseById(it.result)
                    }
                }
            }
        }
        return root
    }

    private fun showCourseById(course: CourseModel) {
        binding.apply {
            courseNameTextview.text = course.title
//            courseCategoryTextview.text = course.categories[0].name
            courseDescriptionTextview.text = course.description
            courseRatingText.text = course.rating.toString()
            courseDurationText.text = course.duration

            modulesHeaderTextview.text = "${course.modules.size} modules"
            modulesRecyclerView.adapter = ModuleItemAdapter(this@CourseFragment, course.modules)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}