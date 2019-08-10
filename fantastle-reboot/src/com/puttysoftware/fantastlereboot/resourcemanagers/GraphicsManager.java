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
package com.puttysoftware.fantastlereboot.resourcemanagers;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.UIManager;

import com.puttysoftware.fantastlereboot.FantastleReboot;
import com.puttysoftware.fantastlereboot.creatures.Element;
import com.puttysoftware.images.BufferedImageIcon;

public class GraphicsManager {
    public static final int MAX_MOBILE_WINDOW_SIZE = 320;
    public static final int MAX_DESKTOP_WINDOW_SIZE = 700;
    private static final Color TRANSPARENT = new Color(200, 100, 100);
    private static Color REPLACE = null;
    private static final Color REPLACE_STAT = new Color(0, 0, 0, 0);

    public static BufferedImageIcon getImage(final String name) {
        // Get it from the cache
        return ImageCache.getCachedImage(name);
    }

    static BufferedImageIcon getUncachedImage(final String name) {
        try {
            String dm;
            if (FantastleReboot.getApplication().getPrefsManager()
                    .isMobileModeEnabled()) {
                dm = "mobile";
            } else {
                dm = "desktop";
            }
            final String normalName = GraphicsManager.normalizeName(name);
            final URL url = GraphicsManager.class.getResource(
                    "/assets/graphics/" + dm
                            + "/objects/" + normalName + ".png");
            final BufferedImage image = ImageIO.read(url);
            final BufferedImageIcon icon = new BufferedImageIcon(image);
            return icon;
        } catch (final IOException ie) {
            return null;
        } catch (final NullPointerException np) {
            return null;
        } catch (final IllegalArgumentException ia) {
            return null;
        }
    }

    public static BufferedImageIcon getStatImage(final String name) {
        try {
            String dm;
            if (FantastleReboot.getApplication().getPrefsManager()
                    .isMobileModeEnabled()) {
                dm = "mobile";
            } else {
                dm = "desktop";
            }
            final String normalName = GraphicsManager.normalizeName(name);
            final URL url = GraphicsManager.class.getResource(
                    "/assets/graphics/" + dm
                            + "/stats/" + normalName + ".png");
            final BufferedImage image = ImageIO.read(url);
            final BufferedImageIcon icon = new BufferedImageIcon(image);
            final BufferedImageIcon result = new BufferedImageIcon(icon);
            for (int x = 0; x < GraphicsManager.getGraphicSize(); x++) {
                for (int y = 0; y < GraphicsManager.getGraphicSize(); y++) {
                    final int pixel = icon.getRGB(x, y);
                    final Color c = new Color(pixel);
                    if (c.equals(GraphicsManager.TRANSPARENT)) {
                        result.setRGB(x, y,
                                GraphicsManager.REPLACE_STAT.getRGB());
                    }
                }
            }
            return result;
        } catch (final IOException ie) {
            return null;
        } catch (final NullPointerException np) {
            return null;
        } catch (final IllegalArgumentException ia) {
            return null;
        }
    }

    public static BufferedImageIcon getTransformedImage(final String name) {
        if (GraphicsManager.REPLACE == null) {
            GraphicsManager.defineReplacementColor();
        }
        try {
            final BufferedImageIcon icon = ImageCache.getCachedImage(name);
            final BufferedImageIcon result = new BufferedImageIcon(icon);
            if (icon != null) {
                for (int x = 0; x < GraphicsManager.getGraphicSize(); x++) {
                    for (int y = 0; y < GraphicsManager.getGraphicSize(); y++) {
                        final int pixel = icon.getRGB(x, y);
                        final Color c = new Color(pixel);
                        if (c.equals(GraphicsManager.TRANSPARENT)) {
                            result.setRGB(x, y,
                                    GraphicsManager.REPLACE.getRGB());
                        }
                    }
                }
                return result;
            } else {
                return null;
            }
        } catch (final NullPointerException np) {
            return null;
        } catch (final IllegalArgumentException ia) {
            return null;
        }
    }

    public static BufferedImageIcon getCompositeImage(final String name1,
            final String name2) {
        try {
            final BufferedImageIcon icon1 = ImageCache.getCachedImage(name1);
            final BufferedImageIcon icon2 = ImageCache.getCachedImage(name2);
            final BufferedImageIcon result = new BufferedImageIcon(icon2);
            if (icon1 != null && icon2 != null) {
                for (int x = 0; x < GraphicsManager.getGraphicSize(); x++) {
                    for (int y = 0; y < GraphicsManager.getGraphicSize(); y++) {
                        final int pixel = icon2.getRGB(x, y);
                        final Color c = new Color(pixel);
                        if (c.equals(GraphicsManager.TRANSPARENT)) {
                            result.setRGB(x, y, icon1.getRGB(x, y));
                        }
                    }
                }
                return result;
            } else {
                return null;
            }
        } catch (final NullPointerException np) {
            return null;
        } catch (final IllegalArgumentException ia) {
            return null;
        }
    }

    public static BufferedImageIcon getVirtualCompositeImage(final String name1,
            final String name2, final String name3) {
        try {
            final BufferedImageIcon icon3 = ImageCache.getCachedImage(name3);
            final BufferedImageIcon icon2 = GraphicsManager
                    .getCompositeImage(name1, name2);
            final BufferedImageIcon result = new BufferedImageIcon(icon3);
            if (icon3 != null && icon2 != null) {
                for (int x = 0; x < GraphicsManager.getGraphicSize(); x++) {
                    for (int y = 0; y < GraphicsManager.getGraphicSize(); y++) {
                        final int pixel = icon3.getRGB(x, y);
                        final Color c = new Color(pixel);
                        if (c.equals(GraphicsManager.TRANSPARENT)) {
                            result.setRGB(x, y, icon2.getRGB(x, y));
                        }
                    }
                }
                return result;
            } else {
                return null;
            }
        } catch (final NullPointerException np) {
            return null;
        } catch (final IllegalArgumentException ia) {
            return null;
        }
    }

    public static BufferedImageIcon getLogo() {
        try {
            String dm;
            if (FantastleReboot.getApplication().getPrefsManager()
                    .isMobileModeEnabled()) {
                dm = "mobile";
            } else {
                dm = "desktop";
            }
            final URL url = GraphicsManager.class.getResource(
                    "/assets/graphics/" + dm
                            + "/logo/logo.png");
            final BufferedImage image = ImageIO.read(url);
            final BufferedImageIcon icon = new BufferedImageIcon(image);
            return icon;
        } catch (final IOException ie) {
            return null;
        } catch (final NullPointerException np) {
            return null;
        }
    }

    public static BufferedImageIcon getLoadingLogo() {
        try {
            final URL url = GraphicsManager.class.getResource(
                    "/assets/graphics/loading/loading.png");
            final BufferedImage image = ImageIO.read(url);
            final BufferedImageIcon icon = new BufferedImageIcon(image);
            return icon;
        } catch (final IOException ie) {
            return null;
        } catch (final NullPointerException np) {
            return null;
        }
    }

    public static BufferedImageIcon getMiniatureLogo() {
        try {
            String dm;
            if (FantastleReboot.getApplication().getPrefsManager()
                    .isMobileModeEnabled()) {
                dm = "mobile";
            } else {
                dm = "desktop";
            }
            final URL url = GraphicsManager.class.getResource(
                    "/assets/graphics/" + dm
                            + "/logo/minilogo.png");
            final BufferedImage image = ImageIO.read(url);
            final BufferedImageIcon icon = new BufferedImageIcon(image);
            return icon;
        } catch (final IOException ie) {
            return null;
        } catch (final NullPointerException np) {
            return null;
        }
    }

    public static BufferedImageIcon getMicroLogo() {
        try {
            String dm;
            if (FantastleReboot.getApplication().getPrefsManager()
                    .isMobileModeEnabled()) {
                dm = "mobile";
            } else {
                dm = "desktop";
            }
            final URL url = GraphicsManager.class.getResource(
                    "/assets/graphics/" + dm
                            + "/logo/micrologo.png");
            final BufferedImage image = ImageIO.read(url);
            final BufferedImageIcon icon = new BufferedImageIcon(image);
            return icon;
        } catch (final IOException ie) {
            return null;
        } catch (final NullPointerException np) {
            return null;
        }
    }

    public static BufferedImageIcon getBossImage() {
        if (GraphicsManager.REPLACE == null) {
            GraphicsManager.defineReplacementColor();
        }
        try {
            final BufferedImageIcon icon = GraphicsManager.getBossTemplate();
            final BufferedImageIcon result = new BufferedImageIcon(icon);
            if (icon != null) {
                for (int x = 0; x < GraphicsManager.getGraphicSize(); x++) {
                    for (int y = 0; y < GraphicsManager.getGraphicSize(); y++) {
                        final int pixel = icon.getRGB(x, y);
                        final Color c = new Color(pixel);
                        if (c.equals(GraphicsManager.TRANSPARENT)) {
                            result.setRGB(x, y,
                                    GraphicsManager.REPLACE.getRGB());
                        }
                    }
                }
                return result;
            } else {
                return null;
            }
        } catch (final NullPointerException np) {
            return null;
        } catch (final IllegalArgumentException ia) {
            return null;
        }
    }

    private static BufferedImageIcon getBossTemplate() {
        try {
            String dm;
            if (FantastleReboot.getApplication().getPrefsManager()
                    .isMobileModeEnabled()) {
                dm = "mobile";
            } else {
                dm = "desktop";
            }
            final URL url = GraphicsManager.class.getResource(
                    "/assets/graphics/" + dm
                            + "/boss/boss.png");
            final BufferedImage image = ImageIO.read(url);
            final BufferedImageIcon icon = new BufferedImageIcon(image);
            return icon;
        } catch (final IOException ie) {
            return null;
        } catch (final NullPointerException np) {
            return null;
        } catch (final IllegalArgumentException ia) {
            return null;
        }
    }

    public static BufferedImageIcon getMonsterImage(final String name,
            final Element element) {
        final BufferedImageIcon template = MonsterImageCache
                .getCachedMonsterImage(name);
        final BufferedImageIcon templateOut = new BufferedImageIcon(
                MonsterImageCache.getCachedMonsterImage(name));
        if (template != null) {
            final int w = template.getWidth();
            final int h = template.getHeight();
            int pixel;
            for (int x = 0; x < w; x++) {
                for (int y = 0; y < h; y++) {
                    pixel = template.getRGB(x, y);
                    final Color old = new Color(pixel);
                    final Color transformed = element.applyTransform(old);
                    pixel = transformed.getRGB();
                    templateOut.setRGB(x, y, pixel);
                }
            }
            return templateOut;
        } else {
            return null;
        }
    }

    static BufferedImageIcon getUncachedMonsterImage(final String name) {
        final BufferedImage template = GraphicsManager.getMonsterTemplate(name);
        if (template != null) {
            return new BufferedImageIcon(template);
        } else {
            return null;
        }
    }

    private static BufferedImage getMonsterTemplate(final String name) {
        try {
            String dm;
            if (FantastleReboot.getApplication().getPrefsManager()
                    .isMobileModeEnabled()) {
                dm = "mobile";
            } else {
                dm = "desktop";
            }
            final String normalName = GraphicsManager.normalizeName(name);
            final URL url = GraphicsManager.class.getResource(
                    "/assets/graphics/" + dm
                            + "/monsters/" + normalName + ".png");
            final BufferedImage image = ImageIO.read(url);
            if (image != null) {
                return image;
            } else {
                return null;
            }
        } catch (final IOException ie) {
            return null;
        } catch (final NullPointerException np) {
            return null;
        } catch (final IllegalArgumentException ia) {
            return null;
        }
    }

    private static String normalizeName(final String name) {
        final StringBuffer sb = new StringBuffer(name);
        for (int x = 0; x < sb.length(); x++) {
            if (!Character.isLetter(sb.charAt(x))
                    && !Character.isDigit(sb.charAt(x))) {
                sb.setCharAt(x, '_');
            }
        }
        return sb.toString().toLowerCase();
    }

    private static void defineReplacementColor() {
        if (System.getProperty("os.name").startsWith("Mac OS X")) {
            GraphicsManager.REPLACE = UIManager.getColor("text");
        } else {
            GraphicsManager.REPLACE = UIManager.getColor("control");
        }
    }

    public static int getGraphicSize() {
        if (FantastleReboot.getApplication().getPrefsManager()
                .isMobileModeEnabled()) {
            return 24;
        } else {
            return 48;
        }
    }
}
