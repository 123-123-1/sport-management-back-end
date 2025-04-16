package com.tongji.sportmanagement.ReservationSubsystem.Entity;

public enum ReservationOperation
{
  reserve, join, sysvalidate, validate, usercancel, managercancel, cancelall, violate, sign;

  public static ReservationOperation getManagerOperationByUserState(ReservationUserState state)
  {
    switch (state) {
      case reserved:
        return validate;
      case cancelled:
        return managercancel;
      case violated:
        return violate;
      default:
        return null;
    }
  }

  public static ReservationOperation getManagerOperationByReservationState(ReservationState state)
  {
    switch (state) {
      case normal:
        return validate;
      case cancelled:
        return cancelall;
      default:
        return null;
    }
  }
}
