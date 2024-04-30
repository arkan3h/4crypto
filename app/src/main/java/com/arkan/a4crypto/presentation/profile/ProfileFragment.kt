package com.arkan.a4crypto.presentation.profile

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.arkan.a4crypto.R
import com.arkan.a4crypto.data.datasource.auth.AuthDataSource
import com.arkan.a4crypto.data.datasource.auth.FirebaseAuthDataSource
import com.arkan.a4crypto.data.repository.UserRepository
import com.arkan.a4crypto.data.repository.UserRepositoryImpl
import com.arkan.a4crypto.data.source.firebase.FirebaseService
import com.arkan.a4crypto.data.source.firebase.FirebaseServiceImpl
import com.arkan.a4crypto.databinding.FragmentProfileBinding
import com.arkan.a4crypto.presentation.login.LoginActivity
import com.arkan.a4crypto.presentation.main.MainActivity
import com.arkan.aresto.utils.GenericViewModelFactory
import com.arkan.aresto.utils.proceedWhen

class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private var count: Int = 0

    private val viewModel: ProfileViewModel by viewModels {
        val user: FirebaseService = FirebaseServiceImpl()
        val userDataSource: AuthDataSource = FirebaseAuthDataSource(user)
        val userRepo: UserRepository = UserRepositoryImpl(userDataSource)
        GenericViewModelFactory.create(ProfileViewModel(userRepo))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        setClickListener()
        getProfileData()
        changeEditMode()
    }

    private fun getProfileData() {
        viewModel.getCurrentUser()?.let {
            binding.tvName.setText(it.fullName)
            binding.tvEmail.setText(it.email)
        }
    }

    private fun setClickListener() {
        binding.btnEdit.setOnClickListener {
            if (viewModel.isUserLoggedIn()) {
                count += 1
                viewModel.changeEditMode()
                if (count % 2 == 0) {
                    val name = binding.tvName.text.toString().trim()
                    binding.btnEdit.setText(getString(R.string.text_edit_profile))
                    changeProfileName(name)
                } else {
                    binding.btnEdit.setText(getString(R.string.text_save))
                }
            } else {
                navigateToLogin()
            }
        }

        binding.btnLogout.setOnClickListener {
            if (viewModel.isUserLoggedIn()) {
                logoutUser()
            } else {
                navigateToLogin()
            }
        }

        binding.btnChangePw.setOnClickListener {
            if (viewModel.isUserLoggedIn()) {
                changePasswordUser()
            } else {
                navigateToLogin()
            }
        }
    }

    private fun changeEditMode() {
        viewModel.isEditMode.observe(viewLifecycleOwner) {
            binding.tvName.isEnabled = it
            binding.tvEmail.isEnabled = it
        }
    }

    private fun changePasswordUser() {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.layout_dialog_change_password)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        viewModel.changePassword()
        val backBtn: Button = dialog.findViewById(R.id.btn_back)
        backBtn.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun changeProfileName(fullName: String) {
        viewModel.changeProfile(fullName).observe(viewLifecycleOwner) {
            it.proceedWhen(
                doOnSuccess = {
                    Toast.makeText(requireContext(), getString(R.string.text_link_edit_profile_success), Toast.LENGTH_SHORT).show()
                    viewModel.changeEditMode()
                },
                doOnError = {
                    Toast.makeText(requireContext(), getString(R.string.text_link_edit_profile_failed), Toast.LENGTH_SHORT).show()
                },
            )
        }
    }

    private fun logoutUser() {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.layout_dialog_logout)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val btnCancel = dialog.findViewById<Button>(R.id.btn_cancel_dialog)
        val btnLogout = dialog.findViewById<Button>(R.id.btn_logout_dialog)
        btnCancel.setOnClickListener {
            dialog.dismiss()
        }
        btnLogout.setOnClickListener {
            dialog.dismiss()
            viewModel.doLogout()
            navigateToHome()
        }
        dialog.show()
    }

    private fun navigateToLogin() {
        startActivity(Intent(requireContext(), LoginActivity::class.java))
    }

    private fun navigateToHome() {
        startActivity(
            Intent(requireContext(), MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            },
        )
    }
}
