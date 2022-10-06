package com.example.rmservicelocator.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.rmservicelocator.databinding.ItemCharacterBinding
import com.example.rmservicelocator.retrofit.Character

class CharacterAdapter(
    context: Context,
    private val onCharacterClicked: (Character) -> Unit
) : ListAdapter<Character, CharacterViewHolder>(DIFF_UTIL) {

    private val layoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder {
        return CharacterViewHolder(
            binding = ItemCharacterBinding.inflate(layoutInflater, parent, false),
            onCharacterClicked = onCharacterClicked
        )
    }

    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {

        private val DIFF_UTIL = object :
            DiffUtil.ItemCallback<Character>() {
            override fun areItemsTheSame(oldItem: Character, newItem: Character): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Character, newItem: Character): Boolean {
                return (oldItem.name == newItem.name &&
                        oldItem.image == newItem.image)
            }
        }
    }
}