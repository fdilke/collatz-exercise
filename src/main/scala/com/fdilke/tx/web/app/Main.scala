package com.fdilke.tx.web.app

import cats.effect.{IO, IOApp}

object Main extends IOApp.Simple:
  val run = CollatzServer.run[IO]