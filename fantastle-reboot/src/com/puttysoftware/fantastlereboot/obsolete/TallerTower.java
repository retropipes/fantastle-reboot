/*  TallerTower: An RPG
Copyright (C) 2008-2012 Eric Ahnell

Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.fantastlereboot.obsolete;

public class TallerTower {
    // Constants
    private static Application application;

    // Methods
    public static Application getApplication() {
        if (TallerTower.application == null) {
            TallerTower.application = new Application();
        }
        return TallerTower.application;
    }
}
