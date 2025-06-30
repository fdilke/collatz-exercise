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
//  final case class Id(id: String) extends AnyVal
//  final case class StartValue(startValue: String) extends AnyVal

  def impl[F[_]: Applicative]: CreateCollatzMachine[F] =
    new CreateCollatzMachine[F]:
      override def create(
        id: String,
        startValue: String
      ): F[Unit] =
        println(s"invoking a Collatz machine: id $id, startValue $startValue")
        ().pure[F]
