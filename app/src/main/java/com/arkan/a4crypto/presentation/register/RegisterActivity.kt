package com.arkan.a4crypto.presentation.register

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
import com.arkan.a4crypto.databinding.ActivityRegisterBinding
import com.arkan.a4crypto.presentation.login.LoginActivity
import com.arkan.a4crypto.presentation.main.MainActivity
import com.arkan.aresto.utils.GenericViewModelFactory
import com.arkan.aresto.utils.proceedWhen
import com.google.android.material.textfield.TextInputLayout

class RegisterActivity : AppCompatActivity() {
    private val binding: ActivityRegisterBinding by lazy {
        ActivityRegisterBinding.inflate(layoutInflater)
    }

    private val viewModel: RegisterViewModel by viewModels {
        val s: FirebaseService = FirebaseServiceImpl()
        val ds: AuthDataSource = FirebaseAuthDataSource(s)
        val r: UserRepository = UserRepositoryImpl(ds)
        GenericViewModelFactory.create(RegisterViewModel(r))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setClickListeners()
    }

    private fun setClickListeners() {
        binding.btnRegister.setOnClickListener {
            doRegister()
        }
        binding.btnRegister.setOnClickListener {
            navigateToLogin()
        }
    }

    private fun doRegister() {
        if (isFormValid()) {
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()
            val fullName = binding.etName.text.toString().trim()
            proceedRegister(email, password, fullName)
        }
    }

    private fun proceedRegister(
        email: String,
        password: String,
        fullName: String,
    ) {
        viewModel.doRegister(email, fullName, password).observe(this) {
            it.proceedWhen(
                doOnSuccess = {
                    binding.pbLoading.isVisible = false
                    binding.btnRegister.isVisible = true
                    navigateToMain()
                },
                doOnError = {
                    binding.pbLoading.isVisible = false
                    binding.btnRegister.isVisible = true
                    Toast.makeText(
                        this,
                        "Login Failed : ${it.exception?.message.orEmpty()}",
                        Toast.LENGTH_SHORT,
                    ).show()
                },
                doOnLoading = {
                    binding.pbLoading.isVisible = true
                    binding.btnRegister.isVisible = false
                },
            )
        }
    }

    private fun navigateToLogin() {
        startActivity(
            Intent(this, LoginActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
            },
        )
    }

    private fun navigateToMain() {
        startActivity(
            Intent(this, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            },
        )
    }

    private fun isFormValid(): Boolean {
        val password = binding.etPassword.text.toString().trim()
        val confirmPassword = binding.etConfirmPassword.text.toString().trim()
        val fullName = binding.etName.text.toString().trim()
        val email = binding.etEmail.text.toString().trim()

        return checkNameValidation(fullName) && checkEmailValidation(email) &&
            checkPasswordValidation(password, binding.tilPassword) &&
            checkPasswordValidation(confirmPassword, binding.tilConfirmPassword) &&
            checkPwdAndConfirmPwd(password, confirmPassword)
    }

    private fun checkNameValidation(fullName: String): Boolean {
        return if (fullName.isEmpty()) {
            binding.tilName.isErrorEnabled = true
            binding.tilName.error = getString(R.string.text_error_name_empty)
            false
        } else {
            binding.tilName.isErrorEnabled = false
            true
        }
    }

    private fun checkEmailValidation(email: String): Boolean {
        return if (email.isEmpty()) {
            binding.tilEmail.isErrorEnabled = true
            binding.tilEmail.error = getString(R.string.text_error_email_empty)
            false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.tilEmail.isErrorEnabled = true
            binding.tilEmail.error = getString(R.string.text_error_email_invalid)
            false
        } else {
            binding.tilEmail.isErrorEnabled = false
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

    private fun checkPwdAndConfirmPwd(
        password: String,
        confirmPassword: String,
    ): Boolean {
        return if (password != confirmPassword) {
            binding.tilPassword.isErrorEnabled = true
            binding.tilPassword.error =
                getString(R.string.text_pw_nomatch)
            binding.tilConfirmPassword.isErrorEnabled = true
            binding.tilConfirmPassword.error =
                getString(R.string.text_pw_nomatch)
            false
        } else {
            binding.tilPassword.isErrorEnabled = false
            binding.tilConfirmPassword.isErrorEnabled = false
            true
        }
    }
}
