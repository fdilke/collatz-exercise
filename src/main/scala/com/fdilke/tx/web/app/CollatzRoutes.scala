package com.fdilke.tx.web.app

import cats.effect.Sync
import cats.implicits.*
import com.fdilke.tx.collatz.CreateCollatzMachine
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl

object CollatzRoutes:
  def collatzRoutes[F[_] : Sync](
    J: CreateCollatzMachine[F]
  ): HttpRoutes[F] =
    val dsl = new Http4sDsl[F] {}
    import dsl._
    HttpRoutes.of[F] {
      case GET -> Root / "create" / id / startValue =>
        for {
          _ <- J.create(id, startValue)
          resp <- Ok("xxx")
        } yield resp
    }