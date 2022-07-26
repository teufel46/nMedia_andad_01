package ru.netology.nmedia.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.databinding.FragmentLoginBinding
import ru.netology.nmedia.di.DependencyContainer
import ru.netology.nmedia.view.afterTextChanged
import ru.netology.nmedia.viewmodel.AuthViewModel
import ru.netology.nmedia.viewmodel.PostViewModel
import ru.netology.nmedia.viewmodel.ViewModelFactory

class LoginFragment : Fragment() {

    private var fragmentBinding: FragmentLoginBinding? = null

    private val dependencyContainer = DependencyContainer.getInstance()
    private val viewModel: PostViewModel by viewModels(
        ownerProducer = ::requireParentFragment,
        factoryProducer = {
            ViewModelFactory(
                dependencyContainer.repository,
                dependencyContainer.appAuth
            )
        }
    )

    private val viewModelAuth: AuthViewModel by viewModels(
        ownerProducer = ::requireParentFragment,
        factoryProducer = {
            ViewModelFactory(
                dependencyContainer.repository,
                dependencyContainer.appAuth
            )
        }
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentLoginBinding.inflate(
            inflater,
            container,
            false
        )
        fragmentBinding = binding

        with(binding) {
            username.requestFocus()

            username.afterTextChanged {
                viewModelAuth.loginDataChanged(
                    username.text.toString(),
                    password.text.toString()
                )
            }

            password.apply {
                afterTextChanged {
                    viewModelAuth.loginDataChanged(
                        username.text.toString(),
                        password.text.toString()
                    )
                }

                login.setOnClickListener {
                    viewModel.updateUser(
                       // "netology",
                           binding.username.text.toString(),
                       // "secret",
                           binding.password.text.toString()
                    )
                    findNavController().navigateUp()
                }

                viewModelAuth.loginFormState.observe(viewLifecycleOwner) {
                    login.isEnabled = it.isDataValid
                }
                return root
            }
        }
    }

    override fun onDestroyView() {
        fragmentBinding = null
        super.onDestroyView()
    }
}


