package com.example.apiassesment2app.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.apiassesment2app.data.Repository
import com.example.apiassesment2app.databinding.ActivityLoginPageBinding
import com.example.apiassesment2app.ui.dashboard.DashboardActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException
import javax.inject.Inject

@AndroidEntryPoint
class LoginPage : AppCompatActivity() {

    private lateinit var binding: ActivityLoginPageBinding
    @Inject lateinit var repo: Repository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Prefill for demo
        binding.etUsername.setText("Geetansh")
        binding.etPassword.setText("8103430")

        binding.btnLogin.setOnClickListener {
            val user = binding.etUsername.text?.toString()?.trim().orEmpty()
            val pass = binding.etPassword.text?.toString()?.trim().orEmpty()

            if (user.isEmpty() || pass.isEmpty()) {
                toast("Enter username & password"); return@setOnClickListener
            }
            if (!pass.matches(Regex("^\\d{7,8}$"))) {
                toast("Password must be your 7- or 8-digit Student ID"); return@setOnClickListener
            }

            binding.progress.visibility = View.VISIBLE
            lifecycleScope.launch {
                try {
                    val keypass = tryLoginOnce(user, pass) ?: run {
                        // Retry once after short delay (Render cold start)
                        toast("Server waking up… retrying")
                        delay(1500)
                        tryLoginOnce(user, pass)
                    }

                    if (keypass == null) {
                        toast("Login failed. Please try again.")
                    } else {
                        startActivity(Intent(this@LoginPage, DashboardActivity::class.java).apply {
                            putExtra("keypass", keypass)
                        })
                        finish()
                    }
                } finally {
                    binding.progress.visibility = View.GONE
                }
            }
        }
    }

    private suspend fun tryLoginOnce(user: String, pass: String): String? {
        return try {
            // Change "footscray" to "sydney" or "br" if that's your campus.
            val res = repo.login("footscray", user, pass)
            res.keypass
        } catch (e: SocketTimeoutException) {
            toast("Login timed out. Retrying…")
            null
        } catch (e: Exception) {
            android.util.Log.e("LoginPage", "Login failed", e)
            toast(e.message ?: "Login error")
            null
        }
    }

    private fun toast(msg: String) =
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
}
