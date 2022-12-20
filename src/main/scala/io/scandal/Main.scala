package io.scandal

import cats.effect._
import cats.effect.unsafe.implicits.global
import io.circe.generic.auto._
import io.circe.parser._
import io.circe.syntax.EncoderOps
import io.scandal.http.dto.{CreateAdRequest, RegisterClick, RegisterImpression}
import io.scandal.service.{AdvertisementService, ClickService, ImpressionService}
import natchez.Trace.Implicits.noop
import skunk.Session

import java.time.LocalDateTime

object Main extends cask.MainRoutes {

  val session: Resource[IO, Session[IO]] = Session.single[IO](
    host = "localhost",
    port = 5432,
    user = "postgres",
    database = "scandal",
    password = Some("mysecretpassword")
  )

  @cask.get("/api/v1/ad")
  def getAd() =
    session
      .use { s =>
        AdvertisementService.fromSession(s).get
      }
      .unsafeRunSync()
      .map(r => cask.Response(r.asJson.spaces4))
      .getOrElse(cask.Response("no ads", 404))

  @cask.post("/api/v1/ad/create")
  def createAd(request: cask.Request): Unit =
    session
      .use { s =>
        parse(request.text()).flatMap(_.as[CreateAdRequest]) match {
          case Left(value) => throw new Exception(value)
          case Right(value) =>
            AdvertisementService.fromSession(s).create(value.toDomain)
        }
      }
      .unsafeRunSync()

  @cask.post("/api/v1/impression/register")
  def registerImpression(request: cask.Request): Unit = session
    .use { s =>
      parse(request.text()).flatMap(_.as[RegisterImpression]) match {
        case Left(value) => throw new Exception(value)
        case Right(value) =>
          ImpressionService
            .fromSession(s)
            .registerImpression(value.toDomain(LocalDateTime.now()))
      }
    }
    .unsafeRunSync()

  @cask.post("/api/v1/click/register")
  def registerClick(request: cask.Request): Unit = session
    .use { s =>
      parse(request.text()).flatMap(_.as[RegisterClick]) match {
        case Left(value) => throw new Exception(value)
        case Right(value) =>
          ClickService
            .fromSession(s)
            .registerClick(value.toDomain(LocalDateTime.now()))
      }
    }
    .unsafeRunSync()

  @cask.delete("/api/v1/ad/remove/:id")
  def removeAd(id: String): Unit =
    session
      .use { s =>
        AdvertisementService.fromSession(s).delete(id)
      }
      .unsafeRunSync()

  initialize()

}
