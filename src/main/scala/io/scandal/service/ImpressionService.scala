package io.scandal.service

import cats.effect.MonadCancel
import cats.implicits._
import io.scandal.domain.ImpressionEvent
import skunk.codec.all._
import skunk.implicits._
import skunk.{Command, Session}

trait ImpressionService[F[_]] {

  def registerImpression(event: ImpressionEvent): F[Unit]

}

object ImpressionService {

  val insertEvent: Command[ImpressionEvent] =
    sql"INSERT INTO impression_events VALUES ($varchar, $timestamp)".command
      .gcontramap[ImpressionEvent]

  def fromSession[F[_]](
      session: Session[F]
  )(implicit ev: MonadCancel[F, Throwable]): ImpressionService[F] =
    new ImpressionService[F] {
      override def registerImpression(event: ImpressionEvent): F[Unit] =
        session.prepare(insertEvent).use(_.execute(event)).void
    }

}
