package com.google.cloud.datalineage.producerclient;

/** A factory that returns ConnectionCache based on LineageEnablementCacheSettings. */
public class LineageEnablementCacheFactory {
  private static volatile LineageEnablementCache commonInstance;

  /** Make the factory class non-instantiable */
  private LineageEnablementCacheFactory() {}

  public static LineageEnablementCache get(CacheSettings settings) {
    if (!settings.getEnabled()) {
      return new NoOpLineageEnablementCache();
    }

    if (!settings.getUseCommonInstance()) {
      return new StandardLineageEnablementCache(settings.getOptions());
    }

    if (commonInstance != null) {
      return commonInstance;
    }

    synchronized (LineageEnablementCacheFactory.class) {
      if (commonInstance == null) {
        commonInstance = new StandardLineageEnablementCache(settings.getOptions());
      }
    }

    return commonInstance;
  }
}
