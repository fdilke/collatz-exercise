package com.fdilke.tx.collatz

import cats.Applicative
import fs2.Stream
import org.http4s.ServerSentEvent
import ServerSentEvent.EventId
import scala.concurrent.duration._

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

  def streamEvents[F[_] : Applicative](): Stream[F, ServerSentEvent] =
    Stream(
      ServerSentEvent(Some("message 1"), Some("event-type-1"), Some(EventId("1")), Some(1.seconds)),
      ServerSentEvent(Some("message 2"), Some("event-type-2"), Some(EventId("2")), Some(2.seconds)),
      ServerSentEvent(Some("message 3"), Some("event-type-1"), Some(EventId("3")), Some(3.seconds))
    ).covary[F]
    