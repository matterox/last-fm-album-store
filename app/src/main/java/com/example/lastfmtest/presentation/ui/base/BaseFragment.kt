package com.example.lastfmtest.presentation.ui.base

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.example.lastfmtest.presentation.helper.showActionSnackBar
import com.example.lastfmtest.presentation.helper.showShortSnackBar

abstract class BaseFragment: Fragment() {
    protected fun showSnackBar(message: String) {
        showShortSnackBar(message)
    }

    protected fun showUndoSnackBar(message: String, actionText: String, action: () -> (Unit), dismissAction: (() -> Unit)) {
        showActionSnackBar(message, actionText, action, dismissAction)
    }

    protected fun navigate(directions: NavDirections) {
        findNavController().navigate(directions)
    }

    protected fun setTitle(title: String) {
        (activity as AppCompatActivity).supportActionBar?.title = title
    }
}