package com.example.myapplication.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.PageContentItemBinding
import com.example.myapplication.model.AdsViewModel
import com.example.myapplication.model.Content
import com.example.myapplication.model.PageContent
import com.example.myapplication.pdf.PdfUtils
import com.facebook.ads.AdOptionsView
import com.facebook.ads.MediaView
import com.facebook.ads.NativeAd
import com.facebook.ads.NativeAdLayout
import com.facebook.ads.NativeAdsManager
import com.example.myapplication.R


class ViewContentAdapter(
    val context: Context,
    var nativeAdsManager: NativeAdsManager?,
    val onclick:(Int) -> Unit): ListAdapter<Any, RecyclerView.ViewHolder>(differ) {

    private val mAdItem = ArrayList<NativeAd>()

    class PageHolder(pageItem:View): RecyclerView.ViewHolder(pageItem) {


    }
    class AdsHolder(adNative:NativeAdLayout): RecyclerView.ViewHolder(adNative) {
        val adLayout = adNative
        val mvAdMedia:MediaView = adLayout.findViewById(R.id.native_ad_media)
        val tvAdTitle:TextView = adLayout.findViewById(R.id.native_ad_title)
        val tvAdBody:TextView = adLayout.findViewById(R.id.native_ad_body)
        val tvAdSocialContext:TextView = adLayout.findViewById(R.id.native_ad_social_context)
        val tvAdSponsoredLabel:TextView = adLayout.findViewById(R.id.native_ad_sponsored_label)
        val btnAdCallToAction:Button = adLayout.findViewById(R.id.native_ad_call_to_action)
        val ivAdIcon:MediaView = adLayout.findViewById(R.id.native_ad_icon)
        val adChoicesContainer:LinearLayout = adLayout.findViewById(R.id.ad_choices_container)
    }
    class SpaceHolder(spaceItem:View): RecyclerView.ViewHolder(spaceItem) {

    }

    override fun getItemViewType(position: Int): Int {
        return when(getItem(position)) {
            is Content -> PAGE
            is AdsViewModel -> ADS
            else -> SPACE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
       return when(viewType){
           PAGE -> {
                val pageView = PageContentItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                PageHolder(pageView.root)
            }
           ADS -> {
               val adsView = LayoutInflater.from(parent.context).inflate(R.layout.native_ad_unit, parent, false) as NativeAdLayout
               AdsHolder(adsView)
           }
           else -> {
               val pageView = PageContentItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
               PageHolder(pageView.root)
           }

        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {

            is PageHolder -> {
                val page = getItem(position)
                if(page is Content){
                    val image = holder.itemView.findViewById<ImageView>(R.id.pageImageItem)
                    val pageBitmap = PdfUtils(context).openPdf(page.page)

                    if (pageBitmap != null) {
                        image.setImageBitmap(pageBitmap)
                    }
                    else{
                        Log.d("error bitmap00", "bitmap: null")
                    }

                        image.setOnClickListener{
                            onclick(page.page)
                        }

                }
            }

            is AdsHolder -> {
                val ad: NativeAd?
                val ads = getItem(position)
                if (ads is AdsViewModel){
                    val adLayout:NativeAdLayout = holder.adLayout
                    holder.adChoicesContainer.removeAllViews()
                    if (nativeAdsManager != null && nativeAdsManager!!.isLoaded){
                        ad = nativeAdsManager!!.nextNativeAd()
                        if(ad != null && !ad.isAdInvalidated){
                            mAdItem.add(ad)
                            Log.d("Ad ManagerX", "Ad error inside: ${ad.isAdInvalidated}")
                            holder.tvAdTitle.text = ad.advertiserName
                            holder.tvAdBody.text = ad.adBodyText
                            holder.tvAdSocialContext.text = ad.adSocialContext
                            holder.tvAdSponsoredLabel.setText(R.string.sponsored)
                            holder.btnAdCallToAction.text = ad.adCallToAction
                            holder.btnAdCallToAction.visibility = if (ad.hasCallToAction()) View.VISIBLE else View.INVISIBLE
                            val adOptionsView = AdOptionsView(context, ad, adLayout)
                            holder.adChoicesContainer.addView(adOptionsView, 0)

                            val clickableViews: MutableList<View> = ArrayList()
                            clickableViews.add(holder.ivAdIcon)
                            clickableViews.add(holder.mvAdMedia)
                            clickableViews.add(holder.btnAdCallToAction)
                            ad.registerViewForInteraction(
                                adLayout,
                                holder.mvAdMedia,
                                holder.ivAdIcon,
                                clickableViews
                            )
                        }
                    }else {
                        Log.d("Ad ManagerX", "error : ${nativeAdsManager!!.isLoaded}")
                    }
                }
            }
        }
    }

    companion object {
        private const val PAGE = 0
        private const val ADS = 1
        private const val SPACE = 2
        private const val AD_DISPLAY_FREQUENCY = 5

        val differ = object : DiffUtil.ItemCallback<Any>() {
            override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean {
                if (oldItem is PageContent && newItem is PageContent){
                    return oldItem == newItem
                }
                return false
            }

            override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean {
                if (oldItem is PageContent && newItem is PageContent) {
                    return oldItem.fromPage == newItem.fromPage && oldItem.toPage == newItem.toPage
                }
                return false
            }

        }
    }


}