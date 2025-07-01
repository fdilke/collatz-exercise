package com.fdilke.tx.collatz

import cats.Applicative
import fs2.Stream
import org.http4s.ServerSentEvent
import ServerSentEvent.EventId
import cats.effect.{Async, Concurrent, IO}
import fs2.concurrent.Topic
import cats.implicits.*

import scala.concurrent.duration.*

class CollatzMachine[F[_]: Async](
  id: String,
  startValue: Int
):
  private var currentValue: Int =
    startValue

  private val topicF: F[Topic[F, Int]] =
    Topic[F, Int]

  val x: Stream[F, Topic[F, Int]] = 
    Stream.eval(topicF)
  
  private def collatz(n: Int) =
    if n == 1 then
      startValue
    else if n % 2 == 1 then
      1 + 3 * n
    else
      n / 2

  def ping(): F[Unit] =
    for
      _ <- { currentValue = collatz(currentValue) }.pure[F]
      topic <- topicF
      _ <- IO.println(s"$id) iterating -> $currentValue").asInstanceOf[F[Unit]]
      _ <- topic.publish1(currentValue)
    yield
      ()
     
      
//      topic.publish1()
  // IO.println(s"pinged machine: $id").asInstanceOf[F[Unit]]
    
  //      {
//  }.pure[F]

  def increment(amount: Int): Unit =
    ()

  def streamEvents(): F[Stream[F, ServerSentEvent]] =
    for
      topic <- topicF
      stream = topic.subscribe(100)
    yield
      for
        n <- stream
      yield
        ServerSentEvent(Some(s"iterate=$n"), Some("collatz-event-type"), Some(EventId("1")), Some(1.seconds))
        
//    Stream(
//      ServerSentEvent(Some("message 1"), Some("event-type-1"), Some(EventId("1")), Some(1.seconds)),
//      ServerSentEvent(Some("message 2"), Some("event-type-2"), Some(EventId("2")), Some(2.seconds)),
//      ServerSentEvent(Some("message 3"), Some("event-type-1"), Some(EventId("3")), Some(3.seconds))
//    ).covary[F]
    