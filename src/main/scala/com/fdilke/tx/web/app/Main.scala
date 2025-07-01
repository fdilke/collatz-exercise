package com.fdilke.tx.web.app

import cats.effect.{Async, ExitCode, IO, IOApp}

import scala.concurrent.duration.*
import cats.implicits.*

import java.time.Instant

object Main extends IOApp.Simple:

//  def schedule[F[_] : Async](task: () => F[Unit]): F[Unit] =
//    Async[F].sleep(1.second) *> {
//      Async[F].delay(println(">> " + Instant.now.toString))
//    } *>
//      Async[F].defer(task())
//
//  def repeat[F[_] : Async](task: () => F[Unit]): F[Unit] =
//    schedule[F](task) >> repeat(task)
//
//  val run =
//    repeat[IO](() => IO.println("bird")).as(ExitCode.Success)

  val run = CollatzServer.run[IO]