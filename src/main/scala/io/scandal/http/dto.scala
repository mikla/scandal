package io.scandal.http

import io.scandal.domain.{Advertisement, ClickEvent, ImpressionEvent}

object dto {

  case class CreateAdRequest(id: String, name: String, adText: String) {
    def toDomain: Advertisement = Advertisement(
      id,
      name,
      adText
    )
  }

  case class RegisterImpression(adId: String) {
    def toDomain = ImpressionEvent(adId, _)
  }

  case class RegisterClick(adId: String) {
    def toDomain = ClickEvent(adId, _)
  }

}
