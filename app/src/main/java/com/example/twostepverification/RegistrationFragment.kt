package com.example.twostepverification

import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

class RegistrationFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_registration, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val usernameField = view.findViewById<EditText>(R.id.etUsername)
        val emailField = view.findViewById<EditText>(R.id.etEmail)
        val mobileField = view.findViewById<EditText>(R.id.etMobile)
        val passwordField = view.findViewById<EditText>(R.id.etPassword)
        val togglePasswordIcon = view.findViewById<ImageView>(R.id.ivTogglePassword)
        val rePasswordField = view.findViewById<EditText>(R.id.etRePassword)
        val toggleRePasswordIcon = view.findViewById<ImageView>(R.id.ivToggleRePassword)
        val genderRadioGroup = view.findViewById<RadioGroup>(R.id.radioGroupGender)
        val registerButton = view.findViewById<Button>(R.id.btnRegister)
        val backToLoginText = view.findViewById<TextView>(R.id.tvBackToLogin)

        var isPasswordVisible = false
        var isRePasswordVisible = false

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

        // Toggle for Re-enter Password visibility
        toggleRePasswordIcon.setOnClickListener {
            if (isRePasswordVisible) {
                // Hide password
                rePasswordField.transformationMethod = PasswordTransformationMethod.getInstance()
                toggleRePasswordIcon.setImageResource(R.drawable.ic_eye_close) // Closed eye icon
            } else {
                // Show password
                rePasswordField.transformationMethod = HideReturnsTransformationMethod.getInstance()
                toggleRePasswordIcon.setImageResource(R.drawable.ic_eye_open) // Open eye icon
            }
            isRePasswordVisible = !isRePasswordVisible
            rePasswordField.setSelection(rePasswordField.text.length) // Move cursor to end
        }

        registerButton.setOnClickListener {
            val username = usernameField.text.toString()
            val email = emailField.text.toString()
            val mobile = mobileField.text.toString()
            val password = passwordField.text.toString()
            val rePassword = rePasswordField.text.toString()

            val selectedGenderId = genderRadioGroup.checkedRadioButtonId
            val gender = if (selectedGenderId != -1) {
                view.findViewById<RadioButton>(selectedGenderId).text.toString()
            } else {
                ""
            }

            if (username.isEmpty() || email.isEmpty() || mobile.isEmpty() || password.isEmpty() || rePassword.isEmpty() || gender.isEmpty()) {
                Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
            } else if (!mobile.startsWith("+91")) {
                Toast.makeText(context, "Mobile number must start with +91", Toast.LENGTH_SHORT).show()
            } else if (password != rePassword) {
                Toast.makeText(context, "Passwords do not match", Toast.LENGTH_SHORT).show()
            } else {
                // Store user data in SharedPreferences
                val sharedPreferences = requireActivity().getSharedPreferences("UserPrefs", AppCompatActivity.MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                editor.putString("email", email)
                editor.putString("mobile", mobile)
                editor.putString("password", password)
                editor.apply()

                // Navigate to OTP Verification
                val otpFragment = OtpVerificationFragment.newInstance(email, mobile)
                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, otpFragment)
                    .addToBackStack(null)
                    .commit()


        // Handle registration logic here
                Toast.makeText(context, "User $username registered successfully!", Toast.LENGTH_SHORT).show()
            }
        }

        backToLoginText.setOnClickListener {
            // Handle navigation back to the login screen
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, LoginFragment())
                .addToBackStack(null)
                .commit()
        }
    }
}
