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

Any questions should be directed to the author via email at: worldz@worldwizard.net
 */
package com.puttysoftware.fantastlereboot.loaders.data;

import java.io.IOException;
import java.util.ArrayList;

import com.puttysoftware.fileutils.ResourceStreamReader;

public class SoundDataManager {
    private SoundDataManager() {
        // Do nothing
    }

    public static String[] getSoundData() {
        try (final ResourceStreamReader rsr = new ResourceStreamReader(
                SoundDataManager.class.getResourceAsStream(
                        "/assets/data/sounds/files.txt"))) {
            // Fetch data
            final ArrayList<String> data = new ArrayList<>();
            String raw = "0";
            while (raw != null) {
                raw = rsr.readString();
                if (raw != null) {
                    data.add(raw);
                }
            }
            return data.toArray(new String[data.size()]);
        } catch (final IOException e) {
            return new String[0];
        }
    }
}