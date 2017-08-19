package com.braisgabin.hanabi

class Game(override val deck: List<Card>,
           override val hands: List<Hand>,
           override val table: List<Int>,
           override val hints: Int,
           override val fails: Int,
           private val remainingTurns: Int?) : Hanabi {
  override val ended: Boolean
    get() = fails >= 3 || table.sum() >= 25 || if (remainingTurns == null) false else remainingTurns <= 0

  override fun apply(action: Hanabi.Action): Hanabi {
    if (ended) {
      throw IllegalStateException("The game is over")
    }
    return when (action) {
      is ActionPlay -> TODO("not implemented")
      is ActionDiscard -> TODO("not implemented")
      is ActionHintColor -> TODO("not implemented")
      is ActionHintNumber -> TODO("not implemented")
      else -> throw IllegalArgumentException("Unknown action $action")
    }
  }
}

class DeckFactory(private val shuffle: (MutableList<out Any>) -> Unit) {

  fun create(): List<Card> {
    val colorCount = 5
    val numbers = listOf(1, 1, 1, 2, 2, 3, 3, 4, 4, 5)
    val deck = mutableListOf<Card>()
    for (color in 0 until colorCount) {
      (0 until numbers.size)
          .map { numbers[it] }
          .mapTo(deck) { Card(color, it) }
    }
    shuffle(deck)
    return deck
  }
}
