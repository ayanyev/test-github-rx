package com.eazzyapps.test.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.eazzyapps.test.ARG_REPO_ID
import com.eazzyapps.test.R
import com.eazzyapps.test.databinding.FragmentMainBinding
import com.eazzyapps.test.ui.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.subscribeBy

@AndroidEntryPoint
class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private val vm: MainViewModel by viewModels()

    private val disposables = CompositeDisposable()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        (requireActivity() as? AppCompatActivity)?.apply {
            title = getString(R.string.title_main)
            supportActionBar?.setDisplayHomeAsUpEnabled(false)
        }
        return FragmentMainBinding.inflate(inflater, container, false)
            .apply { this.viewModel = vm }
            .root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        disposables.add(
            vm.itemClicks
                .subscribeBy {

                    val fragment = DetailsFragment.newInstance().apply {
                        arguments = bundleOf(ARG_REPO_ID to it)
                    }

                    parentFragmentManager.beginTransaction()
                        .replace(R.id.container, fragment)
                        .addToBackStack(null)
                        .commit()
                }
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.dispose()
    }
}