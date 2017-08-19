package com.braisgabin.hanabi

interface Hanabi {
  val ended: Boolean
  val deck: List<Card>
  val table: List<Int>
  val hands: List<Hand>
  val hints: Int
  val fails: Int

  fun apply(action: Hanabi.Action): Hanabi

  interface Action
}

data class ActionPlay(val cardIndex: Int) : Hanabi.Action

data class ActionDiscard(val cardIndex: Int) : Hanabi.Action

data class ActionHintColor(val player: Int, val color: Int) : Hanabi.Action

data class ActionHintNumber(val player: Int, val number: Int) : Hanabi.Action
