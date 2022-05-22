package com.example.metacourse.ui.course

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.metacourse.ResourceHelper
import com.example.metacourse.databinding.FragmentCourseBinding
import com.example.metacourse.databinding.FragmentModuleBinding
import com.example.metacourse.network.CourseModel
import com.example.metacourse.network.ModuleModel
import com.example.metacourse.ui.course.adapter.LessonItemAdapter
import com.example.metacourse.ui.course.adapter.ModuleItemAdapter
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ModuleFragment : Fragment() {
    private var _binding: FragmentModuleBinding? = null
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
        _binding = FragmentModuleBinding.inflate(inflater, container, false)
        val root: View = binding.root

        viewModel.getModuleById(arguments?.get("id") as Long)

        binding.apply {
            backBtn.setOnClickListener {
                activity?.onBackPressed()
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.courseEvents.collect {
                when(it) {
                    is CourseViewModel.CourseEvents.ShowModuleResult -> {
                        showModuleById(it.result)
                    }
                }
            }
        }
        return root
    }

    private fun showModuleById(module: ModuleModel) {
        binding.apply {
            moduleNameTextview.text = module.title
            moduleDescriptionTextview.text = module.description
            lessonsHeaderTextview.text = "${module.lessons.size} lessons"
            lessonsRecyclerView.adapter = LessonItemAdapter(requireActivity().supportFragmentManager, module.lessons)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}