package com.fdilke.tx.collatz

import java.util.{Timer, TimerTask}
import scala.collection.mutable

object CollatzMachines:
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
