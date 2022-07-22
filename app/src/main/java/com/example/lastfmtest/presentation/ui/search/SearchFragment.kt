package com.example.lastfmtest.presentation.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.lastfmtest.databinding.FragmentSearchBinding
import com.example.lastfmtest.presentation.helper.collectLatestLifecycleFlow
import com.example.lastfmtest.presentation.helper.hideKeyboard
import com.example.lastfmtest.presentation.ui.base.BaseFragment
import com.example.lastfmtest.presentation.ui.adapters.ArtistAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : BaseFragment() {
    private val binding: FragmentSearchBinding by viewBinding(FragmentSearchBinding::bind)
    private val searchViewModel: SearchViewModel by viewModels()
    private val adapter: ArtistAdapter = ArtistAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentSearchBinding.inflate(inflater, container, false).root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter.onArtistClicked = searchViewModel::artistClicked
        binding.rvArtist.adapter = adapter

        searchViewModel.apply {
            artistsLiveData.observe(viewLifecycleOwner) { artists ->
                if (artists.isEmpty()) {
                    binding.tvEmptySearch.isVisible = true
                    binding.rvArtist.isVisible = false
                } else {
                    binding.tvEmptySearch.isVisible = false
                    binding.rvArtist.isVisible = true
                }
                adapter.items = artists
            }
            progressLiveData.observe(viewLifecycleOwner) { isVisible ->
                binding.etSearch.isEnabled = !isVisible
                binding.btnSearch.isEnabled = !isVisible
                binding.pbSearchProgress.isGone = !isVisible
            }
            searchQueryLiveData.observe(viewLifecycleOwner) { searchQuery ->
                binding.etSearch.setText(searchQuery)
            }
            collectLatestLifecycleFlow(navigationArtistTopFlow) { navigationData ->
                val direction =
                    SearchFragmentDirections.actionNavigationSearchToNavigationArtistTopAlbums(
                        artistMbid = navigationData.mbid,
                        artistName = navigationData.name
                    )
                navigate(direction)
            }
            collectLatestLifecycleFlow(errorSnackBarFlow) { errorMessage ->
                showSnackBar(errorMessage)
            }
            start()
        }

        binding.btnSearch.setOnClickListener {
            startSearch(binding.etSearch.text.toString())
        }
        binding.etSearch.setOnEditorActionListener { v, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE && !v.text.isNullOrBlank()) {
                startSearch(v.text.toString())
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }
    }

    private fun startSearch(query: String) {
        hideKeyboard()
        searchViewModel.searchPressed(query)
    }
}