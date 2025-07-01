package com.fdilke.tx.web.app

import cats.effect.Sync
import cats.implicits._
import com.fdilke.tx.collatz.{CreateCollatzMachine, DestroyCollatzMachine, IncrementMachine, MessagesForAllIds, MessagesForId}
import org.http4s.{HttpRoutes, MediaType, Response, ServerSentEvent}
import org.http4s.dsl.Http4sDsl
import fs2.Stream
import org.http4s.ServerSentEvent.EventId
import org.http4s.headers.{`Content-Type`, `Last-Modified`}

object CollatzRoutes:
  def createMachine[F[_] : Sync](
    config: CreateCollatzMachine[F]
  ): HttpRoutes[F] =
    val dsl = new Http4sDsl[F] {}
    import dsl._
    HttpRoutes.of[F]:
      case POST -> Root / "create" / id / startValue =>
        for
          _ <- config.create(id, startValue)
          resp <- Created:
            s"created Collatz machine with id=$id startValue=$startValue"
        yield
          resp

  def destroyMachine[F[_] : Sync](
    config: DestroyCollatzMachine[F]
  ): HttpRoutes[F] =
    val dsl = new Http4sDsl[F] {}
    import dsl._
    HttpRoutes.of[F]:
      case POST -> Root / "destroy" / id =>
        for
          _ <- config.destroy(id)
          resp <- Ok:
            s"destroyed Collatz machine with id=$id"
        yield
          resp

  def messagesForId[F[_] : Sync](
    config: MessagesForId[F]
  ): HttpRoutes[F] =
    val dsl = new Http4sDsl[F] {}
    import dsl._
    HttpRoutes.of[F]:
      case GET -> Root / "messages" / id =>
        val events: Stream[F, ServerSentEvent] = 
          config.messages(id)

        val stream: Stream[F, ServerSentEvent] =
          Stream.emit(events).flatMap(identity)

        val response = Response[F](Ok)
          .withContentType(`Content-Type`(
            new MediaType("text", "event-stream", compressible=true, binary=false)
          ))
          .withBodyStream(stream.through(ServerSentEvent.encoder))

        response.pure[F]

  def messagesForAllIds[F[_] : Sync](
    config: MessagesForAllIds[F]
  ): HttpRoutes[F] =
    val dsl = new Http4sDsl[F] {}
    import dsl._
    HttpRoutes.of[F]:
      case GET -> Root / "messages" =>
        for
          _ <- config.messages()
          resp <- Ok:
            s"messages for all ids"
        yield
          resp

  def incrementMachine[F[_] : Sync](
    config: IncrementMachine[F]
  ): HttpRoutes[F] =
    val dsl = new Http4sDsl[F] {}
    import dsl._
    HttpRoutes.of[F]:
      case POST -> Root / "increment" / id/ amount =>
        for
          _ <- config.messages(id, amount)
          resp <- Ok:
            s"incrementing id=$id by $amount"
        yield
          resp