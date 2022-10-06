package com.example.rmservicelocator.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.load
import com.example.rmservicelocator.R
import com.example.rmservicelocator.databinding.FragmentDetailsBinding
import com.example.rmservicelocator.model.Lce
import com.example.rmservicelocator.service.ServiceLocator
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class DetailsFragment : Fragment() {

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = requireNotNull(_binding)

    private val args by navArgs<DetailsFragmentArgs>()

    private val viewModel by viewModels<DetailViewModel> {
        viewModelFactory {
            initializer {
                DetailViewModel(ServiceLocator.provideCharacters(), args.id)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentDetailsBinding.inflate(inflater, container, false)
            .also { _binding = it }
            .root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {

            toolbarFragmentDetail.setNavigationOnClickListener {
                findNavController().navigate(R.id.all_characters_fragment)
            }

            viewModel
                .detailFlow
                .onEach { lce ->
                    when (lce) {
                        Lce.Loading -> {
                            loading.isVisible = true
                        }
                        is Lce.Success -> {
                            loading.isVisible = false
                            cardDetail.isVisible = true
                            imageViewAvatar.load(lce.data.image)
                            textViewNameCharacter.text = lce.data.name
                            textViewIdCharacter.text = "Id: ${lce.data.id}"
                            textViewStatus.text = "Status: ${lce.data.status}"
                            textViewSpecies.text = "Species: ${lce.data.species}"
                            textViewGender.text = "Gender: ${lce.data.gender}"
                        }
                        is Lce.Fail -> {
                            Toast.makeText(requireContext(), lce.error.message, Toast.LENGTH_SHORT)
                                .show()
                        }

                    }
                }.launchIn(viewLifecycleOwner.lifecycleScope)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}