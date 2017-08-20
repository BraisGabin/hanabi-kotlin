package com.braisgabin.hanabi

import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.instanceOf
import org.junit.Assert.assertThat
import org.junit.Test

class GameTest {

  @Test
  fun ended_for_too_much_fails() {
    gameBuilder {
      with_fails(3)
    }.build()
        .assert()
        .then_is_ended()
  }

  @Test
  fun not_ended_because_few_fails() {
    gameBuilder {
      with_fails(2)
    }.build()
        .assert()
        .then_is_not_ended()
  }

  @Test
  fun ended_because_win() {
    gameBuilder {
      with_table(listOf(5, 5, 5, 5, 5))
    }.build()
        .assert()
        .then_is_ended()
  }

  @Test
  fun not_ended_because_no_win() {
    gameBuilder {
      with_table(listOf(4, 5, 5, 5, 5))
    }.build()
        .assert()
        .then_is_not_ended()
  }

  @Test
  fun ended_because_no_more_turn() {
    gameBuilder {
      with_remaining_turn(0)
    }.build()
        .assert()
        .then_is_ended()
  }

  @Test
  fun not_ended_because_more_turn() {
    gameBuilder {
      with_remaining_turn(2)
    }.build()
        .assert()
        .then_is_not_ended()
  }

  @Test
  fun apply_unknown_action_throws_an_illegal_argument_exception() {
    gameBuilder {
    }.build()
        .when_apply(object : Hanabi.Action {})
        .assert()
        .then_throw_an_illegal_argument_exception()
  }

  @Test
  fun apply_an_action_with_ended_game_throws_an_illegal_state_exception() {
    gameBuilder {
      with_remaining_turn(0)
    }.build()
        .when_discard_card(0)
        .assert()
        .then_throw_an_illegal_state_exception()
  }

  private fun gameBuilder(func: GameBuilder.() -> Unit) = GameBuilder().apply(func)

  class GameBuilder {
    var deck: Hanabi.Deck = Deck(emptyList())
    var hands: List<Hand> = emptyList()
    var table: List<Int> = listOf(0, 0, 0, 0, 0)
    var hints: Int = 8
    var fails: Int = 0
    var remainingTurns: Int? = null

    fun build(): Tester {
      return Tester(Game(deck, hands, table, hints, fails, remainingTurns))
    }

    fun with_fails(fails: Int) {
      this.fails = fails
    }

    fun with_table(table: List<Int>) {
      this.table = table
    }

    fun with_remaining_turn(remainingTurns: Int?) {
      this.remainingTurns = remainingTurns
    }
  }

  class Tester(private var game: Hanabi?) {
    private var exception: Throwable? = null

    fun when_play_card(i: Int): Tester {
      return apply(ActionPlay(i))
    }

    fun when_discard_card(i: Int): Tester {
      return apply(ActionDiscard(i))
    }

    fun when_gives_a_color_hint(player: Int, color: Int): Tester {
      return apply(ActionHintColor(player, color))
    }

    fun when_gives_a_number_hint(player: Int, number: Int): Tester {
      return apply(ActionHintNumber(player, number))
    }

    fun when_apply(action: Hanabi.Action): Tester {
      return apply(action)
    }

    fun assert(): Assertions {
      return Assertions(game, exception)
    }

    private fun apply(action: Hanabi.Action): Tester {
      try {
        game = game!!.apply(action)
      } catch (ex: Throwable) {
        game = null
        exception = ex
      }
      return this
    }
  }

  class Assertions(private val game: Hanabi?, private val exception: Throwable?) {

    fun then_is_ended(): Assertions {
      assertThat(game!!.ended, `is`(true))
      return this
    }

    fun then_is_not_ended(): Assertions {
      assertThat(game!!.ended, `is`(false))
      return this
    }

    fun then_throw_an_illegal_argument_exception(): Assertions {
      return exception(IllegalArgumentException::class.java)
    }

    fun then_throw_an_illegal_state_exception(): Assertions {
      return exception(IllegalStateException::class.java)
    }

    private fun exception(clazz: Class<out Throwable>): Assertions {
      assertThat(exception, `is`(instanceOf(clazz)))
      return this
    }
  }
}
