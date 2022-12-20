package io.scandal.domain

trait TargetDevice

object TargetDevice {
  case object Any extends TargetDevice
  case object Mobile extends TargetDevice
  case object Web extends TargetDevice
}
