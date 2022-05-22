package com.example.metacourse.ui.course.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.metacourse.R
import com.example.metacourse.network.CourseModel
import com.example.metacourse.network.ModuleModel
import com.example.metacourse.ui.course.AllModulesFragment
import com.example.metacourse.ui.course.AllModulesFragmentDirections
import com.example.metacourse.ui.course.CourseFragmentDirections
import com.example.metacourse.ui.home.HomeFragmentDirections
import com.example.metacourse.ui.home.adapter.CourseItemAdapter

class ModuleItemAdapter(
    private val fragment: Fragment,
    private val dataset: List<ModuleModel>?
) : RecyclerView.Adapter<ModuleItemAdapter.ModuleItemViewHolder>() {

    class ModuleItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val frameId: Int = View.generateViewId()
        val moduleItem: ConstraintLayout = view.findViewById(R.id.module_item_container) as ConstraintLayout
        init {
            moduleItem.id = frameId
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ModuleItemViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.module_item, parent, false)

        return ModuleItemViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(
        holder: ModuleItemViewHolder,
        position: Int
    ) {
        holder.moduleItem.let {
//            it.findViewById<TextView>(R.id.module_name_text).text = dataset?.get(position)?.title
//            it.findViewById<TextView>(R.id.module_description_text).text = dataset?.get(position)?.description
//            it.findViewById<TextView>(R.id.lessons_amount_text).text = "${dataset?.get(position)?.lessons?.size} lessons"

            it.setOnClickListener {
//                val action = CourseFragmentDirections.actionCourseFragmentToModuleFragment(dataset?.get(position)?.id!!)
//                val actionModules = AllModulesFragmentDirections.actionAllModulesFragmentToModuleFragment(dataset?.get(position)?.id!!)
                if(Navigation.findNavController(it).currentDestination?.id == R.id.courseFragment) {
//                    Navigation.findNavController(it).navigate(action)
                } else if(Navigation.findNavController(it).currentDestination?.id == R.id.allModulesFragment) {
//                    Navigation.findNavController(it).navigate(actionModules)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        if(fragment.findNavController().currentDestination?.id == R.id.courseFragment && dataset != null) {
            return 1
        } else if (dataset != null) {
            return dataset.size
        } else
            return 0
    }
}