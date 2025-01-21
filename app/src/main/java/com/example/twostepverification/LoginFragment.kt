package com.example.twostepverification

import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class LoginFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val usernameField = view.findViewById<EditText>(R.id.etUsername)
        val passwordField = view.findViewById<EditText>(R.id.etPassword)
        val togglePasswordIcon = view.findViewById<ImageView>(R.id.ivTogglePassword)
        val loginButton = view.findViewById<Button>(R.id.btnLogin)
        val createAccountText = view.findViewById<TextView>(R.id.tvCreateAccount)

        var isPasswordVisible = false

        togglePasswordIcon.setOnClickListener {
            if (isPasswordVisible) {
                // Hide password
                passwordField.transformationMethod = PasswordTransformationMethod.getInstance()
                togglePasswordIcon.setImageResource(R.drawable.ic_eye_close) // Closed eye icon
            } else {
                // Show password
                passwordField.transformationMethod = HideReturnsTransformationMethod.getInstance()
                togglePasswordIcon.setImageResource(R.drawable.ic_eye_open) // Open eye icon
            }
            isPasswordVisible = !isPasswordVisible
            passwordField.setSelection(passwordField.text.length) // Move cursor to end
        }

        loginButton.setOnClickListener {
            val username = usernameField.text.toString()
            val password = passwordField.text.toString()

            // Get stored email and password from SharedPreferences
            val sharedPreferences = requireActivity().getSharedPreferences("UserPrefs", AppCompatActivity.MODE_PRIVATE)
            val storedEmail = sharedPreferences.getString("email", "")
            val storedPassword = sharedPreferences.getString("password", "")

            // Validate login credentials
            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
            } else if (username == storedEmail && password == storedPassword) {
                // Successful login
                Toast.makeText(context, "Logging in as $username", Toast.LENGTH_SHORT).show()
                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, HomeFragment())
                    .addToBackStack(null)
                    .commit()
            } else {
                // Invalid credentials
                Toast.makeText(context, "Invalid email or password", Toast.LENGTH_SHORT).show()
            }
        }

        createAccountText.setOnClickListener {
            // Navigate to the registration page
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, RegistrationFragment())
                .addToBackStack(null)
                .commit()
        }
    }
}
