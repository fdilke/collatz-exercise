package com.fdilke.tx.collatz

import cats.Applicative
import cats.effect.Async
import fs2.Stream
import org.http4s.ServerSentEvent

import java.util.{Timer, TimerTask}
import scala.collection.mutable

class CollatzMachines[F[_]: Async]:
  private val allMachines: mutable.Map[String, CollatzMachine] =
    new mutable.HashMap[String, CollatzMachine]()

  (new Timer).schedule(
    () => pingTheMachines(),
    1000,
    1000)

  private def pingTheMachines(): Unit =
    for
      (_, machine) <- allMachines
    do
      machine.ping()

  def create(id: String, startValue: String): Unit =
    if !startValue.forall(_.isDigit) then
      throw new IllegalArgumentException(s"illegal start value $startValue, should be all digits")
    else
      allMachines(id) = CollatzMachine(id, startValue.toInt)

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
  ): Stream[F, ServerSentEvent] =
    if allMachines.keySet.contains(id) then
      allMachines(id).streamEvents()
    else
      throw new IllegalArgumentException(s"unknown Collatz machine id: $id")

