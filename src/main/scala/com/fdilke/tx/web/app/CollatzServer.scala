package com.fdilke.tx.web.app

import cats.effect.{Async, IO}
import cats.effect.kernel.{Fiber, Resource}
import cats.syntax.all.*
import com.comcast.ip4s.*
import com.fdilke.tx.collatz.*
import org.http4s.ember.client.EmberClientBuilder
import org.http4s.ember.server.EmberServerBuilder
import org.http4s.implicits.*
import org.http4s.server.middleware.Logger
import fs2.Stream
import cats.effect.implicits._

import java.time.Instant
import scala.concurrent.duration.*

object CollatzServer:

  def run[F[_]: Async]: F[Unit] =
    val machines = new CollatzMachines[F]()
    val serverResource: Resource[F, Unit] =
      for
        client <- EmberClientBuilder.default[F].build
        createMachine = CreateCollatzMachine.impl[F](machines)
        destroyMachine = DestroyCollatzMachine.impl[F](machines)
        messagesForId = MessagesForId.impl[F](machines)
        messagesForAllIds = MessagesForAllIds.impl[F](machines)
        incrementMachine = IncrementMachine.impl[F](machines)
        httpApp = (
          CollatzRoutes.createMachine[F](createMachine) <+>
            CollatzRoutes.destroyMachine[F](destroyMachine) <+>
            CollatzRoutes.messagesForId[F](messagesForId) <+>
            CollatzRoutes.messagesForAllIds[F](messagesForAllIds) <+>
            CollatzRoutes.incrementMachine[F](incrementMachine)
          ).orNotFound
        finalHttpApp = Logger.httpApp(true, true)(httpApp)

        _ <-
          EmberServerBuilder.default[F]
            .withHost(ipv4"0.0.0.0")
            .withPort(port"8080")
            .withHttpApp(finalHttpApp)
            .build
      yield
        ()
    val timerResource: Resource[F, Unit] =
        Resource.make(
          Async[F].start(
            Stream
              .awakeEvery[F](1.seconds)
              .evalMap(_ => machines.pingTheMachines())
              .compile
              .drain
          )
        )(fiber => fiber.cancel).void
    Resource.both(timerResource, serverResource).useForever.map:
      _ => ()
