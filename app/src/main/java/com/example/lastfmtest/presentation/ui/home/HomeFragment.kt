package com.example.lastfmtest.presentation.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.lastfmtest.R
import com.example.lastfmtest.databinding.FragmentHomeBinding
import com.example.lastfmtest.domain.model.AlbumData
import com.example.lastfmtest.presentation.helper.collectLatestLifecycleFlow
import com.example.lastfmtest.presentation.ui.adapters.AlbumAdapter
import com.example.lastfmtest.presentation.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment() {
    private val binding: FragmentHomeBinding by viewBinding(FragmentHomeBinding::bind)
    private val homeViewModel: HomeViewModel by viewModels()
    private val adapter: AlbumAdapter = AlbumAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentHomeBinding.inflate(inflater, container, false).root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvSavedAlbums.adapter = adapter

        adapter.onAlbumClicked = homeViewModel::albumClicked
        adapter.onFavoriteClicked = ::showUndoAlbumDeletionSnackBar

        homeViewModel.apply {
            savedAlbumsLiveData.observe(viewLifecycleOwner) { albums ->
                if (albums.isEmpty()) {
                    binding.tvEmptySavedAlbums.isVisible = true
                    binding.rvSavedAlbums.isVisible = false
                } else {
                    binding.tvEmptySavedAlbums.isVisible = false
                    binding.rvSavedAlbums.isVisible = true
                }
                adapter.items = albums
            }
            collectLatestLifecycleFlow(navigationAlbumFlow) { navigationData ->
                val direction = HomeFragmentDirections.actionNavigationHomeToAlbumDetailsFragment(
                    albumId = navigationData.mbid,
                    albumName = navigationData.name
                )
                navigate(direction)
            }
            start()
        }
    }

    private fun showUndoAlbumDeletionSnackBar(item: AlbumData) {
        homeViewModel.removeFromFavorites(item)
        showUndoSnackBar(getString(R.string.album_deleted), getString(R.string.undo), {
            homeViewModel.addToFavorites(item)
        }, {
            homeViewModel.removeAlbum(item)
        })
    }
}