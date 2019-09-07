/*  TallerTower: An RPG
Copyright (C) 2008-2012 Eric Ahnell

Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.fantastlereboot.loaders.older;

import com.puttysoftware.images.BufferedImageIcon;

public class ObjectImageCache {
    // Fields
    private static CacheEntry[] cache;
    private static int CACHE_INCREMENT = 20;
    private static int CACHE_SIZE = 0;

    // Methods
    static BufferedImageIcon getCachedImage(final String name,
            final String baseName) {
        if (!ObjectImageCache.isInCache(name)) {
            final BufferedImageIcon bii = ObjectImageManager
                    .getUncachedImage(baseName);
            ObjectImageCache.addToCache(name, bii);
        }
        for (int x = 0; x < ObjectImageCache.cache.length; x++) {
            if (name.equals(ObjectImageCache.cache[x].getName())) {
                return ObjectImageCache.cache[x].getImage();
            }
        }
        return null;
    }

    private static void expandCache() {
        final CacheEntry[] tempCache = new CacheEntry[ObjectImageCache.cache.length
                + ObjectImageCache.CACHE_INCREMENT];
        for (int x = 0; x < ObjectImageCache.CACHE_SIZE; x++) {
            tempCache[x] = ObjectImageCache.cache[x];
        }
        ObjectImageCache.cache = tempCache;
    }

    static synchronized void addToCache(final String name,
            final BufferedImageIcon bii) {
        if (ObjectImageCache.cache == null) {
            ObjectImageCache.cache = new CacheEntry[ObjectImageCache.CACHE_INCREMENT];
        }
        if (ObjectImageCache.CACHE_SIZE == ObjectImageCache.cache.length) {
            ObjectImageCache.expandCache();
        }
        ObjectImageCache.cache[ObjectImageCache.CACHE_SIZE] = new CacheEntry(
                bii, name);
        ObjectImageCache.CACHE_SIZE++;
    }

    static synchronized boolean isInCache(final String name) {
        if (ObjectImageCache.cache == null) {
            ObjectImageCache.cache = new CacheEntry[ObjectImageCache.CACHE_INCREMENT];
        }
        for (int x = 0; x < ObjectImageCache.CACHE_SIZE; x++) {
            if (name.equals(ObjectImageCache.cache[x].getName())) {
                return true;
            }
        }
        return false;
    }
}