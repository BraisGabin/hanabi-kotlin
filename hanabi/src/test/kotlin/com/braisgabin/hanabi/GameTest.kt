package com.braisgabin.hanabi

import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.instanceOf
import org.junit.Assert.assertThat
import org.junit.Test

class GameTest {

  @Test
  fun ended_for_too_much_fails() {
    build {
      with_fails(3)
    } assert {
      then_is_ended()
    }
  }

  @Test
  fun not_ended_because_few_fails() {
    build {
      with_fails(2)
    } assert {
      then_is_not_ended()
    }
  }

  @Test
  fun ended_because_win() {
    build {
      with_table(listOf(5, 5, 5, 5, 5))
    } assert {
      then_is_ended()
    }
  }

  @Test
  fun not_ended_because_no_win() {
    build {
      with_table(listOf(4, 5, 5, 5, 5))
    } assert {
      then_is_not_ended()
    }
  }

  @Test
  fun ended_because_no_more_turn() {
    build {
      with_remaining_turn(0)
    } assert {
      then_is_ended()
    }
  }

  @Test
  fun not_ended_because_more_turn() {
    build {
      with_remaining_turn(2)
    } assert {
      then_is_not_ended()
    }
  }

  @Test
  fun apply_unknown_action_throws_an_illegal_argument_exception() {
    test {
      when_apply(object : Hanabi.Action {})
    } assert {
      then_throw_an_illegal_argument_exception()
    }
  }

  @Test
  fun apply_an_action_with_ended_game_throws_an_illegal_state_exception() {
    build {
      with_remaining_turn(0)
    } test {
      when_discard_card(0)
    } assert {
      then_throw_an_illegal_state_exception()
    }
  }

  private fun build(func: GameBuilder.() -> Unit) = GameBuilder().apply(func)

  private fun test(func: Tester.() -> Unit) = GameBuilder().test(func)

  class GameBuilder {
    var deck: Hanabi.Deck = Deck(emptyList())
    var hands: List<Hand> = emptyList()
    var table: List<Int> = listOf(0, 0, 0, 0, 0)
    var hints: Int = 8
    var fails: Int = 0
    var remainingTurns: Int? = null

    fun with_fails(fails: Int) {
      this.fails = fails
    }

    fun with_table(table: List<Int>) {
      this.table = table
    }

    fun with_remaining_turn(remainingTurns: Int?) {
      this.remainingTurns = remainingTurns
    }

    infix fun test(func: Tester.() -> Unit): Tester {
      return Tester(Game(deck, hands, table, hints, fails, remainingTurns)).apply(func)
    }

    infix fun assert(func: Assertions.() -> Unit): Assertions {
      return Assertions(Game(deck, hands, table, hints, fails, remainingTurns), null).apply(func)
    }
  }

  class Tester(private var game: Hanabi?) {
    private var exception: Throwable? = null

    fun when_play_card(i: Int) {
      apply(ActionPlay(i))
    }

    fun when_discard_card(i: Int) {
      apply(ActionDiscard(i))
    }

    fun when_gives_a_color_hint(player: Int, color: Int) {
      apply(ActionHintColor(player, color))
    }

    fun when_gives_a_number_hint(player: Int, number: Int) {
      apply(ActionHintNumber(player, number))
    }

    fun when_apply(action: Hanabi.Action) {
      apply(action)
    }

    infix fun assert(func: Assertions.() -> Unit): Assertions {
      return Assertions(game, exception).apply(func)
    }

    private fun apply(action: Hanabi.Action) {
      try {
        game = game!!.apply(action)
      } catch (ex: Throwable) {
        game = null
        exception = ex
      }
    }
  }

  class Assertions(private val game: Hanabi?, private val exception: Throwable?) {

    fun then_is_ended() {
      assertThat(game!!.ended, `is`(true))
    }

    fun then_is_not_ended() {
      assertThat(game!!.ended, `is`(false))
    }

    fun then_throw_an_illegal_argument_exception() {
      return exception(IllegalArgumentException::class.java)
    }

    fun then_throw_an_illegal_state_exception() {
      return exception(IllegalStateException::class.java)
    }

    private fun exception(clazz: Class<out Throwable>) {
      assertThat(exception, `is`(instanceOf(clazz)))
    }
  }
}
