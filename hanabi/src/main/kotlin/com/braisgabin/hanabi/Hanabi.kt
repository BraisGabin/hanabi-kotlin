package com.braisgabin.hanabi

interface Hanabi {
  val ended: Boolean
  val deck: Deck
  val table: Table
  val hands: List<Hand>
  val hints: Int
  val fails: Int

  fun apply(action: Hanabi.Action): Hanabi

  interface Action

  interface Deck {
    val size: Int

    operator fun get(i: Int): Card
  }

  interface Table {
    val size: Int

    operator fun get(i: Int): Int
  }
}

data class ActionPlay(val cardIndex: Int) : Hanabi.Action

data class ActionDiscard(val cardIndex: Int) : Hanabi.Action

data class ActionHintColor(val player: Int, val color: Int) : Hanabi.Action

data class ActionHintNumber(val player: Int, val number: Int) : Hanabi.Action
