package com.puttysoftware.fantastlereboot.assets;

import com.puttysoftware.diane.loaders.ColorReplaceRules;

public final class AvatarImageModel {
  // Fields
  private final int familyID;
  private final int hairID;
  private final int skinID;
  private final int bodyID;
  private final int pantsID;
  private final int shoesID;
  private final int eyesID;
  private final ColorReplaceRules rules;

  public AvatarImageModel(final int legacyFamily, final int hair,
      final int skin) {
    this.familyID = 0;
    this.hairID = hair;
    this.skinID = skin;
    this.bodyID = legacyFamily;
    this.pantsID = legacyFamily;
    this.shoesID = skin;
    this.eyesID = 0;
    this.rules = new ColorReplaceRules();
    this.addRules();
  }

  public AvatarImageModel(final int family, final int hair, final int skin,
      final int body, final int pants, final int shoes, final int eyes) {
    this.familyID = family;
    this.hairID = hair;
    this.skinID = skin;
    this.bodyID = body;
    this.pantsID = pants;
    this.shoesID = shoes;
    this.eyesID = eyes;
    this.rules = new ColorReplaceRules();
    this.addRules();
  }

  private final void addRules() {
    this.rules.add(AvatarColors.hairBase, AvatarColors.hairTable[this.hairID]);
    this.rules.add(AvatarColors.skinBase, AvatarColors.skinTable[this.skinID]);
    this.rules.add(AvatarColors.bodyBase, AvatarColors.bodyTable[this.bodyID]);
    this.rules.add(AvatarColors.pantsBase,
        AvatarColors.pantsTable[this.pantsID]);
    this.rules.add(AvatarColors.shoesBase,
        AvatarColors.shoesTable[this.shoesID]);
    this.rules.add(AvatarColors.eyesBase, AvatarColors.eyesTable[this.eyesID]);
  }

  public int getAvatarFamilyID() {
    return this.familyID;
  }

  public int getAvatarHairID() {
    return this.hairID;
  }

  public int getAvatarSkinID() {
    return this.skinID;
  }

  public int getAvatarBodyID() {
    return this.bodyID;
  }

  public int getAvatarPantsID() {
    return this.pantsID;
  }

  public int getAvatarShoesID() {
    return this.shoesID;
  }

  public int getAvatarEyesID() {
    return this.eyesID;
  }

  public ColorReplaceRules getRules() {
    return this.rules;
  }
}
