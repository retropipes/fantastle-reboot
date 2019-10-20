package com.puttysoftware.fantastlereboot.objectmodel;

import com.puttysoftware.diane.objectmodel.ObjectModel;

public interface FantastleObjectModel extends ObjectModel {
    FantastleObjectModel getSavedObject();

    boolean hasSavedObject();

    void setSavedObject(FantastleObjectModel inNewSavedObject);

    int getLayer();
}