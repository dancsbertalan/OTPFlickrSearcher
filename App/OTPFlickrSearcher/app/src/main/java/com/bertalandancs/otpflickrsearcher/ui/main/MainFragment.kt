package com.bertalandancs.otpflickrsearcher.ui.main

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.bertalandancs.otpflickrsearcher.data.model.ThumbnailImage
import com.bertalandancs.otpflickrsearcher.databinding.MainFragmentBinding
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import androidx.appcompat.widget.SearchView


class MainFragment : Fragment() {

    private val TAG = "MainFragment"

    private lateinit var viewModel: MainViewModel

    private lateinit var searchView: SearchView
    private lateinit var searchTextListener: SearchView.OnQueryTextListener

    private var _binding: MainFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MainFragmentBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        // TODO: Use the ViewModel
        val dataSet = listOf(ThumbnailImage(1,1,"asd"),ThumbnailImage(1,1,"asd"),ThumbnailImage(1,1,"asd"),ThumbnailImage(1,1,"asd"),ThumbnailImage(1,1,"asd"),ThumbnailImage(1,1,"asd"),ThumbnailImage(1,1,"asd"),ThumbnailImage(1,1,"asd"),ThumbnailImage(1,1,"asd"))
        val adapter = SearchResultsAdapter(dataSet)
        binding.searchResults.apply {
            setHasFixedSize(true)
            this.adapter = adapter
            layoutManager = FlexboxLayoutManager(this@MainFragment.context).apply {
                flexDirection = FlexDirection.ROW
                justifyContent = JustifyContent.CENTER
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}