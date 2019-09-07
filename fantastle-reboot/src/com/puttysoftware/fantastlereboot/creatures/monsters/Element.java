/*  TallerTower: An RPG
Copyright (C) 2011-2012 Eric Ahnell

Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.fantastlereboot.creatures.monsters;

import com.puttysoftware.fantastlereboot.creatures.faiths.Faith;

public class Element {
    // Fields
    private final double transformRed;
    private final double transformGreen;
    private final double transformBlue;
    private final String name;
    private final Faith faith;

    // Constructor
    public Element(final Faith f) {
        this.faith = f;
        this.transformRed = f.getColor().getRed() / 256.0;
        this.transformGreen = f.getColor().getGreen() / 256.0;
        this.transformBlue = f.getColor().getBlue() / 256.0;
        this.name = f.getName();
    }

    // Methods
    public String getName() {
        return this.name;
    }

    public Faith getFaith() {
        return this.faith;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((this.faith == null) ? 0 : this.faith.hashCode());
        result = prime * result
                + ((this.name == null) ? 0 : this.name.hashCode());
        long temp;
        temp = Double.doubleToLongBits(this.transformBlue);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(this.transformGreen);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(this.transformRed);
        return prime * result + (int) (temp ^ (temp >>> 32));
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Element)) {
            return false;
        }
        final Element other = (Element) obj;
        if (this.faith == null) {
            if (other.faith != null) {
                return false;
            }
        } else if (!this.faith.equals(other.faith)) {
            return false;
        }
        if (this.name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!this.name.equals(other.name)) {
            return false;
        }
        if (Double.doubleToLongBits(this.transformBlue) != Double
                .doubleToLongBits(other.transformBlue)) {
            return false;
        }
        if (Double.doubleToLongBits(this.transformGreen) != Double
                .doubleToLongBits(other.transformGreen)) {
            return false;
        }
        if (Double.doubleToLongBits(this.transformRed) != Double
                .doubleToLongBits(other.transformRed)) {
            return false;
        }
        return true;
    }
}
