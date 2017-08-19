package com.braisgabin.hanabi

class Game(override val deck: List<Card>,
           override val hands: List<Hand>,
           override val table: List<Int>,
           override val hints: Int,
           override val fails: Int,
           private val remainingTurns: Int?) : Hanabi {
  override val ended: Boolean
    get() = fails >= 3 || table.sum() >= 25 || if (remainingTurns == null) false else remainingTurns <= 0

  override fun apply(action: Hanabi.Action): Hanabi {
    return when (action) {
      is ActionPlay -> TODO("not implemented")
      is ActionDiscard -> TODO("not implemented")
      is ActionHintColor -> TODO("not implemented")
      is ActionHintNumber -> TODO("not implemented")
      else -> throw IllegalArgumentException("Unknown action $action")
    }
  }
}
