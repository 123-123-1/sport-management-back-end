package com.tongji.sportmanagement.Common;

public class ServiceException extends Exception
{
  int code = 500;
  public ServiceException()
  {
    super("unknown service exception");
  }

  public ServiceException(int _code, String _msg)
  {
    super(_msg);
    code = _code;
  }

  public int getCode()
  {
    return code;
  }
}
