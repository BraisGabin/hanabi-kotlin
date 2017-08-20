package com.braisgabin.hanabi

class Game(override val deck: Hanabi.Deck,
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

class GameFactory(private val deckFactory: DeckFactory) {

  fun create(players: Int): Game {
    return when (players) {
      2, 3 -> create(players, 5)
      4, 5 -> create(players, 4)
      else -> throw IllegalArgumentException()
    }
  }

  private fun create(players: Int, cards: Int): Game {
    var deck = deckFactory.create()
    val hands = mutableListOf<Hand>()
    for (player in 0 until players) {
      val handCards = mutableListOf<Card>()
      for (i in 0 until cards) {
        val (card, nextDeck) = deck.drawCard()
        deck = nextDeck
        handCards.add(card)
      }
      hands.add(Hand(handCards))
    }
    return Game(deck, hands, listOf(0, 0, 0, 0, 0), 8, 0, null)
  }
}
