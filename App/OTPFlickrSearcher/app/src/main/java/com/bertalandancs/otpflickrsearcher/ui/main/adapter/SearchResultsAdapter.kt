package com.bertalandancs.otpflickrsearcher.ui.main.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bertalandancs.otpflickrsearcher.R
import com.bertalandancs.otpflickrsearcher.databinding.SearchResultItemBinding
import com.bertalandancs.otpflickrsearcher.ui.main.model.ThumbnailImage
import com.squareup.picasso.Picasso

class SearchResultsAdapter : RecyclerView.Adapter<SearchResultsAdapter.ViewHolder>() {

    private lateinit var binding: SearchResultItemBinding

    var results: List<ThumbnailImage> = ArrayList()

    fun setResultList(results: List<ThumbnailImage>) {
        this.results = results
        notifyItemRangeChanged(0, results.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding =
            SearchResultItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.onBind(position)

    override fun getItemCount() = results.size

    inner class ViewHolder(private val binding: SearchResultItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun onBind(position: Int) {
            val item = results[position]
            Log.i(TAG, "onBind for ${item.url}")
            Picasso.get().load(item.url).into(binding.image)

            binding.imageCard.setOnClickListener {
                Navigation.findNavController(itemView)
                    .navigate(
                        R.id.action_mainFragment_to_detailsFragment
                    )
            }
        }
    }

    companion object {
        private const val TAG: String = "SearchResultsAdapter"
    }
}