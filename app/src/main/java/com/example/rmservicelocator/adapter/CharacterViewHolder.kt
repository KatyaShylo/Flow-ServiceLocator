package com.example.rmservicelocator.adapter

import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.rmservicelocator.databinding.ItemCharacterBinding
import com.example.rmservicelocator.retrofit.Character


class CharacterViewHolder(
    private val binding: ItemCharacterBinding,
    private val onCharacterClicked: (Character) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: Character) {
        with(binding) {
            imageAvatar.load(item.image)
            textName.text = item.name

            root.setOnClickListener {
                onCharacterClicked(item)
            }
        }
    }
}