package com.fdilke.tx.collatz

import cats.Applicative
import io.circe.{Encoder, Json}
import org.http4s.EntityEncoder
import org.http4s.circe.jsonEncoderOf
import cats.implicits._

trait MessagesForAllIds[F[_]]:
  def messages(): F[Unit]

object MessagesForAllIds:
  def impl[F[_]: Applicative]: MessagesForAllIds[F] =
    () =>
      CollatzMachines.messagesForAll()
      ().pure[F]
