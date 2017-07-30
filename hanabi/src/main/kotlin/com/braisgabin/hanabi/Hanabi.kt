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
