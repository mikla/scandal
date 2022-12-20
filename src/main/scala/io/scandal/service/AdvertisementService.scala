package io.scandal.service

import cats.effect.MonadCancel
import cats.implicits._
import io.scandal.domain.Advertisement
import skunk.codec.all._
import skunk.implicits._
import skunk.{Command, Query, Session}

trait AdvertisementService[F[_]] {

  def create(ad: Advertisement): F[Unit]

  def delete(adId: String): F[Unit]

  /** Selects and AD with highest CRP ratio
    * @return
    */
  def get: F[Option[Advertisement]]

}

object AdvertisementService {

  private val insertAd: Command[Advertisement] =
    sql"INSERT INTO ads VALUES ($varchar, $varchar, $varchar)".command
      .gcontramap[Advertisement]

  private val deleteAd: Command[String] =
    sql"DELETE FROM ads WHERE id = $varchar".command

  private val selectHighestAd: Query[skunk.Void, Advertisement] =
    sql"select * from ads as ad where ad.id = (select a.id from ctp as a group by a.id order by max(a.ctp) desc limit 1)"
      .query(varchar ~ varchar ~ varchar)
      .gmap[Advertisement]

  def fromSession[F[_]](
      session: Session[F]
  )(implicit ev: MonadCancel[F, Throwable]): AdvertisementService[F] =
    new AdvertisementService[F] {
      override def create(ad: Advertisement): F[Unit] =
        session.prepare(insertAd).use(_.execute(ad)).void

      override def delete(adId: String): F[Unit] =
        session.prepare(deleteAd).use(_.execute(adId)).void

      override def get: F[Option[Advertisement]] =
        session.execute(selectHighestAd).map(_.headOption)
    }

}
