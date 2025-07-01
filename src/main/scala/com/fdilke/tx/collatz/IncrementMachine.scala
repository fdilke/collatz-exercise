package com.fdilke.tx.collatz

import cats.Applicative
import io.circe.{Encoder, Json}
import org.http4s.EntityEncoder
import org.http4s.circe.jsonEncoderOf
import cats.implicits._

trait IncrementMachine[F[_]]:
  def messages(
    id: String,
    amount: String
  ): F[Unit]

object IncrementMachine:
  def impl[F[_]: Applicative](machines: CollatzMachines[F]): IncrementMachine[F] =
    (id: String, amount: String) =>
      machines.incrementMachine(id, amount)
      ().pure[F]
