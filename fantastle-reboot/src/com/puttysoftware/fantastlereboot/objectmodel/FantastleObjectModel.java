package com.puttysoftware.fantastlereboot.objectmodel;

import java.io.IOException;

import com.puttysoftware.diane.loaders.ColorShader;
import com.puttysoftware.diane.objectmodel.ObjectModel;
import com.puttysoftware.fantastlereboot.assets.ObjectImageIndex;
import com.puttysoftware.xio.XDataReader;
import com.puttysoftware.xio.XDataWriter;

public interface FantastleObjectModel
    extends ObjectModel, RandomGenerationRule {
  void setGameLook(String cacheName, ObjectImageIndex image);

  void setGameLook(String cacheName, ObjectImageIndex image,
      ColorShader shader);

  void setEditorLook(String cacheName, ObjectImageIndex image);

  void setEditorLook(String cacheName, ObjectImageIndex image,
      ColorShader shader);

  void setBattleLook(String cacheName, ObjectImageIndex image);

  void setBattleLook(String cacheName, ObjectImageIndex image,
      ColorShader shader);

  FantastleObjectModel getSavedObject();

  boolean hasSavedObject();

  void setSavedObject(FantastleObjectModel inNewSavedObject);

  String getName();

  int getLayer();

  void writeObject(XDataWriter writer) throws IOException;

  FantastleObjectModel readObject(XDataReader reader, int uid)
      throws IOException;
}