package com.braisgabin.hanabi

import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert.assertThat
import org.junit.Test

class GameTest {

  @Test
  fun ended_for_too_much_fails() {
    GameBuilder()
        .with_fails(3)
        .build()
        .then_is_ended()
  }

  @Test
  fun not_ended_because_few_fails() {
    GameBuilder()
        .with_fails(2)
        .build()
        .then_is_not_ended()
  }

  @Test
  fun ended_because_win() {
    GameBuilder()
        .with_table(listOf(5, 5, 5, 5, 5))
        .build()
        .then_is_ended()
  }

  @Test
  fun not_ended_because_no_win() {
    GameBuilder()
        .with_table(listOf(4, 5, 5, 5, 5))
        .build()
        .then_is_not_ended()
  }

  @Test
  fun ended_because_no_more_turn() {
    GameBuilder()
        .with_remaining_turn(0)
        .build()
        .then_is_ended()
  }

  @Test
  fun not_ended_because_more_turn() {
    GameBuilder()
        .with_remaining_turn(2)
        .build()
        .then_is_not_ended()
  }

  class GameBuilder {
    var deck: List<Card> = emptyList()
    var hands: List<Hand> = emptyList()
    var table: List<Int> = listOf(0, 0, 0, 0, 0)
    var hints: Int = 8
    var fails: Int = 0
    var remainingTurns: Int? = null

    fun build(): Assertions {
      return Assertions(Game(deck, hands, table, hints, fails, remainingTurns))
    }

    fun with_fails(fails: Int): GameBuilder {
      this.fails = fails
      return this
    }

    fun with_table(table: List<Int>): GameBuilder {
      this.table = table
      return this
    }

    fun with_remaining_turn(remainingTurns: Int?): GameBuilder {
      this.remainingTurns = remainingTurns
      return this
    }
  }

  class Assertions(val game: Hanabi) {

    fun then_is_ended(): Assertions {
      assertThat(game.ended, `is`(true))
      return this
    }

    fun then_is_not_ended(): Assertions {
      assertThat(game.ended, `is`(false))
      return this
    }
  }
}
