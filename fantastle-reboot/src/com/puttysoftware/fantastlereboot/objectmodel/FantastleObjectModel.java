package com.puttysoftware.fantastlereboot.objectmodel;

import com.puttysoftware.diane.objectmodel.ObjectModel;

public interface FantastleObjectModel extends ObjectModel {

    FantastleObject getSavedObject();

    boolean hasSavedObject();

    void setSavedObject(FantastleObject inNewSavedObject);
}