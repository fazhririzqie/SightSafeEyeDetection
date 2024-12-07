package com.example.sightsafe.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sightsafe.databinding.ItemHistoryBinding
import com.squareup.picasso.Picasso

class ResultAdapter(private val resultList: List<Result>) : RecyclerView.Adapter<ResultAdapter.ResultViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultViewHolder {
        val binding = ItemHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ResultViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ResultViewHolder, position: Int) {
        val result = resultList[position]
        holder.bind(result)
    }

    override fun getItemCount(): Int = resultList.size

    class ResultViewHolder(private val binding: ItemHistoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(result: Result) {
            binding.Disease.text = result.resultText
            Picasso.get().load(result.imageUri).into(binding.tvPicture)
        }
    }
}