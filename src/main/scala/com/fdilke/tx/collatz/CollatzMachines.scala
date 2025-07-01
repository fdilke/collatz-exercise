package com.fdilke.tx.collatz

import cats.Applicative
import cats.effect.{Async, IO}
import cats.syntax.traverse
import fs2.Stream
import org.http4s.ServerSentEvent

import java.util.{Timer, TimerTask}
import scala.collection.mutable
import cats.implicits.*

class CollatzMachines[F[_]: Async]:
  private val allMachines: mutable.Map[String, CollatzMachine[F]] =
    new mutable.HashMap[String, CollatzMachine[F]]()

  def pingTheMachines(): F[Unit] =
//    IO.println("pinging the machines").asInstanceOf[F[Unit]]
    allMachines.values.toList.traverse: machine =>
      machine.ping()
    .map:
      _ => ()

  def create(id: String, startValue: String): Unit =
    if !startValue.forall(_.isDigit) then
      throw new IllegalArgumentException(s"illegal start value $startValue, should be all digits")
    else
      allMachines(id) =
        CollatzMachine[F](id, startValue.toInt)

  def destroy(id: String): Unit =
    if allMachines.keySet.contains(id) then
      allMachines.remove(id)
    else
      throw new IllegalArgumentException(s"unknown Collatz machine id: $id")

  def messages(id: String): Unit =
    ()

  def messagesForAll(): Unit =
    ()

  def incrementMachine(id: String, amount: String): Unit =
    if allMachines.keySet.contains(id) then
      if !amount.forall(_.isDigit) then
        throw new IllegalArgumentException(s"illegal increment amount value $amount, should be all digits")
      else
        allMachines(id).increment(amount.toInt)
    else
      throw new IllegalArgumentException(s"unknown Collatz machine id: $id")

  def streamForMachine(
    id: String
  ): F[Stream[F, ServerSentEvent]] =
    if allMachines.keySet.contains(id) then
      allMachines(id).streamEvents()
    else
      throw new IllegalArgumentException(s"unknown Collatz machine id: $id")

