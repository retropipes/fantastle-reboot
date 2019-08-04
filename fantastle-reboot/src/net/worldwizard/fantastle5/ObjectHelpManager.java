/*  Fantastle: A Maze-Solving Game
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
package net.worldwizard.fantastle5;

import java.awt.FlowLayout;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import com.puttysoftware.help.GraphicalHelpViewer;
import com.puttysoftware.images.BufferedImageIcon;

import net.worldwizard.fantastle5.generic.MazeObjectList;
import net.worldwizard.fantastle5.resourcemanagers.GraphicsManager;

public class ObjectHelpManager {
    // Fields
    private final JFrame helpFrame;
    private final MazeObjectList objectList;
    private final String[] objectNames;
    private final BufferedImageIcon[] objectAppearances;
    private final GraphicalHelpViewer hv;

    // Constructors
    public ObjectHelpManager() {
        this.objectList = Fantastle5.getApplication().getObjects();
        this.objectNames = this.objectList.getAllDescriptions();
        this.objectAppearances = this.objectList.getAllEditorAppearances();
        this.hv = new GraphicalHelpViewer(this.objectAppearances,
                this.objectNames);
        this.helpFrame = new JFrame("Fantastle Object Help");
        this.helpFrame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        this.helpFrame.setLayout(new FlowLayout());
        this.helpFrame.add(this.hv.getHelp());
        if (Fantastle5.getApplication().getPrefsManager().isMobileModeEnabled()) {
            this.hv.setHelpSize(GraphicsManager.MAX_MOBILE_WINDOW_SIZE,
                    GraphicsManager.MAX_MOBILE_WINDOW_SIZE);
        } else {
            this.hv.setHelpSize(GraphicsManager.MAX_DESKTOP_WINDOW_SIZE,
                    GraphicsManager.MAX_DESKTOP_WINDOW_SIZE);
        }
        this.helpFrame.pack();
        this.helpFrame.setResizable(false);
    }

    // Methods
    public void showHelp() {
        this.helpFrame.setVisible(true);
    }

    public void updateHelpSize() {
        if (Fantastle5.getApplication().getPrefsManager().isMobileModeEnabled()) {
            this.hv.setHelpSize(GraphicsManager.MAX_MOBILE_WINDOW_SIZE,
                    GraphicsManager.MAX_MOBILE_WINDOW_SIZE);
        } else {
            this.hv.setHelpSize(GraphicsManager.MAX_DESKTOP_WINDOW_SIZE,
                    GraphicsManager.MAX_DESKTOP_WINDOW_SIZE);
        }
        this.helpFrame.pack();
    }
}
