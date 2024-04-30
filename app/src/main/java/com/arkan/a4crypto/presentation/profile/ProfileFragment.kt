package com.arkan.a4crypto.presentation.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.arkan.aresto.utils.GenericViewModelFactory

class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding

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
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }
}
