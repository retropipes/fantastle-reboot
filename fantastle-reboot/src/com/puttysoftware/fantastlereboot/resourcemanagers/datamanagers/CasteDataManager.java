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

import com.puttysoftware.fantastlereboot.creatures.castes.CasteConstants;
import com.puttysoftware.fileutils.ResourceStreamReader;

public class CasteDataManager {
    public static int[] getCasteData(final int c) {
        final String name = CasteConstants.CASTE_NAMES[c].toLowerCase();
        try (final ResourceStreamReader rsr = new ResourceStreamReader(
                CasteDataManager.class.getResourceAsStream(
                        "/net/worldwizard/fantastle5/resources/data/caste/"
                                + name + ".dat"))) {
            // Fetch data
            final int[] rawData = new int[CasteConstants.CASTES_ATTRIBUTE_COUNT];
            for (int x = 0; x < rawData.length; x++) {
                try {
                    rawData[x] = rsr.readInt();
                } catch (final NumberFormatException nfe) {
                    rawData[x] = 0;
                }
            }
            rsr.close();
            return rawData;
        } catch (final Exception e) {
            return null;
        }
    }
}
