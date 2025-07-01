package com.fdilke.tx.collatz

import cats.Applicative
import io.circe.{Encoder, Json}
import org.http4s.{EntityEncoder, ServerSentEvent}
import ServerSentEvent.EventId
import org.http4s.circe.jsonEncoderOf
import cats.implicits.*
import fs2.Stream
import scala.concurrent.duration._

trait MessagesForId[F[_]]:
  def messages(
    id: String
  ): Stream[F, ServerSentEvent]

object MessagesForId:
  def impl[F[_]: Applicative]: MessagesForId[F] =
    (id: String) =>
      Stream(
        ServerSentEvent(Some("message 1"), Some("event-type-1"), Some(EventId("1")), Some(1.seconds)),
        ServerSentEvent(Some("message 2"), Some("event-type-2"), Some(EventId("2")), Some(2.seconds)),
        ServerSentEvent(Some("message 3"), Some("event-type-1"), Some(EventId("3")), Some(3.seconds))
      ).covary[F]

