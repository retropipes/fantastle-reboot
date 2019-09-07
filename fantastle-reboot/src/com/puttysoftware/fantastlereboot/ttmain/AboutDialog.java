/*  TallerTower: An RPG
Copyright (C) 2008-2012 Eric Ahnell

Any questions should be directed to the author via email at: products@puttysoftware.com
 */
package com.puttysoftware.fantastlereboot.ttmain;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

import com.puttysoftware.fantastlereboot.loaders.older.LogoManager;

public class AboutDialog {
    // Fields
    private JFrame aboutFrame;

    // Constructors
    public AboutDialog(final String ver) {
        this.setUpGUI(ver);
    }

    // Methods
    public void showAboutDialog() {
        this.aboutFrame.setVisible(true);
    }

    void hideAboutDialog() {
        this.aboutFrame.setVisible(false);
    }

    private void setUpGUI(final String ver) {
        final EventHandler handler = new EventHandler();
        this.aboutFrame = new JFrame("About TallerTower");
        final Image iconlogo = LogoManager.getIconLogo();
        this.aboutFrame.setIconImage(iconlogo);
        final Container aboutPane = new Container();
        final Container textPane = new Container();
        final Container buttonPane = new Container();
        final Container logoPane = new Container();
        final JButton aboutOK = new JButton("OK");
        final JLabel miniLabel = new JLabel("", LogoManager.getMiniatureLogo(),
                SwingConstants.LEFT);
        miniLabel.setLabelFor(null);
        aboutOK.setDefaultCapable(true);
        this.aboutFrame.getRootPane().setDefaultButton(aboutOK);
        this.aboutFrame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        aboutPane.setLayout(new BorderLayout());
        logoPane.setLayout(new FlowLayout());
        logoPane.add(miniLabel);
        textPane.setLayout(new GridLayout(4, 1));
        textPane.add(new JLabel("TallerTower Version: " + ver));
        textPane.add(new JLabel("Author: Eric Ahnell"));
        textPane.add(new JLabel(
                "Web Site: http://www.puttysoftware.com/tallertower/"));
        textPane.add(new JLabel(
                "E-mail bug reports to: products@puttysoftware.com  "));
        buttonPane.setLayout(new FlowLayout());
        buttonPane.add(aboutOK);
        aboutPane.add(logoPane, BorderLayout.WEST);
        aboutPane.add(textPane, BorderLayout.CENTER);
        aboutPane.add(buttonPane, BorderLayout.SOUTH);
        this.aboutFrame.setResizable(false);
        aboutOK.addActionListener(handler);
        this.aboutFrame.setContentPane(aboutPane);
        this.aboutFrame.pack();
    }

    private class EventHandler implements ActionListener {
        EventHandler() {
            // Do nothing
        }

        // Handle buttons
        @Override
        public void actionPerformed(final ActionEvent e) {
            try {
                final AboutDialog ad = AboutDialog.this;
                final String cmd = e.getActionCommand();
                if (cmd.equals("OK")) {
                    ad.hideAboutDialog();
                }
            } catch (final Exception ex) {
                TallerTower.getErrorLogger().logError(ex);
            }
        }
    }
}