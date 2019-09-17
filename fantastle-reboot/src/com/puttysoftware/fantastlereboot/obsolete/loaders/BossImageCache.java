/*  TallerTower: An RPG
Copyright (C) 2008-2012 Eric Ahnell

Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.fantastlereboot.obsolete.loaders;

import com.puttysoftware.images.BufferedImageIcon;

public class BossImageCache {
    // Fields
    private static CacheEntry[] cache;
    private static int CACHE_INCREMENT = 20;
    private static int CACHE_SIZE = 0;

    // Methods
    static BufferedImageIcon getCachedImage(final String name) {
        if (!BossImageCache.isInCache(name)) {
            final BufferedImageIcon bii = BossImageManager
                    .getUncachedImage(name);
            BossImageCache.addToCache(name, bii);
        }
        for (int x = 0; x < BossImageCache.cache.length; x++) {
            if (name.equals(BossImageCache.cache[x].getName())) {
                return BossImageCache.cache[x].getImage();
            }
        }
        return null;
    }

    private static void expandCache() {
        final CacheEntry[] tempCache = new CacheEntry[BossImageCache.cache.length
                + BossImageCache.CACHE_INCREMENT];
        for (int x = 0; x < BossImageCache.CACHE_SIZE; x++) {
            tempCache[x] = BossImageCache.cache[x];
        }
        BossImageCache.cache = tempCache;
    }

    static synchronized void addToCache(final String name,
            final BufferedImageIcon bii) {
        if (BossImageCache.cache == null) {
            BossImageCache.cache = new CacheEntry[BossImageCache.CACHE_INCREMENT];
        }
        if (BossImageCache.CACHE_SIZE == BossImageCache.cache.length) {
            BossImageCache.expandCache();
        }
        BossImageCache.cache[BossImageCache.CACHE_SIZE] = new CacheEntry(bii,
                name);
        BossImageCache.CACHE_SIZE++;
    }

    static synchronized boolean isInCache(final String name) {
        if (BossImageCache.cache == null) {
            BossImageCache.cache = new CacheEntry[BossImageCache.CACHE_INCREMENT];
        }
        for (int x = 0; x < BossImageCache.CACHE_SIZE; x++) {
            if (name.equals(BossImageCache.cache[x].getName())) {
                return true;
            }
        }
        return false;
    }
}