package com.fdilke.tx.collatz

import cats.Applicative
import io.circe.{Encoder, Json}
import org.http4s.EntityEncoder
import org.http4s.circe.jsonEncoderOf
import cats.implicits._

trait MessagesForId[F[_]]:
  def messages(
    id: String
  ): F[Unit]

object MessagesForId:
  def impl[F[_]: Applicative]: MessagesForId[F] =
    (id: String) =>
      CollatzMachines.messages(id)
      ().pure[F]
