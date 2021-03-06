package com.braisgabin.hanabi

class Game(override val deck: Deck,
           override val hands: List<Hand>,
           override val table: Table,
           override val hints: Int,
           override val fails: Int,
           private val remainingTurns: Int?) : Hanabi {
  override val ended: Boolean
    get() = fails >= 3 || table.points >= 25 || if (remainingTurns == null) false else remainingTurns <= 0

  companion object {
    private fun nextPlayer(hands: List<Hand>): List<Hand> {
      val newHands = hands.toMutableList()
      newHands.add(newHands.removeAt(0))
      return newHands
    }
  }

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
    var hints: Int
    var fails: Int
    var table: Table

    try {
      val card = hands[0][cardIndex]
      table = this.table.playCard(card)
      hints = if (card.number == 5) minOf(8, this.hints + 1) else this.hints
      fails = this.fails
    } catch (ex: Table.UnplayableCardException) {
      table = this.table
      hints = this.hints
      fails = this.fails + 1
    }

    val (deck, hands) = drawCard(cardIndex)

    return Game(deck, nextPlayer(hands), table, hints, fails, calculateRemainingTurns())
  }

  private fun discard(cardIndex: Int): Hanabi {
    val hints = minOf(8, this.hints + 1)
    val (deck, hands) = drawCard(cardIndex)

    return Game(deck, nextPlayer(hands), table, hints, fails, calculateRemainingTurns())
  }

  private fun hint(player: Int, valid: (Hand) -> Boolean): Hanabi {
    if (player <= 0 || player > hands.size) {
      throw IllegalArgumentException("Invalid player id.")
    }
    if (!valid(hands[player])) {
      throw IllegalArgumentException("You can't give this hint.")
    }
    if (hints <= 0) {
      throw IllegalStateException("There are not hints.")
    }
    return Game(deck, hands, table, hints - 1, fails, remainingTurns)
  }

  private fun calculateRemainingTurns(): Int? {
    return when {
      remainingTurns != null -> this.remainingTurns - 1
      deck.size <= 1 -> hands.size
      else -> null
    }
  }

  private fun drawCard(cardIndex: Int): CardPosition {
    val deck: Deck
    val card: Card?
    if (this.deck.isEmpty) {
      deck = this.deck
      card = null
    } else {
      val drawCard = this.deck.drawCard()
      deck = drawCard.deck
      card = drawCard.card
    }

    val hands = this.hands.toMutableList()
    hands[0] = this.hands[0].replaceCard(cardIndex, card)

    return CardPosition(deck, hands)
  }

  private data class CardPosition(val deck: Deck, val hands: List<Hand>)
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
    return Game(deck, hands, Table(listOf(0, 0, 0, 0, 0)), 8, 0, null)
  }
}
