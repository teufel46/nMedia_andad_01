package ru.netology.nmedia.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import ru.netology.nmedia.BuildConfig
import ru.netology.nmedia.activity.NewPostFragment.Companion.textArg
import ru.netology.nmedia.databinding.FragmentImagePreviewBinding
import ru.netology.nmedia.view.load

@AndroidEntryPoint
class ImagePreviewFragment  : Fragment()  {

    private var fragmentBinding: FragmentImagePreviewBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentImagePreviewBinding.inflate(
            inflater,
            container,
            false
        )
        fragmentBinding = binding

        val url = arguments?.textArg
        binding.imageView.load("${BuildConfig.BASE_URL}/media/${url}")
        binding.back.setOnClickListener {
            findNavController().navigateUp()
        }

        return binding.root
    }

    override fun onDestroyView() {
        fragmentBinding = null
        super.onDestroyView()
    }
}
