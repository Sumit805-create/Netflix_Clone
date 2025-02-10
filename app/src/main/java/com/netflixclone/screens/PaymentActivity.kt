package com.netflixclone.payment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.razorpay.Checkout
import com.razorpay.PaymentResultListener
import com.netflixclone.network.models.RetrofitClient
import com.netflixclone.network.models.AuthResponse
import com.netflixclone.screens.ErrorActivity
import com.netflixclone.screens.LoginActivity
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PaymentActivity : AppCompatActivity(), PaymentResultListener {

    private var username: String? = null
    private lateinit var checkout: Checkout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Get Amount & Username from SubscriptionActivity
        val amount = intent.getIntExtra("amount", 0) // Amount in paise
        username = intent.getStringExtra("username")

        Log.d("PaymentActivity", "Starting Razorpay payment. Amount: $amount, Username: $username")

        if (amount > 0) {
            startPayment(amount)
        } else {
            Log.e("PaymentActivity", "Invalid payment amount received")
            Toast.makeText(this, "Invalid amount", Toast.LENGTH_LONG).show()
            navigateToErrorScreen("Invalid payment amount received")
        }
    }

    private fun startPayment(amount: Int) {
        checkout = Checkout()
        checkout.setKeyID("rzp_test_K5Ui643gWIwneL") // Replace with your Razorpay test key

        try {
            val options = JSONObject()
            options.put("name", "Netflix Clone")
            options.put("description", "Premium Subscription")
            options.put("currency", "INR")
            options.put("amount", amount) // Razorpay requires amount in paise
            options.put("prefill.email", "test@example.com")
            options.put("prefill.contact", "9999999999")
            options.put("theme.color", "#F37254")

            Log.d("PaymentActivity", "Opening Razorpay Checkout")
            checkout.open(this, options) // Open Razorpay payment window

        } catch (e: Exception) {
            Log.e("PaymentActivity", "Error initializing Razorpay: ${e.message}")
            navigateToErrorScreen("Payment Initialization Error: ${e.message}")
        }
    }

    override fun onPaymentSuccess(paymentId: String?) {
        Log.d("PaymentActivity", "Payment Successful. ID: $paymentId, Username: $username")

        if (username.isNullOrEmpty() || paymentId.isNullOrEmpty()) {
            Log.e("PaymentActivity", "Missing required parameters: Username or Payment ID")
            navigateToErrorScreen("Missing required parameters: Username or Payment ID")
            return
        }

        // Data to send to the backend
        val paymentData = mapOf(
            "username" to username!!,
            "razorpay_payment_id" to paymentId
        )

        Log.d("PaymentActivity", "Sending request to update subscription: $paymentData")

        RetrofitClient.instance.updateSubscription(paymentData).enqueue(object : Callback<AuthResponse> {
            override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
                if (response.isSuccessful) {
                    Log.d("PaymentActivity", "Subscription updated successfully!")

                    // Show success message
                    Toast.makeText(this@PaymentActivity, "Subscription updated successfully!", Toast.LENGTH_LONG).show()

                    // Navigate to Login Page
                    val intent = Intent(this@PaymentActivity, LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    finish() // Close PaymentActivity
                } else {
                    val errorMessage = response.errorBody()?.string() ?: "Subscription update failed"
                    Log.e("PaymentActivity", "Subscription update failed: $errorMessage")
                    navigateToErrorScreen(errorMessage)
                }
            }

            override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                Log.e("PaymentActivity", "API error updating subscription: ${t.message}")
                navigateToErrorScreen("Network error updating subscription: ${t.message}")
            }
        })
    }

    override fun onPaymentError(code: Int, response: String?) {
        Log.e("PaymentActivity", "Payment Failed. Code: $code, Response: $response")
        navigateToErrorScreen("Payment Failed: $response")
    }

    private fun navigateToErrorScreen(errorMessage: String) {
        Log.e("PaymentActivity", "Navigating to Error Screen: $errorMessage")
        val intent = Intent(this@PaymentActivity, ErrorActivity::class.java)
        intent.putExtra("error_message", errorMessage)
        startActivity(intent)
        finish()
    }
}
