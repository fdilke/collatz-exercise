package com.fdilke.tx.web.app

import cats.effect.Async
import cats.syntax.all.*
import com.comcast.ip4s.*
import com.fdilke.tx.collatz.{CreateCollatzMachine, DestroyCollatzMachine, MessagesForAllIds, MessagesForId}
import org.http4s.ember.client.EmberClientBuilder
import org.http4s.ember.server.EmberServerBuilder
import org.http4s.implicits.*
import org.http4s.server.middleware.Logger

object CollatzServer:

  def run[F[_]: Async]: F[Nothing] = {
    for {
      client <- EmberClientBuilder.default[F].build
      //      helloWorldAlg = HelloWorld.impl[F]
      //      jokeAlg = Jokes.impl[F](client)
      createMachine = CreateCollatzMachine.impl[F]
      destroyMachine = DestroyCollatzMachine.impl[F]
      messagesForId = MessagesForId.impl[F]
      messagesForAllIds = MessagesForAllIds.impl[F]

      httpApp = (
        CollatzRoutes.createMachine[F](createMachine) <+>
          CollatzRoutes.destroyMachine[F](destroyMachine) <+>
          CollatzRoutes.messagesForId[F](messagesForId) <+>
          CollatzRoutes.messagesForAllIds[F](messagesForAllIds)
        ).orNotFound

      // With Middlewares in place
      finalHttpApp = Logger.httpApp(true, true)(httpApp)

      _ <-
        EmberServerBuilder.default[F]
          .withHost(ipv4"0.0.0.0")
          .withPort(port"8080")
          .withHttpApp(finalHttpApp)
          .build
    } yield ()
  }.useForever
