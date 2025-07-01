package com.fdilke.tx.collatz

import cats.Applicative
import fs2.Stream
import org.http4s.ServerSentEvent
import ServerSentEvent.EventId
import cats.effect.{Async, Concurrent, IO}
import fs2.concurrent.Topic
import cats.implicits.*

import java.util.concurrent.atomic.AtomicInteger
import scala.concurrent.duration.*

class CollatzMachine[F[_]: Async](
  id: String,
  startValue: Int
):
  private val seqPosition: AtomicInteger =
    AtomicInteger(0)

  private val collatzStream: Stream[F, Int] =
    Stream.iterate(startValue): n =>
      if n == 1 then
        startValue
      else if n % 2 == 1 then
        1 + 3 * n
      else
        n / 2
    .covary[F]

//  private val topicF: F[Topic[F, Int]] =
//    Topic[F, Int]
//
//  val x: Stream[F, Topic[F, Int]] =
//    Stream.eval(topicF)
  

  def ping(): F[Unit] =
    for
      position <- { seqPosition.getAndIncrement }.pure[F]
      latestOption <- collatzStream.drop(position).take(1).compile.last
      latest = latestOption.getOrElse { throw new IllegalStateException() }
      _ <- IO.println(s"$id) iterating -> position=$position value=$latest").asInstanceOf[F[Unit]]
    yield
      ()

  def increment(amount: Int): Unit =
    ()

  def streamEvents(): F[Stream[F, ServerSentEvent]] =
    (for
      n <- collatzStream.drop(seqPosition.get)
    yield
      ServerSentEvent(Some(s"iterate=$n"), Some("collatz-event-type"), Some(EventId("1")), Some(1.seconds))
    ).pure[F]

