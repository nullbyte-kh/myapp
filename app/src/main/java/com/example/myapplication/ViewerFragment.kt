package com.example.myapplication

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.example.myapplication.databinding.FragmentViewerBinding
import com.example.myapplication.pdf.PdfUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class ViewerFragment : Fragment(R.layout.fragment_viewer) {

    private var optionInvisible = true
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val binding = FragmentViewerBinding.bind(view)
        val page = arguments?.getInt("page") ?: 1

        binding.zoomImage.setScrollEndListener(object :PinchImageView.ScrollEndListener {
            override fun onScrollEnd() {
                requireActivity().supportFragmentManager.commit {
                    remove(this@ViewerFragment)
                }
            }
        })

        binding.zoomImage.setOnClickListener {

            if (optionInvisible){
                binding.optionGroup.visibility = View.VISIBLE
                optionInvisible = false
                Handler(Looper.getMainLooper()).postDelayed(
                    {
                        binding.optionGroup.visibility = View.INVISIBLE
                        optionInvisible = true
                    },
                    3000
                )
            }
            else{
                binding.optionGroup.visibility = View.INVISIBLE
                optionInvisible = true
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner, BackPressed()
        )
        CoroutineScope(Dispatchers.IO).launch {
            val bm = PdfUtils(requireContext()).openPdf(page)

            withContext(Dispatchers.Main){
                if (bm != null) {
                    binding.zoomImage.setImageBitmap(bm)
                }
                cancel()
            }
        }
    }

    inner class BackPressed: OnBackPressedCallback(
        true
    ){
        override fun handleOnBackPressed() {
            if (isEnabled) requireActivity().supportFragmentManager.commit {
                remove(this@ViewerFragment)
            }
        }

    }
}