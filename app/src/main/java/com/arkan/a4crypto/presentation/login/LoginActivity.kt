package com.arkan.a4crypto.presentation.login

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.arkan.a4crypto.R
import com.arkan.a4crypto.data.datasource.auth.AuthDataSource
import com.arkan.a4crypto.data.datasource.auth.FirebaseAuthDataSource
import com.arkan.a4crypto.data.repository.UserRepository
import com.arkan.a4crypto.data.repository.UserRepositoryImpl
import com.arkan.a4crypto.data.source.firebase.FirebaseService
import com.arkan.a4crypto.data.source.firebase.FirebaseServiceImpl
import com.arkan.a4crypto.databinding.ActivityLoginBinding
import com.arkan.a4crypto.presentation.main.MainActivity
import com.arkan.a4crypto.presentation.register.RegisterActivity
import com.arkan.aresto.utils.GenericViewModelFactory
import com.arkan.aresto.utils.proceedWhen
import com.google.android.material.textfield.TextInputLayout

class LoginActivity : AppCompatActivity() {
    private val binding: ActivityLoginBinding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }
    private val viewModel: LoginViewModel by viewModels {
        val s: FirebaseService = FirebaseServiceImpl()
        val ds: AuthDataSource = FirebaseAuthDataSource(s)
        val r: UserRepository = UserRepositoryImpl(ds)
        GenericViewModelFactory.create(LoginViewModel(r))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setClickListener()
        observeResult()
    }

    private fun setClickListener() {
        binding.layoutFormLogin.btnLogin.setOnClickListener {
            doLogin()
        }
        binding.layoutFormLogin.tvNavToRegister.setOnClickListener {
            navigateToRegister()
        }
    }

    private fun navigateToMain() {
        startActivity(
            Intent(this, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            },
        )
    }

    private fun navigateToRegister() {
        startActivity(
            Intent(this, RegisterActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
            },
        )
    }

    private fun observeResult() {
        viewModel.loginResult.observe(this) {
            it.proceedWhen(
                doOnSuccess = {
                    binding.layoutFormLogin.pbLogin.isVisible = false
                    binding.layoutFormLogin.btnLogin.isVisible = true
                    navigateToMain()
                },
                doOnError = {
                    binding.layoutFormLogin.pbLogin.isVisible = false
                    binding.layoutFormLogin.btnLogin.isVisible = true
                    Toast.makeText(
                        this,
                        "Login Failed : ${it.exception?.message.orEmpty()}",
                        Toast.LENGTH_SHORT,
                    ).show()
                },
                doOnLoading = {
                    binding.layoutFormLogin.pbLogin.isVisible = true
                    binding.layoutFormLogin.btnLogin.isVisible = false
                },
            )
        }
    }

    private fun doLogin() {
        if (isFormValid()) {
            val email = binding.layoutFormLogin.etEmailLogin.text.toString().trim()
            val password = binding.layoutFormLogin.etPasswordLogin.text.toString().trim()
            viewModel.doLogin(email, password)
        }
    }

    private fun isFormValid(): Boolean {
        val email = binding.layoutFormLogin.etEmailLogin.text.toString().trim()
        val password = binding.layoutFormLogin.etPasswordLogin.text.toString().trim()

        return checkEmailValidation(email) &&
            checkPasswordValidation(password, binding.layoutFormLogin.tilPasswordLogin)
    }

    private fun checkEmailValidation(email: String): Boolean {
        return if (email.isEmpty()) {
            binding.layoutFormLogin.tilEmailLogin.isErrorEnabled = true
            binding.layoutFormLogin.tilEmailLogin.error = getString(R.string.text_error_email_empty)
            false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.layoutFormLogin.tilEmailLogin.isErrorEnabled = true
            binding.layoutFormLogin.tilEmailLogin.error = getString(R.string.text_error_email_invalid)
            false
        } else {
            binding.layoutFormLogin.tilEmailLogin.isErrorEnabled = false
            true
        }
    }

    private fun checkPasswordValidation(
        confirmPassword: String,
        textInputLayout: TextInputLayout,
    ): Boolean {
        return if (confirmPassword.isEmpty()) {
            textInputLayout.isErrorEnabled = true
            textInputLayout.error =
                getString(R.string.text_error_pw_empty)
            false
        } else if (confirmPassword.length < 8) {
            textInputLayout.isErrorEnabled = true
            textInputLayout.error =
                getString(R.string.text_error_pw_lower)
            false
        } else {
            textInputLayout.isErrorEnabled = false
            true
        }
    }
}
