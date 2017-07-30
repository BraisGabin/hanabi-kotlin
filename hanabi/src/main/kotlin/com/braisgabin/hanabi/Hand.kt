package com.braisgabin.hanabi

data class Hand(private val cards: List<Card>) {
  val size: Int = cards.size

  operator fun get(i: Int) = cards[i]

  fun replaceCard(index: Int, card: Card?): Hand {
    val mutableList = this.cards.toMutableList()
    mutableList.removeAt(index)
    if (card != null) {
      mutableList.add(card)
    }
    return Hand(mutableList)
  }

  fun hasColor(color: Int): Boolean {
    return cards.any { it.color == color }
  }

  fun hasNumber(number: Int): Boolean {
    return cards.any { it.number == number }
  }
}
