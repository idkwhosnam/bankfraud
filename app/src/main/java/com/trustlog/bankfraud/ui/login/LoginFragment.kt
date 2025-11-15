package com.trustlog.bankfraud.ui.login

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.trustlog.bankfraud.R
import com.trustlog.bankfraud.databinding.FragmentLoginBinding
import com.trustlog.bankfraud.sensor.SensorCollectionManager
import com.trustlog.bankfraud.viewmodel.SessionViewModel
import java.util.regex.Pattern

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SessionViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        SensorCollectionManager.clear()
        binding.inputEmail.setText(viewModel.getUserEmail())
        binding.inputName.setText(viewModel.getUserName())

        val watcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                validateInputs()
            }
        }
        binding.inputEmail.addTextChangedListener(watcher)
        binding.inputName.addTextChangedListener(watcher)

        binding.buttonLogin.setOnClickListener {
            if (!validateInputs(showErrors = true)) {
                Snackbar.make(binding.root, R.string.error_inputs_invalid, Snackbar.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val email = binding.inputEmail.text?.toString().orEmpty().trim()
            val name = binding.inputName.text?.toString().orEmpty().trim()
            viewModel.setUserCredentials(email, name)
            SensorCollectionManager.startForegroundCollection()
            findNavController().navigate(R.id.action_loginFragment_to_transferFragment)
        }

        validateInputs()
    }

    private fun validateInputs(showErrors: Boolean = false): Boolean {
        val email = binding.inputEmail.text?.toString().orEmpty().trim()
        val name = binding.inputName.text?.toString().orEmpty().trim()

        val emailValid = EMAIL_PATTERN.matcher(email).matches()
        val nameValid = NAME_PATTERN.matcher(name).matches() && name.length in 3..40

        if (showErrors || email.isNotEmpty()) {
            binding.emailInputLayout.error = if (emailValid || email.isEmpty()) null else getString(R.string.error_email_invalid)
        }
        if (showErrors || name.isNotEmpty()) {
            binding.nameInputLayout.error = if (nameValid || name.isEmpty()) null else getString(R.string.error_name_invalid)
        }
        val isValid = emailValid && nameValid
        binding.buttonLogin.isEnabled = isValid
        return isValid
    }

    companion object {
        private val EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9._%+-]+@gmail\\.com$")
        private val NAME_PATTERN = Pattern.compile("^[\\p{L} ]+$")
    }

    override fun onResume() {
        super.onResume()
        SensorCollectionManager.startForegroundCollection()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
