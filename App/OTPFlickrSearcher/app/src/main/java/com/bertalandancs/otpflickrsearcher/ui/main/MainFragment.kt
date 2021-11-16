package com.bertalandancs.otpflickrsearcher.ui.main

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.app.SharedElementCallback
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bertalandancs.otpflickrsearcher.R
import com.bertalandancs.otpflickrsearcher.databinding.MainFragmentBinding
import com.bertalandancs.otpflickrsearcher.ui.main.adapter.SearchResultsAdapter
import com.bertalandancs.otpflickrsearcher.ui.main.model.ThumbnailImage
import com.bertalandancs.otpflickrsearcher.utils.Utils
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import io.reactivex.rxjava3.disposables.CompositeDisposable
import timber.log.Timber


class MainFragment(
    private val viewModel: MainViewModel,
    private val sharedPreferences: SharedPreferences
) : Fragment() {

    private lateinit var searchView: SearchView

    private var _binding: MainFragmentBinding? = null
    private val binding get() = _binding!!
    private val disposable: CompositeDisposable = CompositeDisposable()
    private val searchResultsAdapter = SearchResultsAdapter()
    private var firstOpen = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MainFragmentBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelableArrayList(
            CURRENT_RESULTS,
            searchResultsAdapter.results as ArrayList<ThumbnailImage>
        )
        super.onSaveInstanceState(outState)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu, menu)

        val searchItem = menu.findItem(R.id.search_item)
        searchView = searchItem?.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String): Boolean {
                Timber.d("onQueryTextSubmit: $query")
                Utils.hideKeyboardFrom(requireContext(), searchView)
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
        initSubscriptions()
        initView()

        if (savedInstanceState != null)
            savedInstanceState.getParcelableArrayList<ThumbnailImage>(CURRENT_RESULTS)?.let {
                searchResultsAdapter.results = it
            }
        else
            if (firstOpen) {
                initialSearch()
                firstOpen = false
            }
    }

    private fun initView() {
        with(binding.searchResults) {
            setHasFixedSize(true)
            layoutManager = FlexboxLayoutManager(this@MainFragment.context).apply {
                adapter = searchResultsAdapter
                flexDirection = FlexDirection.ROW
                justifyContent = JustifyContent.CENTER
            }
            this.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if (!binding.progressIndicator.isVisible &&
                        !recyclerView.canScrollVertically(1)
                    )
                        viewModel.nextImages()
                }
            })
        }
    }

    private fun initialSearch() {
        var query = FIRST_LAUNCH_QUERY
        if (sharedPreferences.contains(LAST_SEARCH))
            query = sharedPreferences.getString(LAST_SEARCH, LAST_SEARCH).toString()
        Timber.d("initialSearch with $query")
        viewModel.getImages(query)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        disposable.clear()
        viewModelStore.clear()
    }

    private fun initSubscriptions() {
        disposable.add(
            viewModel.searchStatus.subscribe({
                Timber.d("download images onNext: $it")
                when (it) {
                    is SearchStatus.Loading -> {
                        binding.progressIndicator.isVisible = it.isLoading
                    }
                    is SearchStatus.Ok -> {
                        if (it.page > 1)
                            searchResultsAdapter.addToResultList(it.thumbnails)
                        else
                            searchResultsAdapter.results = it.thumbnails
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
                    Timber.e("download images error: $it")
                })
        )
    }

    companion object {
        private const val CURRENT_RESULTS = "CURRENT_RESULTS"
        private const val LAST_SEARCH: String = "LAST_SEARCH"

        private const val FIRST_LAUNCH_QUERY = "dog"

    }
}