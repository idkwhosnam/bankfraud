package com.trustlog.bankfraud.ui.transfer

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.trustlog.bankfraud.R
import com.trustlog.bankfraud.databinding.FragmentTransferBinding
import com.trustlog.bankfraud.viewmodel.SessionViewModel
import java.text.DecimalFormat

class TransferFragment : Fragment() {

    private var _binding: FragmentTransferBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SessionViewModel by activityViewModels()
    private var navigationHandled = false
    private var amountWatcher: TextWatcher? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTransferBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.balance.text = getString(R.string.balance_template, "50.000.000")
        binding.sender.text = getString(R.string.sender_template, viewModel.getUserName())
        binding.receiver.text = getString(R.string.receiver_template, getString(R.string.placeholder_receiver))

        setupAmountFormatting()

        binding.buttonTransfer.setOnClickListener {
            val amount = binding.inputAmount.text?.toString()?.trim().orEmpty()
            if (!isValidAmount(amount)) {
                binding.amountInputLayout.error = getString(R.string.error_amount_invalid)
                return@setOnClickListener
            }
            binding.amountInputLayout.error = null
            viewModel.finalizeTransaction(amount)
        }

        viewModel.resultState.observe(viewLifecycleOwner, Observer { state ->
            binding.buttonTransfer.isEnabled = !state.isUploading
            if (!navigationHandled && state.result != null && !state.isUploading) {
                navigationHandled = true
                findNavController().navigate(R.id.action_transferFragment_to_resultFragment)
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        navigationHandled = false
        amountWatcher?.let { binding.inputAmount.removeTextChangedListener(it) }
        _binding = null
    }

    private fun setupAmountFormatting() {
        val formatter = DecimalFormat("###,###")
        amountWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                if (s.isNullOrEmpty()) return
                binding.inputAmount.removeTextChangedListener(this)
                val digits = s.toString().replace(".", "").replace(",", "")
                val numeric = digits.toLongOrNull()
                if (numeric != null) {
                    val formatted = formatter.format(numeric).replace(",", ".")
                    binding.inputAmount.setText(formatted)
                    binding.inputAmount.setSelection(formatted.length)
                }
                binding.inputAmount.addTextChangedListener(this)
            }
        }
        binding.inputAmount.addTextChangedListener(amountWatcher)
    }

    private fun isValidAmount(input: String): Boolean {
        if (input.isBlank()) return false
        if (!AMOUNT_REGEX.matches(input)) return false
        val numeric = input.replace(".", "")
        val value = numeric.toLongOrNull() ?: return false
        return value >= 1_000
    }

    companion object {
        private val AMOUNT_REGEX = Regex("^\\d{1,3}(\\.\\d{3})*$")
    }
}
