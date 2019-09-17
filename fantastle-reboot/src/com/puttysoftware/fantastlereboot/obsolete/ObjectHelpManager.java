/*  TallerTower: An RPG
Copyright (C) 2008-2012 Eric Ahnell

Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.fantastlereboot.obsolete;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

import com.puttysoftware.fantastlereboot.obsolete.loaders.ImageTransformer;
import com.puttysoftware.fantastlereboot.utilities.MazeObjectList;
import com.puttysoftware.help.GraphicalHelpViewer;
import com.puttysoftware.images.BufferedImageIcon;

public class ObjectHelpManager {
    // Fields
    private JFrame helpFrame;
    GraphicalHelpViewer hv;
    private boolean inited = false;

    // Constructors
    public ObjectHelpManager() {
        // Do nothing
    }

    // Methods
    public void showHelp() {
        this.initHelp();
        this.helpFrame.setVisible(true);
    }

    private void initHelp() {
        if (!this.inited) {
            final ButtonHandler buttonHandler = new ButtonHandler();
            final MazeObjectList objectList = TallerTower.getApplication()
                    .getObjects();
            final String[] objectNames = objectList.getAllDescriptions();
            final BufferedImageIcon[] objectAppearances = objectList
                    .getAllEditorAppearances();
            this.hv = new GraphicalHelpViewer(objectAppearances, objectNames,
                    new Color(223, 223, 223));
            final JButton export = new JButton("Export");
            export.addActionListener(buttonHandler);
            this.helpFrame = new JFrame("TallerTower Object Help");
            final Image iconlogo = Application.getIconLogo();
            this.helpFrame.setIconImage(iconlogo);
            this.helpFrame
                    .setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
            this.helpFrame.setLayout(new BorderLayout());
            this.helpFrame.add(this.hv.getHelp(), BorderLayout.CENTER);
            this.helpFrame.add(export, BorderLayout.SOUTH);
            this.hv.setHelpSize(ImageTransformer.MAX_WINDOW_SIZE,
                    ImageTransformer.MAX_WINDOW_SIZE);
            this.helpFrame.pack();
            this.helpFrame.setResizable(false);
            // Mac OS X-specific fixes
            if (System.getProperty("os.name").startsWith("Mac OS X")) {
                final MenuManager menu = new MenuManager();
                menu.setHelpMenus();
                this.helpFrame.setJMenuBar(menu.getMainMenuBar());
            }
            this.inited = true;
        }
    }

    private class ButtonHandler implements ActionListener {
        ButtonHandler() {
            // Do nothing
        }

        @Override
        public void actionPerformed(final ActionEvent e) {
            ObjectHelpManager.this.hv.exportHelp();
        }
    }
}
