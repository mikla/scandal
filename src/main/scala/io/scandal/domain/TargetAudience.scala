package io.scandal.domain

case class TargetAudience(
    age: Range,
    sex: Sex,
    languages: List[String],
    geohash: GeoHash
)
