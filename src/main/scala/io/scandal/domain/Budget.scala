package io.scandal.domain

trait Budget

object Budget {
  case class Daily(amount: Double, currency: String) extends Budget
  case class WholePeriod() extends Budget
}

