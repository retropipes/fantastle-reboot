/*  TallerTower: An RPG
Copyright (C) 2008-2012 Eric Ahnell

Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.fantastlereboot.obsolete.loaders2;

import com.puttysoftware.images.BufferedImageIcon;

public class LogoCache {
    // Fields
    private static CacheEntry[] cache;
    private static int CACHE_INCREMENT = 5;
    private static int CACHE_SIZE = 0;

    // Methods
    static BufferedImageIcon getCachedLogo(final String name) {
        if (!LogoCache.isInCache(name)) {
            final BufferedImageIcon bii = LogoManager.getUncachedLogo(name);
            LogoCache.addToCache(name, bii);
        }
        for (int x = 0; x < LogoCache.cache.length; x++) {
            if (name.equals(LogoCache.cache[x].getName())) {
                return LogoCache.cache[x].getImage();
            }
        }
        return null;
    }

    private static void expandCache() {
        final CacheEntry[] tempCache = new CacheEntry[LogoCache.cache.length
                + LogoCache.CACHE_INCREMENT];
        for (int x = 0; x < LogoCache.CACHE_SIZE; x++) {
            tempCache[x] = LogoCache.cache[x];
        }
        LogoCache.cache = tempCache;
    }

    private static synchronized void addToCache(final String name,
            final BufferedImageIcon bii) {
        if (LogoCache.cache == null) {
            LogoCache.cache = new CacheEntry[LogoCache.CACHE_INCREMENT];
        }
        if (LogoCache.CACHE_SIZE == LogoCache.cache.length) {
            LogoCache.expandCache();
        }
        LogoCache.cache[LogoCache.CACHE_SIZE] = new CacheEntry(bii, name);
        LogoCache.CACHE_SIZE++;
    }

    private static synchronized boolean isInCache(final String name) {
        if (LogoCache.cache == null) {
            LogoCache.cache = new CacheEntry[LogoCache.CACHE_INCREMENT];
        }
        for (int x = 0; x < LogoCache.CACHE_SIZE; x++) {
            if (name.equals(LogoCache.cache[x].getName())) {
                return true;
            }
        }
        return false;
    }
}