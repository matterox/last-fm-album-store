package com.example.lastfmtest.presentation.ui.albumdetails

import android.os.Build
import android.os.Bundle
import android.text.Html
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.lastfmtest.R
import com.example.lastfmtest.databinding.FragmentAlbumDetailsBinding
import com.example.lastfmtest.presentation.helper.collectLatestLifecycleFlow
import com.example.lastfmtest.presentation.helper.loadAndCacheImage
import com.example.lastfmtest.presentation.ui.adapters.TrackAdapter
import com.example.lastfmtest.presentation.ui.base.BaseFragment
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AlbumDetailsFragment: BaseFragment() {
    private val binding: FragmentAlbumDetailsBinding by viewBinding(FragmentAlbumDetailsBinding::bind)
    private val albumDetailsViewModel: AlbumDetailsViewModel by viewModels()
    private val safeArgs: AlbumDetailsFragmentArgs by navArgs()
    private val adapter: TrackAdapter = TrackAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentAlbumDetailsBinding.inflate(inflater, container, false).root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvTracks.adapter = adapter

        albumDetailsViewModel.apply {
            albumMbid = safeArgs.albumId
            albumName = safeArgs.albumName
            albumNameLiveData.observe(viewLifecycleOwner) { albumName ->
                setTitle(getString(R.string.title_album_details, albumName))
            }
            collectLatestLifecycleFlow(albumsLiveData) { albumDetailsData ->
                binding.ivAlbumCover.loadAndCacheImage(
                    requireContext(),
                    albumDetailsData.cover
                )
                updateTagChips(albumDetailsData.tags)
                binding.tvAlbumName.text = albumDetailsData.name
                binding.tvArtistName.text = albumDetailsData.artist
                binding.tvSummary.text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    Html.fromHtml("Summary: ${albumDetailsData.summary}", Html.FROM_HTML_MODE_COMPACT)
                } else {
                    Html.fromHtml("Summary: ${albumDetailsData.summary}")
                }
                binding.tvSummary.movementMethod = LinkMovementMethod.getInstance()
                binding.tvSummary.isVisible = albumDetailsData.summary.isNotBlank()
                adapter.items = albumDetailsData.tracks
            }
            collectLatestLifecycleFlow(errorSnackBarFlow) { errorMessage ->
                showSnackBar(errorMessage)
            }
            start()
        }
    }

    private fun updateTagChips(tags: List<String>) {
        binding.chipTags.removeAllViews()
        tags.forEach { tag ->
            val chip = Chip(requireContext())
            chip.text = tag
            chip.isEnabled = false
            binding.chipTags.addView(chip)
        }
    }
}