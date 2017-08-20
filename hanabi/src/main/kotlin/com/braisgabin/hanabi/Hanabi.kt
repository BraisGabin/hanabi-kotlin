package com.braisgabin.hanabi

interface Hanabi {
  val ended: Boolean
  val deck: Deck
  val table: List<Int>
  val hands: List<Hand>
  val hints: Int
  val fails: Int

  fun apply(action: Hanabi.Action): Hanabi

  interface Action

  interface Deck {
    val size: Int

    fun drawCard(): DrawCard

    data class DrawCard(val card: Card, val deck: Hanabi.Deck)
  }
}

fun Hanabi.Deck.empty() = size <= 0

data class ActionPlay(val cardIndex: Int) : Hanabi.Action

data class ActionDiscard(val cardIndex: Int) : Hanabi.Action

data class ActionHintColor(val player: Int, val color: Int) : Hanabi.Action

data class ActionHintNumber(val player: Int, val number: Int) : Hanabi.Action
