package com.example.lastfmtest.presentation.ui.artisttopalbums

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.lastfmtest.R
import com.example.lastfmtest.databinding.FragmentArtistTopAlbumsBinding
import com.example.lastfmtest.presentation.helper.collectLatestLifecycleFlow
import com.example.lastfmtest.presentation.ui.adapters.AlbumAdapter
import com.example.lastfmtest.presentation.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ArtistTopAlbumsFragment : BaseFragment() {
    private val binding: FragmentArtistTopAlbumsBinding by viewBinding(FragmentArtistTopAlbumsBinding::bind)
    private val artistTopAlbumsViewModel: ArtistTopAlbumsViewModel by viewModels()
    private val safeArgs: ArtistTopAlbumsFragmentArgs by navArgs()
    private val adapter: AlbumAdapter = AlbumAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentArtistTopAlbumsBinding.inflate(inflater, container, false).root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvAlbums.adapter = adapter

        adapter.onAlbumClicked = artistTopAlbumsViewModel::albumClicked
        adapter.onFavoriteClicked = artistTopAlbumsViewModel::albumFavoriteClicked

        artistTopAlbumsViewModel.apply {
            artistMbid = safeArgs.artistMbid
            artistName = safeArgs.artistName
            artistNameLiveData.observe(viewLifecycleOwner) { artistName ->
                setTitle(getString(R.string.title_top, artistName))
            }
            albumsLiveData.observe(viewLifecycleOwner) { albums ->
                adapter.items = albums
            }
            collectLatestLifecycleFlow(navigationAlbumFlow) { navigationData ->
                val direction =
                    ArtistTopAlbumsFragmentDirections.actionNavigationArtistTopAlbumsToAlbumDetailsFragment(
                        albumId = navigationData.mbid,
                        albumName = navigationData.name
                    )
                navigate(direction)
            }
            collectLatestLifecycleFlow(errorSnackBarFlow) { errorMessage ->
                showSnackBar(errorMessage)
            }
            start()
        }
    }
}