package com.example.guessingcard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import java.util.*
import androidx.lifecycle.ViewModel


class CardModel(
    var char: String,
    var isVisible: Boolean = true,
    var isSelect: Boolean = false,
    var id: String = UUID.randomUUID().toString(),
) {}

class CardViewModel : ViewModel() {
    private val numbs: MutableLiveData<MutableList<CardModel>> by lazy {
        MutableLiveData<MutableList<CardModel>>()
    }

    fun getCards(): LiveData<MutableList<CardModel>> {
        return numbs
    }

    fun loadCards() {
        numbs.value = mutableListOf(
            CardModel("1"),
            CardModel("2"),
            CardModel("3"),
            CardModel("4"),
            CardModel("5"),
            CardModel("6"),
            CardModel("1"),
            CardModel("2"),
            CardModel("3"),
            CardModel("4"),
            CardModel("5"),
            CardModel("6"),
        ).apply { shuffle() }
    }

    fun updateShowVisibleCard(id: String) {
        val selects: List<CardModel>? = numbs.value?.filter { it -> it.isSelect }
        val selectCount: Int = selects?.size ?: 0
        var charFind: String = "";
        if (selectCount >= 2) {
            val hasSameChar: Boolean = selects!!.get(0).char == selects.get(1).char
            if (hasSameChar) {
                charFind = selects.get(0).char
            }
        }

        val list: MutableList<CardModel>? = numbs.value?.map { it ->
            if (selectCount >= 2) {
                it.isSelect = false
            }
            if (it.char == charFind) {
                it.isVisible = false
            }
            if (it.id == id) {
                it.isSelect = true
            }

            it
        } as MutableList<CardModel>?

        val visibleCount: Int = list?.filter { it -> it.isVisible }?.size ?: 0
        if (visibleCount <= 0) {
            loadCards()
            return
        }

        numbs.value?.removeAll { true }
        numbs.value = list
    }
}