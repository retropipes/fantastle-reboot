package com.puttysoftware.fantastlereboot.game;

import com.puttysoftware.diane.gui.CommonDialogs;
import com.puttysoftware.fantastlereboot.creatures.party.PartyManager;
import com.puttysoftware.fantastlereboot.creatures.party.PartyMember;

public final class InventoryViewer {
  private InventoryViewer() {
    // Do nothing
  }

  public static void showEquipmentDialog() {
    final String title = "Equipment";
    final PartyMember member = PartyManager.getParty().getLeader();
    if (member != null) {
      final String[] equipString = member.getItems()
          .generateEquipmentStringArray();
      CommonDialogs.showInputDialog("Equipment", title, equipString,
          equipString[0]);
    }
  }
}
