package com.braisgabin.hanabi

import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import java.util.*

class DeckFactoryTest {

  @Test
  fun create_deck() {
    val deckFactory = DeckFactory({})
    assertThat(deckFactory.create(), `is`(Deck(cards()) as Hanabi.Deck))
  }

  @Test
  fun create_deck_reverse() {
    val deckFactory = DeckFactory({ Collections.reverse(it) })
    val cards: MutableList<Card> = cards()
    Collections.reverse(cards)
    assertThat(deckFactory.create(), `is`(Deck(cards) as Hanabi.Deck))
  }

  private fun cards(): MutableList<Card> {
    val colorCount = 5
    val numbers = listOf(1, 1, 1, 2, 2, 3, 3, 4, 4, 5)
    val deck = mutableListOf<Card>()
    for (color in 0 until colorCount) {
      (0 until numbers.size)
          .map { numbers[it] }
          .mapTo(deck) { Card(color, it) }
    }
    return deck
  }
}
