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
      is ActionPlay -> play(action.cardIndex)
      is ActionDiscard -> discard(action.cardIndex)
      is ActionHintColor -> hint(action.player, { it.hasColor(action.color) })
      is ActionHintNumber -> hint(action.player, { it.hasColor(action.number) })
      else -> throw IllegalArgumentException("Unknown action $action")
    }
  }

  private fun play(cardIndex: Int): Hanabi {
    val fails: Int
    val table: List<Int>

    val card = hands[0][cardIndex]
    if (this.table[card.color] + 1 == card.number) {
      fails = this.fails
      table = this.table.mapIndexed { index, value -> if (index == card.color) value + 1 else value }
    } else {
      fails = this.fails + 1
      table = this.table
    }

    val (deck, hands) = drawCard(cardIndex)

    return Game(deck, hands, table, hints, fails, calculeRemainingTurns())
  }

  private fun discard(cardIndex: Int): Hanabi {
    val hints = minOf(8, this.hints + 1)
    val (deck, hands) = drawCard(cardIndex)

    return Game(deck, hands, table, hints, fails, calculeRemainingTurns())
  }

  private fun hint(player: Int, valid: (Hand) -> Boolean): Hanabi {
    if (!valid(hands.get(player))) {
      throw IllegalArgumentException("You can't give this hint.")
    }
    if (hints <= 0) {
      throw IllegalStateException("You can't give this hint now.")
    }
    return Game(deck, hands, table, hints - 1, fails, remainingTurns)
  }

  private fun calculeRemainingTurns(): Int? {
    val remainingTurns: Int?
    if (this.remainingTurns != null) {
      remainingTurns = this.remainingTurns - 1
    } else if (deck.size <= 1) {
      remainingTurns = hands.size
    } else {
      remainingTurns = null
    }
    return remainingTurns
  }

  private fun drawCard(cardIndex: Int): CardPosition {
    val deck = deck.toMutableList()
    val newCard = deck.removeAt(deck.size - 1)
    val hands = mutableListOf<Hand>()
    for (i in 1..this.hands.size - 1) {
      hands.add(this.hands[i])
    }
    hands.add(this.hands[0].replaceCard(cardIndex, newCard))

    return CardPosition(deck, hands)
  }

  private data class CardPosition(val deck: List<Card>, val hands: List<Hand>)
}
