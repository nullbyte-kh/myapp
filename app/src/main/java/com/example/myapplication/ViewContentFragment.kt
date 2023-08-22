package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.adapter.ViewContentAdapter
import com.example.myapplication.data.ListUtils
import com.example.myapplication.databinding.FragmentContentBinding
import com.example.myapplication.livedata.ContentModel
import com.facebook.ads.AdError
import com.facebook.ads.AdSettings
import com.facebook.ads.AudienceNetworkAds
import com.facebook.ads.NativeAdsManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class ViewContentFragment : Fragment(R.layout.fragment_content) , NativeAdsManager.Listener {

    private val placementID = "553643662682778_1153724779341327"
    private var nativeAdsManager: NativeAdsManager? = null
    private lateinit var liveModel:ContentModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapterView: ViewContentAdapter
    private lateinit var handler: Handler
    private lateinit var runnable: Runnable

    @SuppressLint("SuspiciousIndentation")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val binding = FragmentContentBinding.bind(view)
        liveModel = ViewModelProvider(requireActivity())[ContentModel::class.java]
        recyclerView = binding.viewContentRecycler
        recyclerView.layoutManager = SmoothScrollManager(requireContext())
        recyclerView.smoothScrollBy(0, 1)

        AudienceNetworkAds.initialize(requireContext())
        AdSettings.addTestDevice("d91e84f0-d57c-48e1-be87-9b0fa95843da")

        nativeAdsManager = NativeAdsManager(requireContext(), placementID, 2)
        nativeAdsManager!!.setListener(this)
        nativeAdsManager!!.loadAds()
        adapterView = ViewContentAdapter(requireContext(), nativeAdsManager) {
            val bundle = Bundle()
                bundle.putInt("page", it)
                requireActivity().supportFragmentManager.commit {
                    setReorderingAllowed(false)
                    add(R.id.homeScreenFragmentContainer, ViewerFragment::class.java, bundle)
                }
            Log.d("content bug", "view fragment clicked.")
        }
        //Handler to content list was clicked.

        liveModel.currentValue.observe(requireActivity()) {
            CoroutineScope(Dispatchers.Main).launch {
                binding.loadingGroup.visibility = View.VISIBLE
                delay(500)
                val list = ListUtils.generateList(it.fromPage, it.toPage)
                adapterView.submitList(list)
                recyclerView.adapter = adapterView
                binding.loadingGroup.visibility = View.INVISIBLE
            }
        }

        //Ad refresh handler.

        handler = Handler(Looper.getMainLooper())
        runnable = object : Runnable {
            override fun run() {
                Log.d("Native Manager", "Runnable called.")
                nativeAdsManager!!.loadAds()
                handler.postDelayed(this, 60 * 1000 * 5)
            }
        }
        handler.postDelayed(
           runnable, 60 * 1000 * 5)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onAdsLoaded() {

        Log.d("Native Manager", "Native Manager Loaded")
        adapterView.nativeAdsManager = nativeAdsManager
        adapterView.notifyDataSetChanged()

    }

    override fun onAdError(error: AdError?) {
        Log.d("Native Manager", "Ad Error ${error?.errorMessage}")
    }

    override fun onResume() {
        Log.d("content frag", "onResume")
        handler.removeCallbacks(runnable)
        handler.postDelayed(runnable, 60 * 1000 * 5)
        super.onResume()
    }

    override fun onStop() {
        Log.d("content frag", "onStop")
    handler.removeCallbacks(runnable)
    super.onStop()
    }

    inner class SmoothScrollManager(context:Context): LinearLayoutManager(context) {
        override fun smoothScrollToPosition(
            recyclerView: RecyclerView?,
            state: RecyclerView.State?,
            position: Int
        ) {
            val smoothScroller = object : LinearSmoothScroller(recyclerView?.context){
                override fun calculateSpeedPerPixel(displayMetrics: DisplayMetrics?): Float {
                    return 0.5f
                }
            }
            smoothScroller.targetPosition = position
            startSmoothScroll(smoothScroller)
        }
    }
}