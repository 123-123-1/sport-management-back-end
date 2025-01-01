package com.tongji.sportmanagement.Common;

import java.lang.reflect.Field;

public class SportManagementUtils
{
  public static void copyNotNullProperties(Object src, Object dst) throws Exception
  {
    if (src == null || dst == null) {
      throw new IllegalArgumentException("copyNotNullProperties: src和dst必须非空");
    }
    Class<?> srcClass = src.getClass();
    Field[] srcFields = srcClass.getDeclaredFields();
    for (Field srcField : srcFields) {
      srcField.setAccessible(true);
      Object srcValue = srcField.get(src);
      if (srcValue != null) {
        // 执行复制操作
        Field dstField = dst.getClass().getDeclaredField(srcField.getName());
        dstField.setAccessible(true);
        dstField.set(dst, srcValue);
        dstField.setAccessible(false);
      }
      srcField.setAccessible(false);
    }
  }
}