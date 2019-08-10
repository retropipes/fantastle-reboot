/*  Worldz: A Maze-Solving Game
Copyright (C) 2008-2010 Eric Ahnell

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.

Any questions should be directed to the author via email at: fantastle@worldwizard.net
 */
package com.puttysoftware.fantastlereboot.resourcemanagers.datamanagers;

import com.puttysoftware.fantastlereboot.creatures.faiths.FaithConstants;
import com.puttysoftware.fileutils.ResourceStreamReader;

public class FaithDataManager {
    public static double[] getFaithData(final int f) {
        final String name = FaithConstants.FAITH_NAMES[f].toLowerCase();
        try (final ResourceStreamReader rsr = new ResourceStreamReader(
                FaithDataManager.class.getResourceAsStream(
                        "/assets/data/faith/"
                                + name + ".dat"))) {
            // Fetch data
            final int[] rawData = new int[FaithConstants.FAITHS_COUNT];
            for (int x = 0; x < rawData.length; x++) {
                try {
                    rawData[x] = rsr.readInt();
                } catch (final NumberFormatException nfe) {
                    rawData[x] = -3;
                }
            }
            rsr.close();
            // Parse raw data
            final double[] finalData = new double[rawData.length];
            for (int x = 0; x < rawData.length; x++) {
                double d = 0.0;
                final int i = rawData[x];
                if (i == -2) {
                    d = 0.5;
                } else if (i == -1) {
                    d = 2.0 / 3.0;
                } else if (i == 1) {
                    d = 1.5;
                } else if (i == 2) {
                    d = 2.0;
                } else if (i == 0) {
                    d = 1.0;
                } else {
                    d = 0.0;
                }
                finalData[x] = d;
            }
            return finalData;
        } catch (final Exception e) {
            return null;
        }
    }
}
