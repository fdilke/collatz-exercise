package com.fdilke.tx.collatz

import cats.Applicative
import io.circe.{Encoder, Json}
import org.http4s.{EntityEncoder, ServerSentEvent}
import ServerSentEvent.EventId
import org.http4s.circe.jsonEncoderOf
import cats.implicits.*
import fs2.Stream

trait MessagesForId[F[_]]:
  def messages(
    id: String
  ): Stream[F, ServerSentEvent]

object MessagesForId:
  def impl[F[_]: Applicative]: MessagesForId[F] =
    (id: String) =>
      CollatzMachines.streamForMachine(id)

