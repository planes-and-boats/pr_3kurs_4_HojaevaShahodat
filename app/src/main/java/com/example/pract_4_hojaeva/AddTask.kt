package com.example.pract_4_hojaeva

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class AddTask : Fragment(R.layout.activity_add_task) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPreferences = requireContext().getSharedPreferences("Data", Context.MODE_PRIVATE)
        val gson = Gson()

        val btnMoveToList = view.findViewById<Button>(R.id.btnMoveToList)
        val btnSaveTask = view.findViewById<Button>(R.id.btnSaveTask)
        val spnImport = view.findViewById<Spinner>(R.id.spnLevel)
        val spnDay = view.findViewById<Spinner>(R.id.spnDay)
        val etTaskTitle = view.findViewById<EditText>(R.id.etTaskTitle)
        val etDescription = view.findViewById<EditText>(R.id.etDescription)

        spnImport.prompt = getString(R.string.spnImportTitle)
        val adapterImport = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.spnImportArray,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
        spnImport.adapter = adapterImport

        spnDay.prompt = getString(R.string.spnDayTitle)
        val adapterDay = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.spnDayArray,
            android.R.layout.simple_spinner_item
        ). also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
        spnDay.adapter = adapterDay

        btnMoveToList.setOnClickListener {
            parentFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_container, ToDoList())
                .commit()
        }

        btnSaveTask.setOnClickListener {
            val title = etTaskTitle.text.toString().trim()
            val description = etDescription.text.toString().trim()
            val importance = spnImport.selectedItem?.toString() ?: ""
            val day = spnDay.selectedItem?.toString() ?: ""

            if (title.isEmpty() || description.isEmpty() || importance.isEmpty() || day.isEmpty()) {
                Toast.makeText(requireContext(), R.string.msgPleaseInputAllDataAddTask, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val jsonTasks = sharedPreferences.getString("TASKS_LIST", null)

            val taskList: ArrayList<Task> = if (jsonTasks == null) {
                ArrayList()
            } else {
                val type = object : TypeToken<ArrayList<Task>>() {}.type
                gson.fromJson(jsonTasks, type)
            }

            val newTask = Task(title, importance, day, description)
            taskList.add(newTask)

            val updatedJsonTasks = gson.toJson(taskList)

            sharedPreferences.edit().putString("TASKS_LIST", updatedJsonTasks).apply()

            Toast.makeText(requireContext(), R.string.msgSaveTask, Toast.LENGTH_SHORT).show()

            etTaskTitle.text.clear()
            etDescription.text.clear()
        }

    }
}