package com.puttysoftware.fantastlereboot.objects.temporary;

import com.puttysoftware.fantastlereboot.assets.ObjectImageIndex;
import com.puttysoftware.fantastlereboot.objectmodel.FantastleObject;

public final class NoteObject extends FantastleObject {
  public NoteObject() {
    super(-4, "note", ObjectImageIndex.NOTE);
    this.setDestroyable(false);
  }
}
