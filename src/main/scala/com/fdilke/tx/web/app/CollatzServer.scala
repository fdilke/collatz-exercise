package com.fdilke.tx.web.app

import cats.effect.{Async, IO}
import cats.effect.kernel.Resource
import cats.syntax.all.*
import com.comcast.ip4s.*
import com.fdilke.tx.collatz.*
import org.http4s.ember.client.EmberClientBuilder
import org.http4s.ember.server.EmberServerBuilder
import org.http4s.implicits.*
import org.http4s.server.middleware.Logger

import java.time.Instant
import scala.concurrent.duration.*

object CollatzServer:

  def run[F[_]: Async]: F[Unit] =
    val machines = new CollatzMachines[F]()
    val resource: Resource[F, Unit] =
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

//        _ <-
//          Resource.eval(IO.println("bird 1 off").asInstanceOf[F[Unit]])
//           Resource.eval(repeat[F](() => machines.pingTheMachines()))
        _ <-
          EmberServerBuilder.default[F]
            .withHost(ipv4"0.0.0.0")
            .withPort(port"8080")
            .withHttpApp(finalHttpApp)
            .build
      yield
        ()
    for {
/*      _ <- Resource.make(
        Async[F].start(
          machines.pingTheMachines()
//          repeat[F](() => machines.pingTheMachines())
        )
      )(fiber => fiber.cancel).use(_ => Async[F].never) */
      _ <- resource.useForever
    } yield ()

  private def repeat[F[_] : Async](task: () => F[Unit]): F[Unit] =
    Async[F].sleep(1.second) *> {
      Async[F].delay(println(">> " + Instant.now.toString))
    } *>
      Async[F].defer(task())
    *> Async[F].defer(repeat(task))

//  private def schedule[F[_] : Async](task: () => F[Unit]): F[Unit] =
//    Async[F].sleep(1.second) *> {
//      Async[F].delay(println(">> " + Instant.now.toString))
//    } *>
//      Async[F].defer(task())
//
//  private def repeat[F[_] : Async](task: () => F[Unit]): F[Unit] =
//    schedule[F](task) >> repeat(task)
