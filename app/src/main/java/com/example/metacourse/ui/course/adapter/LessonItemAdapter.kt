package com.example.metacourse.ui.course.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.FragmentManager
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.metacourse.R
import com.example.metacourse.network.LessonModel
import com.example.metacourse.network.ModuleModel
import com.example.metacourse.ui.course.CourseFragmentDirections
import com.example.metacourse.ui.course.LessonFragment
import com.example.metacourse.ui.course.ModuleFragmentDirections

class LessonItemAdapter(
    private val fragmentManager: FragmentManager,
    private val dataset: List<LessonModel>?
) : RecyclerView.Adapter<LessonItemAdapter.LessonItemViewHolder>() {

    class LessonItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val frameId: Int = View.generateViewId()
        val lessonItem: ConstraintLayout = view.findViewById(R.id.lesson_item_container) as ConstraintLayout
        init {
            lessonItem.id = frameId
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): LessonItemViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.lesson_item, parent, false)

        return LessonItemViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(
        holder: LessonItemViewHolder,
        position: Int
    ) {
        holder.lessonItem.let {
            it.findViewById<TextView>(R.id.lesson_name_text).text = dataset?.get(position)?.title
            it.findViewById<TextView>(R.id.lesson_description_text).text = dataset?.get(position)?.description

            it.setOnClickListener {
                val action = ModuleFragmentDirections.actionModuleFragmentToLessonFragment(dataset?.get(position)?.id!!)
                if(Navigation.findNavController(it).currentDestination?.id == R.id.moduleFragment) {
                    Navigation.findNavController(it).navigate(action)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        if (dataset != null) {
            return dataset.size
        } else
            return 0
    }
}