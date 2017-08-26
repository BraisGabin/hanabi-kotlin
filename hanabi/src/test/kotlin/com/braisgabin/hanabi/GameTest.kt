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

  @Test
  fun discard_with_less_than_8_hints_gives_a_hint() {
    build {
      with_hints(4)
    } test {
      when_discard_card(0)
    } assert {
      then_there_are_hints(5)
    }
  }

  @Test
  fun discard_with_8_hints_gives_no_hint() {
    build {
      with_hints(8)
    } test {
      when_discard_card(0)
    } assert {
      then_there_are_hints(8)
    }
  }

  @Test
  fun discard_end_your_turn() {
    test {
      when_discard_card(0)
    } assert {
      then_play_the_next_player()
    }
  }

  @Test
  fun discard_removes_your_card() {
    test {
      when_discard_card(0)
    } assert {
      then_you_dont_have_that_card()
    }
  }

  @Test
  fun discard_gives_you_the_next_from_the_deck() {
    test {
      when_discard_card(0)
    } assert {
      then_you_have_the_next_deck_card()
    }
  }

  @Test
  fun discard_when_there_are_not_more_cards_in_deck_lefts_you_with_a_smaller_hand() {
    build {
      with_empty_deck()
    } test {
      when_discard_card(0)
    } assert {
      then_you_have_a_hand_smaller_by_one()
    }
  }

  @Test
  fun play_with_an_incorrect_card_increase_the_fails_by_1() {
    build {
      with_table(listOf(3, 0, 0))
      with_hints(4)
      with_fails(0)
      with_playable_hand(listOf(Card(0, 3), Card(1, 5), Card(2, 5), Card(3, 5)))
    } test {
      when_play_card(0)
    } assert {
      then_there_are_fails(1)
      then_there_are_hints(4)
      then_there_are_table(listOf(3, 0, 0))
    }
  }

  @Test
  fun play_with_a_correct_card_increase_the_table_value_at_that_color_and_not_changes_hints() {
    build {
      with_table(listOf(3, 0, 0))
      with_hints(4)
      with_playable_hand(listOf(Card(0, 4), Card(1, 5), Card(2, 5), Card(3, 5)))
    } test {
      when_play_card(0)
    } assert {
      then_there_are_fails(0)
      then_there_are_hints(4)
      then_there_are_table(listOf(4, 0, 0))
    }
  }

  @Test
  fun play_with_a_correct_5card_increases_hints_by_1_if_less_than_8() {
    build {
      with_table(listOf(4, 0, 0))
      with_hints(4)
      with_playable_hand(listOf(Card(0, 5), Card(1, 5), Card(2, 5), Card(3, 5)))
    } test {
      when_play_card(0)
    } assert {
      then_there_are_hints(5)
      then_there_are_table(listOf(5, 0, 0))
    }
  }

  @Test
  fun play_with_a_correct_5card_not_increases_hints_by_1_if_there_are_8() {
    build {
      with_table(listOf(4, 0, 0))
      with_hints(8)
      with_playable_hand(listOf(Card(0, 5), Card(1, 5), Card(2, 5), Card(3, 5)))
    } test {
      when_play_card(0)
    } assert {
      then_there_are_hints(8)
      then_there_are_table(listOf(5, 0, 0))
    }
  }

  @Test
  fun play_end_your_turn() {
    test {
      when_play_card(0)
    } assert {
      then_play_the_next_player()
    }
  }

  @Test
  fun play_removes_your_card() {
    test {
      when_play_card(0)
    } assert {
      then_you_dont_have_that_card()
    }
  }

  @Test
  fun play_gives_you_the_next_from_the_deck() {
    test {
      when_play_card(0)
    } assert {
      then_you_have_the_next_deck_card()
    }
  }

  @Test
  fun play_when_there_are_not_more_cards_in_deck_lefts_you_with_a_smaller_hand() {
    build {
      with_empty_deck()
    } test {
      when_play_card(0)
    } assert {
      then_you_have_a_hand_smaller_by_one()
    }
  }

  @Test
  fun color_hint_to_yourself() {
    build {
    } test {
      when_give_a_color_hint(0, 1)
    } assert {
      then_throw_an_illegal_argument_exception()
    }
  }

  @Test
  fun color_hint_to_unknown_player() {
    build {
    } test {
      when_give_a_color_hint(6, 1)
    } assert {
      then_throw_an_illegal_argument_exception()
    }
  }

  @Test
  fun color_hint_to_player_that_doesnt_have() {
    build {
    } test {
      when_give_a_color_hint(1, 2)
    } assert {
      then_throw_an_illegal_argument_exception()
    }
  }

  @Test
  fun color_hint_without_hints() {
    build {
      with_hints(0)
    } test {
      when_give_a_color_hint(1, 1)
    } assert {
      then_throw_an_illegal_state_exception()
    }
  }

  @Test
  fun color_hint_reduce_hints() {
    build {
      with_hints(4)
    } test {
      when_give_a_color_hint(1, 1)
    } assert {
      then_there_are_hints(3)
    }
  }

  @Test
  fun number_hint_to_yourself() {
    build {
    } test {
      when_give_a_number_hint(0, 1)
    } assert {
      then_throw_an_illegal_argument_exception()
    }
  }

  @Test
  fun number_hint_to_unknown_player() {
    build {
    } test {
      when_give_a_number_hint(6, 1)
    } assert {
      then_throw_an_illegal_argument_exception()
    }
  }

  @Test
  fun number_hint_to_player_that_doesnt_have() {
    build {
    } test {
      when_give_a_number_hint(1, 8)
    } assert {
      then_throw_an_illegal_argument_exception()
    }
  }

  @Test
  fun number_hint_without_hints() {
    build {
      with_hints(0)
    } test {
      when_give_a_number_hint(1, 1)
    } assert {
      then_throw_an_illegal_state_exception()
    }
  }

  @Test
  fun number_hint_reduce_hints() {
    build {
      with_hints(4)
    } test {
      when_give_a_number_hint(1, 1)
    } assert {
      then_there_are_hints(3)
    }
  }

  private fun build(func: GameBuilder.() -> Unit) = GameBuilder().apply(func)

  private fun test(func: Tester.() -> Unit) = GameBuilder().test(func)

  class GameBuilder {
    private var deck: Deck? = null
    private var hands: List<Hand>? = null
    private var players: Int = 4
    private var handSize: Int = 4
    private var playableHand: Hand? = null
    private var table: Table = Table(listOf(0, 0, 0, 0, 0))
    private var hints: Int = 8
    private var fails: Int = 0
    private var remainingTurns: Int? = null

    fun with_fails(fails: Int) {
      this.fails = fails
    }

    fun with_playable_hand(cards: List<Card>) {
      playableHand = Hand(cards)
    }

    fun with_table(table: List<Int>) {
      this.table = Table(table)
    }

    fun with_remaining_turn(remainingTurns: Int?) {
      this.remainingTurns = remainingTurns
    }

    fun with_hints(hints: Int): GameBuilder {
      this.hints = hints
      return this
    }

    fun with_empty_deck(): GameBuilder {
      this.deck = Deck(emptyList())
      return this
    }

    infix fun test(func: Tester.() -> Unit): Tester {
      return Tester(buildGame()).apply(func)
    }

    infix fun assert(func: Assertions.() -> Unit): Assertions {
      return Assertions(listOf(buildGame()), null, null).apply(func)
    }

    private fun buildGame(): Game {
      var deck = deck
      if (deck == null) {
        deck = Deck(List(20, { Card(0, 20 - it) }))
      }
      var hands = hands
      if (hands == null) {
        hands = mutableListOf()
        val playableHand = playableHand
        if (playableHand != null) {
          hands.add(playableHand)
        }
        for (player in hands.size until players) {
          val handCards = mutableListOf<Card>()
          (0 until handSize).mapTo(handCards) { Card(1, it + 1) }
          hands.add(Hand(handCards))
        }
      }

      return Game(deck, hands, table, hints, fails, remainingTurns)
    }
  }

  class Tester(game: Hanabi) {
    private var currentTurn: Hanabi? = game
    private var exception: RuntimeException? = null
    private val turns: MutableList<Hanabi> = mutableListOf(game)
    private var previousPlayedCard: Card? = null

    private fun getGame(): Hanabi {
      val exception = exception
      if (exception != null) {
        throw exception
      }
      return currentTurn!!
    }

    fun when_play_card(i: Int) {
      previousPlayedCard = getGame().hands[0][i]
      apply(ActionPlay(i))
    }

    fun when_discard_card(i: Int) {
      previousPlayedCard = getGame().hands[0][i]
      apply(ActionDiscard(i))
    }

    fun when_give_a_color_hint(player: Int, color: Int) {
      previousPlayedCard = null
      apply(ActionHintColor(player, color))
    }

    fun when_give_a_number_hint(player: Int, number: Int) {
      previousPlayedCard = null
      apply(ActionHintNumber(player, number))
    }

    fun when_apply(action: Hanabi.Action) {
      apply(action)
    }

    private fun apply(action: Hanabi.Action) {
      try {
        val game = getGame().apply(action)
        turns.add(game)
        this.currentTurn = game
      } catch (ex: RuntimeException) {
        currentTurn = null
        exception = ex
      }
    }

    infix fun assert(func: Assertions.() -> Unit): Assertions {
      return Assertions(turns, exception, previousPlayedCard).apply(func)
    }
  }

  class Assertions(private val turns: List<Hanabi>,
                   private val exception: Throwable?,
                   private val previousPlayedCard: Card?) {

    private val currentTurn: Hanabi by lazy {
      if (exception != null) {
        throw exception
      } else {
        turns.last()
      }
    }

    fun then_is_ended() {
      assertThat(currentTurn.ended, `is`(true))
    }

    fun then_is_not_ended() {
      assertThat(currentTurn.ended, `is`(false))
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

    fun then_there_are_hints(hints: Int) {
      assertThat(currentTurn.hints, `is`(hints))
    }

    fun then_there_are_fails(fails: Int) {
      assertThat(currentTurn.fails, `is`(fails))
    }

    fun then_play_the_next_player() {
      val lastTurn = previousTurn()
      assertThat(lastTurn.hands[1], `is`(currentTurn.hands[0]))
    }

    fun then_you_dont_have_that_card() {
      assertThat(currentTurn.hands.last().contains(previousPlayedCard!!), `is`(false))
    }

    fun then_you_have_the_next_deck_card() {
      val lastTurnDeck = previousTurn().deck
      val card = lastTurnDeck.last()
      assertThat(currentTurn.hands.last().contains(card), `is`(true))
    }

    fun then_you_have_a_hand_smaller_by_one() {
      val previousHandSize = previousTurn().hands[0].size
      val currentHandSize = currentTurn.hands.last().size
      assertThat(previousHandSize, `is`(currentHandSize + 1))
    }

    fun then_there_are_table(table: List<Int>) {
      assertThat(currentTurn.table as Table, `is`(Table(table)))
    }

    private fun previousTurn() = turns[turns.size - 2]

    private fun Hand.contains(card: Card) = (0 until size).any { this[it] == card }

    private fun Hanabi.Deck.last() = this[size - 1]
  }
}
