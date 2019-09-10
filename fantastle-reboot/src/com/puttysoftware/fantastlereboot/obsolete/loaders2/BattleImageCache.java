/*  TallerTower: An RPG
Copyright (C) 2008-2012 Eric Ahnell

Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.fantastlereboot.obsolete.loaders2;

import com.puttysoftware.images.BufferedImageIcon;

public class BattleImageCache {
    // Fields
    private static CacheEntry[] cache;
    private static int CACHE_INCREMENT = 20;
    private static int CACHE_SIZE = 0;

    // Methods
    static BufferedImageIcon getCachedImage(final String name,
            final String baseName) {
        if (!BattleImageCache.isInCache(name)) {
            final BufferedImageIcon bii = BattleImageManager
                    .getUncachedImage(baseName);
            BattleImageCache.addToCache(name, bii);
        }
        for (int x = 0; x < BattleImageCache.cache.length; x++) {
            if (name.equals(BattleImageCache.cache[x].getName())) {
                return BattleImageCache.cache[x].getImage();
            }
        }
        return null;
    }

    private static void expandCache() {
        final CacheEntry[] tempCache = new CacheEntry[BattleImageCache.cache.length
                + BattleImageCache.CACHE_INCREMENT];
        for (int x = 0; x < BattleImageCache.CACHE_SIZE; x++) {
            tempCache[x] = BattleImageCache.cache[x];
        }
        BattleImageCache.cache = tempCache;
    }

    static synchronized void addToCache(final String name,
            final BufferedImageIcon bii) {
        if (BattleImageCache.cache == null) {
            BattleImageCache.cache = new CacheEntry[BattleImageCache.CACHE_INCREMENT];
        }
        if (BattleImageCache.CACHE_SIZE == BattleImageCache.cache.length) {
            BattleImageCache.expandCache();
        }
        BattleImageCache.cache[BattleImageCache.CACHE_SIZE] = new CacheEntry(
                bii, name);
        BattleImageCache.CACHE_SIZE++;
    }

    static synchronized boolean isInCache(final String name) {
        if (BattleImageCache.cache == null) {
            BattleImageCache.cache = new CacheEntry[BattleImageCache.CACHE_INCREMENT];
        }
        for (int x = 0; x < BattleImageCache.CACHE_SIZE; x++) {
            if (name.equals(BattleImageCache.cache[x].getName())) {
                return true;
            }
        }
        return false;
    }
}