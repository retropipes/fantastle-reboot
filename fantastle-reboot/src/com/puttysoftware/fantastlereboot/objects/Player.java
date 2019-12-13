package com.puttysoftware.fantastlereboot.objects;

import com.puttysoftware.diane.loaders.ColorReplaceRules;
import com.puttysoftware.fantastlereboot.assets.ObjectImageIndex;
import com.puttysoftware.fantastlereboot.loaders.AvatarImageLoader;
import com.puttysoftware.fantastlereboot.objectmodel.FantastleObject;
import com.puttysoftware.images.BufferedImageIcon;

public final class Player extends FantastleObject {
  private static int avatarFamily = -1;
  private static ColorReplaceRules avatarRules;

  public Player() {
    super(72, "player", ObjectImageIndex.PLAYER);
    this.setSavedObject(new OpenSpace());
  }

  public static void setAvatar(final int family,
      final ColorReplaceRules rules) {
    Player.avatarFamily = family;
    Player.avatarRules = rules;
  }

  @Override
  protected BufferedImageIcon getGameImageHook() {
    if (Player.avatarFamily != -1 && Player.avatarRules != null) {
      return AvatarImageLoader.load(Player.avatarFamily, Player.avatarRules);
    }
    return super.getGameImageHook();
  }
}
