package com.example.myapplication.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.model.PageContent

class ContentAdapter(val onClick:(PageContent) -> Unit): ListAdapter <PageContent, ContentAdapter.ContentViewHolder>(diffUtil) {

    override fun getItemViewType(position: Int): Int {
        return when(getItem(position).type){
            PageContent.CHAPTER -> PageContent.CHAPTER
            else -> PageContent.LESSON
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContentViewHolder {
        return when(viewType){
            PageContent.CHAPTER -> ContentViewHolder(
               LayoutInflater.from(parent.context).inflate(R.layout.chapter_content_item, parent, false)
            )
            else -> ContentViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.lesson_content_item, parent, false)
            )
        }
    }

    override fun onBindViewHolder(holder: ContentViewHolder, position: Int) {
        holder.itemView.setOnClickListener{
            onClick(getItem(position))
        }
        holder.bind(getItem(position))
    }


    inner class ContentViewHolder(private val item: View): RecyclerView.ViewHolder(item){
          fun bind(content: PageContent) {
              val text = item.findViewById<TextView>(R.id.itemContentText)
                  text.text = content.title
          }
    }


    companion object {
        val diffUtil = object :DiffUtil.ItemCallback<PageContent>() {

            override fun areItemsTheSame(oldItem: PageContent, newItem: PageContent): Boolean {
                 return  oldItem.title == newItem.title
            }

            override fun areContentsTheSame(oldItem: PageContent, newItem: PageContent): Boolean {
                return oldItem.fromPage == newItem.fromPage && oldItem.toPage == newItem.toPage
            }

        }
    }


}