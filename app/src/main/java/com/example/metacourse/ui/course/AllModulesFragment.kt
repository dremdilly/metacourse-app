package com.example.metacourse.ui.course

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.metacourse.ResourceHelper
import com.example.metacourse.databinding.FragmentAllModulesBinding
import com.example.metacourse.databinding.FragmentCourseBinding
import com.example.metacourse.network.CourseModel
import com.example.metacourse.ui.course.adapter.ModuleItemAdapter
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class AllModulesFragment: Fragment() {
    private var _binding: FragmentAllModulesBinding? = null
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
        _binding = FragmentAllModulesBinding.inflate(inflater, container, false)
        val root: View = binding.root

        viewModel.getCourseById(arguments?.get("id") as Long)

        binding.apply {
            backBtn.setOnClickListener {
                activity?.onBackPressed()
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
            modulesHeaderTextview.text = "${course.modules.size} modules"
            modulesRecyclerView.adapter = ModuleItemAdapter(this@AllModulesFragment, course.modules)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}