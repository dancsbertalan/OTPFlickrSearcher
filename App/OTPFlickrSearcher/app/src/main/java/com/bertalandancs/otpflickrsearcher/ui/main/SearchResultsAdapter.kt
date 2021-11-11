package com.bertalandancs.otpflickrsearcher.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bertalandancs.otpflickrsearcher.R
import com.bertalandancs.otpflickrsearcher.data.model.ThumbnailImage
import com.bertalandancs.otpflickrsearcher.databinding.SearchResultItemBinding

class SearchResultsAdapter(private val dataSet: List<ThumbnailImage>) :
    RecyclerView.Adapter<SearchResultsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = SearchResultItemBinding.inflate(inflater)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(dataSet[position])

    override fun getItemCount(): Int = dataSet.size

    inner class ViewHolder(private val binding: SearchResultItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(thumbnailImage: ThumbnailImage) {
            //TODO: Load images with Picasso or Glide
            binding.imageCard.setOnClickListener {
                Navigation.findNavController(itemView)
                    .navigate(R.id.action_mainFragment_to_detailsFragment)
            }
        }
    }
}