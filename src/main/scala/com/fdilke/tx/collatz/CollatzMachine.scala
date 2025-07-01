package com.fdilke.tx.collatz

class CollatzMachine(
  id: String,
  startValue: Int
):
  private var currentValue: Int =
    startValue

  private def collatz(n: Int) =
    if n == 1 then
      startValue
    else if n % 2 == 1 then
      1 + 3 * n
    else
      n / 2

  def ping(): Unit =
    println(s"$id) iterating -> $currentValue")
    currentValue = collatz(currentValue)

  def increment(amount: Int): Unit =
    ()