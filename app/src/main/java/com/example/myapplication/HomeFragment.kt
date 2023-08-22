package com.example.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.slidingpanelayout.widget.SlidingPaneLayout
import com.example.myapplication.adapter.ContentAdapter
import com.example.myapplication.data.DataManager
import com.example.myapplication.databinding.FragmentHomeBinding
import com.example.myapplication.livedata.ContentModel


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var adapter:ContentAdapter
    private lateinit var titleRecycler: RecyclerView
    private lateinit var liveModel:ContentModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)
        liveModel = ViewModelProvider(requireActivity())[ContentModel::class.java]

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner, SlidingPane(binding.slidePane)
        )
        titleRecycler = binding.titleRecycler
        adapter = ContentAdapter {

            if (binding.slidePane.isSlideable){
                binding.slidePane.openPane()
            }
            liveModel.updateValue(it)
        }
        adapter.submitList(DataManager.lesson)
        titleRecycler.layoutManager = LinearLayoutManager(requireActivity())
        titleRecycler.adapter = adapter
    }

    inner class SlidingPane(private val slidePane: SlidingPaneLayout)  : OnBackPressedCallback(
        true
    ), SlidingPaneLayout.PanelSlideListener {

        init {
            slidePane.lockMode = SlidingPaneLayout.LOCK_MODE_LOCKED
            slidePane.addPanelSlideListener(this)
        }

        override fun onPanelSlide(panel: View, slideOffset: Float) {
        }

        override fun onPanelOpened(panel: View) {

        }

        override fun onPanelClosed(panel: View) {

        }

        override fun handleOnBackPressed() {
//            Log.d("fragment oop", "sliding pane: clickable ${slidePane.isSlideable} and is isOpen : ${slidePane.isOpen}")
            if (slidePane.isOpen && slidePane.isSlideable){
                slidePane.closePane()
            }
            else{
               requireActivity().finish()
            }
        }
    }
}