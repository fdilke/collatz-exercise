package com.fdilke.tx.collatz

import cats.Applicative
import io.circe.{Encoder, Json}
import org.http4s.EntityEncoder
import org.http4s.circe.jsonEncoderOf
import cats.implicits._

trait CreateCollatzMachine[F[_]]:
  def create(
    id: String,
    startValue: String       
  ): F[Unit]

object CreateCollatzMachine:
  def impl[F[_]: Applicative](machines: CollatzMachines[F]): CreateCollatzMachine[F] =
    new CreateCollatzMachine[F]:
      override def create(
        id: String,
        startValue: String
      ): F[Unit] =
        println(s"creating a Collatz machine: id $id, startValue $startValue")
        machines.create(id, startValue)
        ().pure[F]
