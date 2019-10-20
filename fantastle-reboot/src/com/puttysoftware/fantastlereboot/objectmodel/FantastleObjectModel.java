package com.puttysoftware.fantastlereboot.objectmodel;

import java.io.IOException;

import com.puttysoftware.diane.objectmodel.ObjectModel;
import com.puttysoftware.fantastlereboot.utilities.RandomGenerationRule;
import com.puttysoftware.xio.XDataReader;
import com.puttysoftware.xio.XDataWriter;

public interface FantastleObjectModel
        extends ObjectModel, RandomGenerationRule {
    FantastleObjectModel getSavedObject();

    boolean hasSavedObject();

    void setSavedObject(FantastleObjectModel inNewSavedObject);

    int getLayer();

    void writeObject(final XDataWriter writer) throws IOException;

    FantastleObjectModel readObject(final XDataReader reader, final int uid)
            throws IOException;
}