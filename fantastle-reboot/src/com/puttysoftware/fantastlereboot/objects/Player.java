package com.puttysoftware.fantastlereboot.objects;

import com.puttysoftware.fantastlereboot.assets.ObjectImageIndex;
import com.puttysoftware.fantastlereboot.loaders.AvatarImageLoader;
import com.puttysoftware.fantastlereboot.objectmodel.FantastleObject;
import com.puttysoftware.images.BufferedImageIcon;

public final class Player extends FantastleObject {
  private static int avatarFamily = -1;
  private static int avatarSkin = -1;
  private static int avatarHair = -1;

  public Player() {
    super(72, "player", ObjectImageIndex.PLAYER);
    this.setSavedObject(new OpenSpace());
  }

  public static void setAvatar(final int af, final int as, final int ah) {
    Player.avatarFamily = af;
    Player.avatarSkin = as;
    Player.avatarHair = ah;
  }

  private static boolean avatarRangeCheck() {
    return Player.avatarFamily >= 0 && Player.avatarFamily <= 5
        && Player.avatarSkin >= 0 && Player.avatarSkin <= 9
        && Player.avatarHair >= 0 && Player.avatarHair <= 9;
  }

  @Override
  protected BufferedImageIcon getGameImageHook() {
    if (Player.avatarRangeCheck()) {
      return AvatarImageLoader.load(Player.avatarFamily, Player.avatarSkin,
          Player.avatarHair);
    }
    return super.getGameImageHook();
  }
}
