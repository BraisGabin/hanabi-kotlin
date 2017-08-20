package com.braisgabin.hanabi

import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException

class DeckTest {

  @Rule
  @JvmField
  val thrown: ExpectedException = ExpectedException.none()

  private val card1 = Card(1, 1)
  private val card2 = Card(2, 2)

  @Test
  fun size() {
    assertThat(Deck(listOf(card1)).size, `is`(1))
  }

  @Test
  fun empty() {
    assertThat(Deck(listOf()).isEmpty, `is`(true))
  }

  @Test
  fun no_empty() {
    assertThat(Deck(listOf(card1)).isEmpty, `is`(false))
  }

  @Test
  fun draw_card() {
    assertThat(Deck(listOf(card1, card2)).drawCard(), `is`(Deck.DrawCard(card2, Deck(listOf(card1)))))
  }

  @Test
  fun draw_card_with_empty_deck_trows_IllegalStateException() {
    thrown.expect(IllegalStateException::class.java)
    Deck(listOf()).drawCard()
  }

  @Test
  fun get_0() {
    assertThat(Deck(listOf(card1, card2))[0], `is`(card1))
  }

  @Test
  fun get_1() {
    assertThat(Deck(listOf(card1, card2))[1], `is`(card2))
  }
}
