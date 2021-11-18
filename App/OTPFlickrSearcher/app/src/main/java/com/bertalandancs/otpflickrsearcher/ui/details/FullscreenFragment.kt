package com.bertalandancs.otpflickrsearcher.ui.details

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bertalandancs.otpflickrsearcher.databinding.FragmentFullscreenBinding
import com.squareup.picasso.Picasso

class FullscreenFragment : Fragment() {


    private var _binding: FragmentFullscreenBinding? = null
    private val binding get() = _binding!!

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity() as AppCompatActivity).supportActionBar?.hide()
        requireActivity().window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
    }

    override fun onDetach() {
        super.onDetach()
        (requireActivity() as AppCompatActivity).supportActionBar?.show()
        requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFullscreenBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val imageUrl = arguments?.getString(IMAGE_URL_STRING)
        if (imageUrl.isNullOrEmpty())
            findNavController().popBackStack()
        else
            Picasso.get().load(imageUrl).into(binding.photoView)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    companion object {
        private const val IMAGE_URL_STRING = "IMAGE_URL_STRING"
    }

}