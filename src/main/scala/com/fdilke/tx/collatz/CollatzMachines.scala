package com.fdilke.tx.collatz

import scala.collection.mutable

object CollatzMachines:
  private val all: mutable.Map[String, CollatzMachine] =
    new mutable.HashMap[String, CollatzMachine]()

  def create(id: String, startValue: String): Unit =
    if !startValue.forall(_.isDigit) then
      throw new IllegalArgumentException(s"illegal start value $startValue, should be all digits")
    else    
      all(id) = CollatzMachine(id, startValue.toInt)

  def destroy(id: String): Unit =
    if all.keySet.contains(id) then
      all.remove(id)
    else 
      throw new IllegalArgumentException(s"unknown Collatz machine id: $id")

