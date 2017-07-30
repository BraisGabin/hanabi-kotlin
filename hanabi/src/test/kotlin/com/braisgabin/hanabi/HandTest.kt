package com.braisgabin.hanabi

import org.hamcrest.CoreMatchers.*
import org.junit.Assert.assertThat
import org.junit.Test

class HandTest {
  @Test
  fun replaceCard_1() {
    val hand = Hand(listOf(
        Card(0,1),
        Card(0,2),
        Card(0,3),
        Card(0,4)))

    val newHand = hand.replaceCard(2, Card(4,4))

    assertThat(newHand, `is`(Hand(listOf(
        Card(0,1),
        Card(0,2),
        Card(0,4),
        Card(4,4)))))
  }

  @Test
  fun replaceCard_2() {
    val hand = Hand(listOf(
        Card(0,1),
        Card(0,2),
        Card(0,3),
        Card(0,4)))

    val newHand = hand.replaceCard(0, Card(4,4))

    assertThat(newHand, `is`(Hand(listOf(
        Card(0,2),
        Card(0,3),
        Card(0,4),
        Card(4,4)))))
  }

  @Test
  fun hasColor() {
    val hand = Hand(listOf(
        Card(0,1),
        Card(0,2),
        Card(3,3),
        Card(0,4)))

    assertThat(hand.hasColor(0), `is`(true))
    assertThat(hand.hasColor(1), `is`(false))
    assertThat(hand.hasColor(2), `is`(false))
    assertThat(hand.hasColor(3), `is`(true))
    assertThat(hand.hasColor(4), `is`(false))
  }

  @Test
  fun hasNumber() {
    val hand = Hand(listOf(
        Card(0,1),
        Card(0,2),
        Card(3,4),
        Card(0,4)))

    assertThat(hand.hasNumber(1), `is`(true))
    assertThat(hand.hasNumber(2), `is`(true))
    assertThat(hand.hasNumber(3), `is`(false))
    assertThat(hand.hasNumber(4), `is`(true))
    assertThat(hand.hasNumber(5), `is`(false))
  }

  @Test
  fun get() {
    val hand = Hand(listOf(
        Card(0,1),
        Card(0,2),
        Card(3,4),
        Card(0,4)))

    assertThat(hand[0], `is`(Card(0, 1)))
    assertThat(hand[1], `is`(Card(0, 2)))
    assertThat(hand[2], `is`(Card(3, 4)))
    assertThat(hand[3], `is`(Card(0, 4)))
  }

  @Test
  fun size() {
    val hand1 = Hand(listOf(
        Card(0,1),
        Card(0,2),
        Card(3,4),
        Card(0,4)))
    assertThat(hand1.size, `is`(4))

    val hand2 = Hand(listOf(
        Card(0,1),
        Card(0,4)))
    assertThat(hand2.size, `is`(2))
  }
}
