package com.example.pract_4_hojaeva

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ToDoList : Fragment(R.layout.activity_to_do_list) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tvListOutput = view.findViewById<TextView>(R.id.tvList)
        val btnBack = view.findViewById<Button>(R.id.btnBack)
        val sharedPreferences = requireContext().getSharedPreferences("Data", Context.MODE_PRIVATE)
        val gson = Gson()

        val jsonTasks = sharedPreferences.getString("TASKS_LIST", null)

        val taskList: ArrayList<Task> = if (jsonTasks == null) {
            ArrayList()
        } else {
            val type = object : TypeToken<ArrayList<Task>>() {}.type
            gson.fromJson(jsonTasks, type)
        }

        if (taskList.isEmpty()) {
            tvListOutput.text = getString(R.string.tvEmptyList)
        } else {
            val stringBuilder = StringBuilder()

            for ((index, task) in taskList.withIndex()) {
                stringBuilder.append("${index + 1}. Задача: ${task.title}\n")
                stringBuilder.append("--> ${task.category} | ${task.day}\n")
                stringBuilder.append("--> ${task.description}\n\n")
            }

            tvListOutput.text = stringBuilder.toString()
        }

        btnBack.setOnClickListener {
            parentFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_container, AddTask())
                .commit()
        }
    }
}