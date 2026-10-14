package com.example.pract_4_hojaeva

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment

class LogIn : Fragment(R.layout.activity_log_in) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPreferences = requireContext().getSharedPreferences("Data", Context.MODE_PRIVATE)

        val btnLogIn = view.findViewById<Button>(R.id.btnLogIn)
        val etLogin = view.findViewById<EditText>(R.id.etLogin)
        val etPassword = view.findViewById<EditText>(R.id.etPassword)

        btnLogIn.setOnClickListener {
            val loginText = etLogin.text.toString().trim()
            val passwordText = etPassword.text.toString().trim()

            if (loginText.isEmpty() || passwordText.isEmpty()) {
                Toast.makeText(
                    requireContext(),
                    R.string.msgPleaseInputAllDataLog,
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }
            val savedLogin = sharedPreferences.getString("LOGIN", null)
            val savedPassword = sharedPreferences.getString("PASSWORD", null)

            if (savedLogin == null) {
                val prefEditor = sharedPreferences.edit()
                prefEditor.putString("LOGIN", loginText)
                prefEditor.putString("PASSWORD", passwordText)
                prefEditor.apply()

                parentFragmentManager
                    .beginTransaction()
                    .replace(R.id.fragment_container, AddTask())
                    .commit()
            } else {

                if (loginText == savedLogin && passwordText == savedPassword) {
                    Toast.makeText(requireContext(), R.string.msgGoodExitLog, Toast.LENGTH_SHORT).show()

                    parentFragmentManager
                        .beginTransaction()
                        .replace(R.id.fragment_container, AddTask())
                        .commit()
                } else {
                    Toast.makeText(requireContext(), R.string.msgBadExitLog, Toast.LENGTH_SHORT).show()
                }
            }
        }

    }
}