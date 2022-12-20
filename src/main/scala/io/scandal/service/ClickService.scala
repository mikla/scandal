package io.scandal.service

import cats.effect.MonadCancel
import cats.implicits._
import io.scandal.domain.ClickEvent
import skunk.codec.all._
import skunk.implicits._
import skunk.{Command, Session}

trait ClickService[F[_]] {

  def registerClick(event: ClickEvent): F[Unit]

}

object ClickService {

  val insertEvent: Command[ClickEvent] =
    sql"INSERT INTO click_events VALUES ($varchar, $timestamp)".command
      .gcontramap[ClickEvent]

  def fromSession[F[_]](
      session: Session[F]
  )(implicit ev: MonadCancel[F, Throwable]): ClickService[F] =
    new ClickService[F] {
      override def registerClick(event: ClickEvent): F[Unit] =
        session.prepare(insertEvent).use(_.execute(event)).void
    }

}
