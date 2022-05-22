package com.example.metacourse.ui.home.adapter

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.FragmentManager
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.metacourse.R
import com.example.metacourse.network.CourseModel
import com.example.metacourse.ui.home.CourseItemFragment
import com.example.metacourse.ui.home.HomeFragmentDirections
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class CourseItemAdapter(
    private val context: Context,
    private val dataset: List<CourseModel>?
) : RecyclerView.Adapter<CourseItemAdapter.CourseItemViewHolder>() {

    class CourseItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val frameId: Int = View.generateViewId()
        val courseItem: ConstraintLayout = view.findViewById(R.id.course_item_container) as ConstraintLayout
        init {
            courseItem.id = frameId
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CourseItemViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.course_item, parent, false)

        return CourseItemViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(
        holder: CourseItemViewHolder,
        position: Int
    ) {

        holder.courseItem.let {
            it.findViewById<TextView>(R.id.course_name_text).text = dataset?.get(position)?.title
            it.findViewById<TextView>(R.id.course_author_text).text = dataset?.get(position)?.duration
//            it.findViewById<TextView>(R.id.course_category_text).text = dataset?.get(position)?.categories?.get(0)?.name
//            it.findViewById<TextView>(R.id.course_description_text).text = dataset?.get(position)?.description?.substring(0, 70) + "..."
            it.findViewById<TextView>(R.id.course_rating_text).text = dataset?.get(position)?.rating.toString()
            it.findViewById<TextView>(R.id.course_duration_text).text = dataset?.get(position)?.duration.toString()
//            it.findViewById<TextView>(R.id.course_auditory_text).text = dataset?.get(position)?.modules?.get(0)?.title
//            it.findViewById<TextView>(R.id.course_item_container).text = dataset?.get(position)?.modules?.size.toString()

            it.setOnClickListener {
//            val fragment = CourseItemFragment.newInstance(
//                dataset?.get(position)?.id,
//                dataset?.get(position)?.title,
//                dataset?.get(position)?.description,
//                dataset?.get(position)?.duration,
//                dataset?.get(position)?.categories,
//                dataset?.get(position)?.rating,
//                dataset?.get(position)?.modules
//            )
//
//            val fragmentTransaction = fragmentManager.beginTransaction()
//            fragmentTransaction.replace(holder.frameId, fragment)
//                .commit()
                val action = HomeFragmentDirections.actionNavigationHomeToCourseFragment(dataset?.get(position)?.id!!)
                if(Navigation.findNavController(it).currentDestination?.id == R.id.navigation_home) {
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