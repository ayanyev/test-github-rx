package com.eazzyapps.test.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.eazzyapps.test.ARG_REPO_ID
import com.eazzyapps.test.R
import com.eazzyapps.test.databinding.FragmentDetailsBinding
import com.eazzyapps.test.ui.viewmodels.DetailsViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DetailsFragment : Fragment() {

    @Inject
    lateinit var detailsViewModelFactory: DetailsViewModel.AssistedFactory

    private val vm: DetailsViewModel by viewModels {
        val repoId = arguments?.getInt(ARG_REPO_ID)
            ?: throw Exception(getString(R.string.exception_no_arg, ARG_REPO_ID))
        DetailsViewModel.provideFactory(detailsViewModelFactory, repoId)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        (requireActivity() as? AppCompatActivity)?.apply {
            title = getString(R.string.title_details)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
        return FragmentDetailsBinding.inflate(inflater, container, false)
            .apply { this.viewModel = vm }
            .root
    }

    override fun onResume() {
        super.onResume()
        vm.onResume()
    }

    override fun onPause() {
        super.onPause()
        vm.onPause()
    }

    companion object {
        fun newInstance() = DetailsFragment()
    }

}