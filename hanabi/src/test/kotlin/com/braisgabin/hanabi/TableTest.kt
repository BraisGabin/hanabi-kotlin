package com.braisgabin.hanabi

import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException

class TableTest {

  @Rule
  @JvmField
  val thrown: ExpectedException = ExpectedException.none()

  @Test
  fun getSize() {
    assertThat(Table(listOf(1, 2, 3)).size, `is`(3))
    assertThat(Table(listOf(1, 2, 3, 4)).size, `is`(4))
  }

  @Test
  fun getPoints() {
    assertThat(Table(listOf(1, 2, 3)).points, `is`(6))
    assertThat(Table(listOf(1, 3, 3)).points, `is`(7))
  }

  @Test
  fun get() {
    assertThat(Table(listOf(1, 2, 3))[0], `is`(1))
    assertThat(Table(listOf(1, 2, 3))[1], `is`(2))
  }

  @Test
  fun playCard_colorOutOfRange() {
    thrown.expect(IllegalArgumentException::class.java)
    Table(listOf(1, 2, 3)).playCard(Card(5, 1))
  }

  @Test
  fun playCard_numberTooHigh() {
    thrown.expect(Table.UnplayableCardException::class.java)
    Table(listOf(1, 2, 3)).playCard(Card(1, 4))
  }

  @Test
  fun playCard_numberTooLow() {
    thrown.expect(Table.UnplayableCardException::class.java)
    Table(listOf(1, 2, 3)).playCard(Card(1, 1))
  }

  @Test
  fun playCard() {
    assertThat(Table(listOf(1, 2, 3)).playCard(Card(1, 3)), `is`(Table(listOf(1, 3, 3))))
  }
}
