package com.fdilke.tx.web.app

import cats.effect.Async
import cats.syntax.all._
import com.comcast.ip4s._
import com.fdilke.tx.collatz.CreateCollatzMachine
import org.http4s.ember.client.EmberClientBuilder
import org.http4s.ember.server.EmberServerBuilder
import org.http4s.implicits._
import org.http4s.server.middleware.Logger

object CollatzServer:

  def run[F[_]: Async]: F[Nothing] = {
    for {
      client <- EmberClientBuilder.default[F].build
//      helloWorldAlg = HelloWorld.impl[F]
//      jokeAlg = Jokes.impl[F](client)
      collatzAlg = CreateCollatzMachine.impl[F]

      httpApp = (
        CollatzRoutes.collatzRoutes[F](collatzAlg)
//        HttpshRoutes.helloWorldRoutes[F](helloWorldAlg) <+>
//          HttpshRoutes.jokeRoutes[F](jokeAlg)
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
