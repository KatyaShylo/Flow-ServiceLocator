package com.example.rmservicelocator.view

import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.rmservicelocator.R
import com.example.rmservicelocator.adapter.CharacterAdapter
import com.example.rmservicelocator.databinding.FragmentAllCharactersBinding
import com.example.rmservicelocator.model.Lce
import com.example.rmservicelocator.service.ServiceLocator
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class AllCharactersFragment : Fragment() {

    private var _binding: FragmentAllCharactersBinding? = null
    private val binding get() = requireNotNull(_binding)

    private val adapter by lazy {
        CharacterAdapter(requireContext()) { character ->
            findNavController().navigate(
                AllCharactersFragmentDirections.toDetailFragment(
                    character.id
                )
            )
        }
    }

    private val viewModel by viewModels<AllCharactersViewModel> {
        viewModelFactory {
            initializer {
                AllCharactersViewModel(
                    ServiceLocator.provideCharacters(),
                    ServiceLocator.provideDataSource())
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentAllCharactersBinding.inflate(inflater, container, false)
            .also { _binding = it }
            .root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {

            recyclerView.adapter = adapter

            viewModel.onRefreshed()

            with(toolbar) {

                menu
                    .findItem(R.id.action_search)
                    .actionView
                    .let { it as SearchView }
                    .setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                        override fun onQueryTextSubmit(query: String): Boolean = false

                        override fun onQueryTextChange(query: String): Boolean {
                            viewModel.onQueryChanged(query)
                            return true
                        }
                    })
            }

            swipe.setOnRefreshListener {
                adapter.submitList(emptyList())
                viewModel.onRefreshed()
            }

            recyclerView.addItemDecoration(object : RecyclerView.ItemDecoration() {
                override fun getItemOffsets(
                    outRect: Rect,
                    view: View,
                    parent: RecyclerView,
                    state: RecyclerView.State
                ) {
                    if (parent.getChildAdapterPosition(view) != parent.adapter?.itemCount) {
                        outRect.top = resources.getDimensionPixelSize(R.dimen.value_for_decorator)
                    }
                }
            })

            viewModel
                .rickAndMortyFlow
                .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
                .onEach { swipe.isRefreshing = false }
                .onEach{ lce ->
                    when (lce) {
                        Lce.Loading -> {
                            loading.isVisible = true
                        }
                        is Lce.Success -> {
                            loading.isVisible = false
                            swipe.isVisible = true
                            adapter.submitList(lce.data)
                        }
                        is Lce.Fail -> {
                            Toast.makeText(requireContext(), lce.error.message, Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                }
                .launchIn(viewLifecycleOwner.lifecycleScope)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}