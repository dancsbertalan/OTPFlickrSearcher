package com.bertalandancs.otpflickrsearcher.ui.main

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.bertalandancs.otpflickrsearcher.R
import com.bertalandancs.otpflickrsearcher.databinding.MainFragmentBinding
import io.reactivex.rxjava3.disposables.CompositeDisposable
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainFragment : Fragment() {

    private val TAG = "MainFragment"

    private val viewModel by viewModel<MainViewModel>()

    private lateinit var searchView: SearchView

    private var _binding: MainFragmentBinding? = null
    private val binding get() = _binding!!
    private val disposable: CompositeDisposable = CompositeDisposable()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MainFragmentBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu, menu)

        val searchItem = menu.findItem(R.id.search_item)
        searchView = searchItem?.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String): Boolean {
                Log.d(TAG, "onQueryTextSubmit: $query")
                viewModel.getImages(query)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return true
            }
        })

        return super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        disposable.add(
            viewModel.images.subscribe({
                Log.d(TAG, "download images onNext: $it")
                when (it) {
                    is SearchStatus.Loading -> binding.progressIndicator.isVisible = it.isLoading
                    is SearchStatus.Ok -> {
                        //TODO: Show images
                    }
                    is SearchStatus.Fail -> Toast.makeText(
                        requireContext(),
                        "${requireContext().getText(R.string.msg_error_from_server)} ${it.errorCode}",
                        Toast.LENGTH_LONG
                    )
                        .show()
                    is SearchStatus.InternetError -> Toast.makeText(
                        requireContext(),
                        R.string.msg_internet_error,
                        Toast.LENGTH_LONG
                    ).show()
                    is SearchStatus.UnknownError -> Toast.makeText(
                        requireContext(),
                        "${requireContext().getText(R.string.msg_unknown_error)} ${it.throwable.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            },
                {
                    Log.e(TAG, "download images list failed by $it")
                })
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        disposable.clear()
        viewModelStore.clear()
    }
}