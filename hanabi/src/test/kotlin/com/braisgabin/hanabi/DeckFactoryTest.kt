package com.braisgabin.hanabi

import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import java.util.*

class DeckFactoryTest {

  @Test
  fun create_deck() {
    val deckFactory = DeckFactory({})
    assertThat(deckFactory.create(), `is`(deck()))
  }

  @Test
  fun create_deck_reverse() {
    val deckFactory = DeckFactory({ Collections.reverse(it) })
    val deck: MutableList<Card> = deck().toMutableList()
    Collections.reverse(deck)
    assertThat(deckFactory.create(), `is`(deck.toList()))
  }

  private fun deck(): List<Card> {
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
