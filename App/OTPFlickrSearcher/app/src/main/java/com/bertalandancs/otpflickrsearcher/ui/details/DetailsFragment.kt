package com.bertalandancs.otpflickrsearcher.ui.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.bertalandancs.otpflickrsearcher.R
import com.bertalandancs.otpflickrsearcher.data.model.GetInfoPhoto
import com.bertalandancs.otpflickrsearcher.databinding.DetailsFragmentBinding
import com.bertalandancs.otpflickrsearcher.view.DetailsItem
import com.squareup.picasso.Picasso
import io.reactivex.rxjava3.disposables.CompositeDisposable
import timber.log.Timber

class DetailsFragment(private val viewModel: DetailsViewModel) : Fragment() {
    private var _binding: DetailsFragmentBinding? = null
    private val binding get() = _binding!!
    private val disposable: CompositeDisposable = CompositeDisposable()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DetailsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initSubscriptions()
        //TODO: Handle when IMAGE_ID is null
        val imageId = arguments?.getLong(IMAGE_ID)
        if (imageId != 0L)
            viewModel.getImageInfo(imageId)
    }

    private fun initSubscriptions() {
        disposable.add(viewModel.infoStatus.subscribe({
            Timber.d("download image infos onNext: $it")
            when (it) {
                is InfoStatus.Loading -> {
                    binding.progressIndicator.isVisible = it.isLoading
                }
                is InfoStatus.Ok -> setDetails(it.getInfoPhoto)
                is InfoStatus.Fail -> Toast.makeText(
                    requireContext(),
                    "${requireContext().getText(R.string.msg_error_from_server)} ${it.errorCode}",
                    Toast.LENGTH_LONG
                )
                    .show()
                is InfoStatus.InternetError -> Toast.makeText(
                    requireContext(),
                    R.string.msg_internet_error,
                    Toast.LENGTH_LONG
                ).show()
                is InfoStatus.UnknownError -> Toast.makeText(
                    requireContext(),
                    "${requireContext().getText(R.string.msg_unknown_error)} ${it.throwable.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }, {
            Timber.e("download image infos error: $it")
        }))
    }

    private fun setDetails(infoPhoto: GetInfoPhoto) {
        Timber.d("setDetails for photo: $infoPhoto")
        with(binding) {
            title.title = infoPhoto.title
            title.value = infoPhoto.description ?: ""

            val imageUrl = createImageUrl(infoPhoto)
            Timber.d("image url: $imageUrl")
            //TODO: Handle too large (Canvas: trying to draw too large(120422400bytes) bitmap.)
            Picasso.get().load(imageUrl).into(binding.detailedImage)

            ownerName.value = infoPhoto.owner.username
            ownerLocation.value = infoPhoto.owner.location

            if (infoPhoto.visibility.isPublic == 1) {
                publicVisible.isVisible = true
                familyVisible.isVisible = false
                friendsVisible.isVisible = false
            } else {
                publicVisible.isVisible = false
                if (infoPhoto.visibility.isFamily == 1)
                    familyVisible.isVisible = true
                if (infoPhoto.visibility.isFriend == 1)
                    friendsVisible.isVisible = true
            }

            if (infoPhoto.tags != null && !infoPhoto.tags.tagList.isNullOrEmpty())
                for (tag in infoPhoto.tags.tagList)
                    tagsContainer.addView(DetailsItem(requireContext(), null).apply {
                        value = tag.raw
                    })

            detailedImage.setOnClickListener {
                Navigation.findNavController(it)
                    .navigate(R.id.action_detailsFragment_to_fullscreenFragment, Bundle().apply {
                        putString(IMAGE_URL_STRING, imageUrl)
                    })
            }

            detailsContainer.isVisible = true
        }
    }

    private fun createImageUrl(infoPhoto: GetInfoPhoto): String {
        var secret = infoPhoto.originalSecret
        var size = "o"
        if (secret == null) {
            secret = infoPhoto.secret
            size = "b"
        }
        return "https://live.staticflickr.com/${infoPhoto.server}/${infoPhoto.id}_${secret}_$size.jpg"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        disposable.clear()
        viewModelStore.clear()
    }

    companion object {
        private const val IMAGE_ID = "IMAGE_ID"
        private const val IMAGE_URL_STRING = "IMAGE_URL_STRING"
    }
}

