package com.fdilke.tx.web.app

import cats.effect.Sync
import cats.implicits.*
import com.fdilke.tx.collatz.{CreateCollatzMachine, DestroyCollatzMachine}
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl

object CollatzRoutes:
  def createMachine[F[_] : Sync](
    config: CreateCollatzMachine[F]
  ): HttpRoutes[F] =
    val dsl = new Http4sDsl[F] {}
    import dsl._
    HttpRoutes.of[F]:
      case POST -> Root / "create" / id / startValue =>
        for
          _ <- config.create(id, startValue)
          resp <- Created:
            s"created Collatz machine with id=$id startValue=$startValue"
        yield
          resp

  def destroyMachine[F[_] : Sync](
    config: DestroyCollatzMachine[F]
  ): HttpRoutes[F] =
    val dsl = new Http4sDsl[F] {}
    import dsl._
    HttpRoutes.of[F]:
      case POST -> Root / "destroy" / id =>
        for
          _ <- config.destroy(id)
          resp <- Ok:
            s"destroyed Collatz machine with id=$id"
        yield
          resp
