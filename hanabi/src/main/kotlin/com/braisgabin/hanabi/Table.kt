package com.braisgabin.hanabi

data class Table(private val table: List<Int>) : Hanabi.Table {
  override val size: Int by lazy(LazyThreadSafetyMode.NONE) { table.size }
  val points: Int by lazy(LazyThreadSafetyMode.NONE) { (0 until table.size).sumBy { table[it] } }

  override fun get(i: Int): Int {
    return table[i]
  }

  fun playCard(card: Card): Table {
    if (card.color > table.size) {
      throw IllegalArgumentException("The color of this card is equal or greater than the size of the table")
    }
    if (table[card.color] + 1 == card.number) {
      val table = this.table.toMutableList()
      table[card.color]++
      return Table(table)
    } else {
      throw Table.UnplayableCardException("You can't play $card. The current number is ${this.table[card.color]}")
    }
  }

  class UnplayableCardException(message: String) : Exception(message)
}
