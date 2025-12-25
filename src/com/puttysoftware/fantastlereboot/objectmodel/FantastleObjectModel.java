package com.puttysoftware.fantastlereboot.objectmodel;

import java.io.IOException;

import org.retropipes.diane.asset.image.ColorShader;
import org.retropipes.diane.fileio.DataIOReader;
import org.retropipes.diane.fileio.DataIOWriter;

import com.puttysoftware.fantastlereboot.assets.ObjectImageIndex;

public interface FantastleObjectModel extends ObjectModel, RandomGenerationRule {
    void setGameLook(String cacheName, ObjectImageIndex image);

    void setGameLook(String cacheName, ObjectImageIndex image, ColorShader shader);

    void setEditorLook(String cacheName, ObjectImageIndex image);

    void setEditorLook(String cacheName, ObjectImageIndex image, ColorShader shader);

    void setBattleLook(String cacheName, ObjectImageIndex image);

    void setBattleLook(String cacheName, ObjectImageIndex image, ColorShader shader);

    FantastleObjectModel getSavedObject();

    boolean hasSavedObject();

    void setSavedObject(FantastleObjectModel inNewSavedObject);

    String getName();

    int getLayer();

    void writeObject(DataIOWriter writer) throws IOException;

    FantastleObjectModel readObject(DataIOReader reader, int uid) throws IOException;
}