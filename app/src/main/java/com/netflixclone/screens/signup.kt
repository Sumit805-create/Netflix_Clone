package com.netflixclone.screens


import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.netflixclone.R
import com.netflixclone.helpers.AuthTokenManager
import com.netflixclone.network.models.AuthResponse
import com.netflixclone.network.models.RetrofitClient
import com.netflixclone.network.models.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignupActivity : AppCompatActivity() {

    private lateinit var authTokenManager: AuthTokenManager  // Helper class to store token

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        val emailEditText: EditText = findViewById(R.id.email_edit_text)
        val passwordEditText: EditText = findViewById(R.id.password_edit_text)
        val signupButton: Button = findViewById(R.id.signup_button)



        signupButton.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()
//
//            if (email.isEmpty() || password.isEmpty()) {
//                Toast.makeText(this, "Please enter all fields", Toast.LENGTH_SHORT).show()
//                return@setOnClickListener
//            }

            signUpUser(email, password)
        }
    }

    private fun signUpUser(email: String, password: String) {
        RetrofitClient.instance.signup(User(email, password)).enqueue(object: Callback<AuthResponse>{
            override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
                if(response.isSuccessful) {
                    val intent = Intent(this@SignupActivity, SubscriptionActivity::class.java).apply {
                        putExtras(Bundle().apply{
                            putString("username",email)
                        })
                    }
                    this@SignupActivity.startActivity(intent)
                } else {
                    Toast.makeText(this@SignupActivity, "Failed to signup", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                Toast.makeText(this@SignupActivity, "Failed to signup", Toast.LENGTH_LONG).show()
            }

        })

    }
}
