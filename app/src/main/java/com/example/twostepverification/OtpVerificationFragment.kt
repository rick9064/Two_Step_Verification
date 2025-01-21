package com.example.twostepverification

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

class OtpVerificationFragment : Fragment() {

    private lateinit var firebaseAuth: FirebaseAuth
    private var verificationId: String? = null

    companion object {
        fun newInstance(email: String, mobile: String): OtpVerificationFragment {
            val fragment = OtpVerificationFragment()
            val args = Bundle()
            args.putString("email", email)
            args.putString("mobile", mobile)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_otp_verification, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        firebaseAuth = FirebaseAuth.getInstance()

        val email = arguments?.getString("email")
        val mobile = arguments?.getString("mobile")
        val otpField = view.findViewById<EditText>(R.id.etOTP)
        val verifyButton = view.findViewById<Button>(R.id.btnVerify)

        if (!mobile.isNullOrEmpty()) {
            sendOtpToMobile(mobile)
        } else {
            Toast.makeText(context, "Mobile number is missing.", Toast.LENGTH_SHORT).show()
        }

        verifyButton.setOnClickListener {
            val otp = otpField.text.toString()
            if (otp.isEmpty()) {
                Toast.makeText(context, "Please enter OTP", Toast.LENGTH_SHORT).show()
            } else {
                verifyOtp(otp)
            }
        }
    }

    private fun sendOtpToMobile(mobile: String) {
        val options = PhoneAuthOptions.newBuilder(firebaseAuth)
            .setPhoneNumber(mobile)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(requireActivity())
            .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                    // Auto verification
                    Toast.makeText(context, "OTP verified automatically", Toast.LENGTH_SHORT).show()
                    firebaseAuth.signInWithCredential(credential)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Toast.makeText(context, "Login successful!", Toast.LENGTH_SHORT).show()
                                // Navigate to the next screen
                            }
                        }
                }

                override fun onVerificationFailed(exception: FirebaseException) {
                    Toast.makeText(
                        context,
                        "Failed to send OTP: ${exception.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onCodeSent(
                    verificationId: String,
                    token: PhoneAuthProvider.ForceResendingToken
                ) {
                    this@OtpVerificationFragment.verificationId = verificationId
                    Toast.makeText(context, "OTP sent to $mobile", Toast.LENGTH_SHORT).show()
                }
            })
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private fun verifyOtp(otp: String) {
        if (verificationId == null) {
            Toast.makeText(context, "OTP verification failed. Try again.", Toast.LENGTH_SHORT).show()
            return
        }
        val credential = PhoneAuthProvider.getCredential(verificationId!!, otp)
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(context, "OTP verified successfully!", Toast.LENGTH_SHORT).show()
                    // Navigate to HomeFragment or other actions
                } else {
                    Toast.makeText(context, "Invalid OTP. Try again.", Toast.LENGTH_SHORT).show()
                }
            }
    }
}
