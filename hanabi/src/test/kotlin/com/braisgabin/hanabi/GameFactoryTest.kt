package com.braisgabin.hanabi

import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule


class GameFactoryTest {

  @Rule
  @JvmField
  val rule: MockitoRule = MockitoJUnit.rule()

  @Rule
  @JvmField
  val thrown: ExpectedException = ExpectedException.none()

  @Mock
  private lateinit var deckFactory: DeckFactory

  private lateinit var gameFactory: GameFactory

  @Before
  fun setUp() {
    gameFactory = GameFactory(deckFactory)

    `when`(deckFactory.create()).thenReturn(Deck(cards(0 until 50)))
  }

  @Test
  fun create_1_player_throw_illegal_exception() {
    thrown.expect(IllegalArgumentException::class.java)
    gameFactory.create(1)
  }

  @Test
  fun create_2_player_throw_illegal_exception() {
    val game = gameFactory.create(2)
    assertThat(game.deck, `is`(Deck(cards(0 until 40)) as Hanabi.Deck))
    assertThat(game.hands, `is`(listOf(
        Hand(hand(45 until 50)),
        Hand(hand(40 until 45))
    )))
    assertThat(game.table, `is`(listOf(0, 0, 0, 0, 0)))
    assertThat(game.hints, `is`(8))
    assertThat(game.fails, `is`(0))
    assertThat(game.ended, `is`(false))
  }

  @Test
  fun create_3_player_throw_illegal_exception() {
    val game = gameFactory.create(3)
    assertThat(game.deck, `is`(Deck(cards(0 until 35)) as Hanabi.Deck))
    assertThat(game.hands, `is`(listOf(
        Hand(hand(45 until 50)),
        Hand(hand(40 until 45)),
        Hand(hand(35 until 40))
    )))
    assertThat(game.table, `is`(listOf(0, 0, 0, 0, 0)))
    assertThat(game.hints, `is`(8))
    assertThat(game.fails, `is`(0))
    assertThat(game.ended, `is`(false))
  }

  @Test
  fun create_4_player_throw_illegal_exception() {
    val game = gameFactory.create(4)
    assertThat(game.deck, `is`(Deck(cards(0 until 34)) as Hanabi.Deck))
    assertThat(game.hands, `is`(listOf(
        Hand(hand(46 until 50)),
        Hand(hand(42 until 46)),
        Hand(hand(38 until 42)),
        Hand(hand(34 until 38))
    )))
    assertThat(game.table, `is`(listOf(0, 0, 0, 0, 0)))
    assertThat(game.hints, `is`(8))
    assertThat(game.fails, `is`(0))
    assertThat(game.ended, `is`(false))
  }

  @Test
  fun create_5_player_throw_illegal_exception() {
    val game = gameFactory.create(5)
    assertThat(game.deck, `is`(Deck(cards(0 until 30)) as Hanabi.Deck))
    assertThat(game.hands, `is`(listOf(
        Hand(hand(46 until 50)),
        Hand(hand(42 until 46)),
        Hand(hand(38 until 42)),
        Hand(hand(34 until 38)),
        Hand(hand(30 until 34))
    )))
    assertThat(game.table, `is`(listOf(0, 0, 0, 0, 0)))
    assertThat(game.hints, `is`(8))
    assertThat(game.fails, `is`(0))
    assertThat(game.ended, `is`(false))
  }

  @Test
  fun create_6_player_throw_illegal_exception() {
    thrown.expect(IllegalArgumentException::class.java)
    gameFactory.create(6)
  }

  private fun cards(range: IntProgression): List<Card> {
    val deck = mutableListOf<Card>()
    range
        .mapTo(deck) { Card(it, 1) }
    return deck
  }

  private fun hand(range: IntProgression): List<Card> {
    return cards(range.reversed())
  }
}
