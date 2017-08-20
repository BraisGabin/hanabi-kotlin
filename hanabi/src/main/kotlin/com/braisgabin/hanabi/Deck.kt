package com.braisgabin.hanabi

import com.braisgabin.mockable.Mockable

data class Deck(private val cards: List<Card>) : Hanabi.Deck {
  override val size: Int by lazy { cards.size }

  override fun drawCard(): Hanabi.Deck.DrawCard {
    if (cards.isEmpty()) {
      throw IllegalStateException("You can't draw card from an empty deck")
    }
    val cards = cards.toMutableList()
    val card = cards.removeAt(cards.size - 1)
    return Hanabi.Deck.DrawCard(card, Deck(cards))
  }
}

@Mockable
class DeckFactory(private val shuffle: (MutableList<out Any>) -> Unit) {

  fun create(): Hanabi.Deck {
    val colorCount = 5
    val numbers = listOf(1, 1, 1, 2, 2, 3, 3, 4, 4, 5)
    val cards = mutableListOf<Card>()
    for (color in 0 until colorCount) {
      (0 until numbers.size)
          .map { numbers[it] }
          .mapTo(cards) { Card(color, it) }
    }
    shuffle(cards)
    return Deck(cards)
  }
}
