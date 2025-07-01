package com.fdilke.tx.collatz

import cats.Applicative
import io.circe.{Encoder, Json}
import org.http4s.EntityEncoder
import org.http4s.circe.jsonEncoderOf
import cats.implicits._

trait DestroyCollatzMachine[F[_]]:
  def destroy(
    id: String,
  ): F[Unit]

object DestroyCollatzMachine:
  def impl[F[_]: Applicative](machines: CollatzMachines[F]): DestroyCollatzMachine[F] =
    new DestroyCollatzMachine[F]:
      override def destroy(
        id: String
      ): F[Unit] =
        println(s"destroying Collatz machine: id $id")
        machines.destroy(id)
        ().pure[F]
