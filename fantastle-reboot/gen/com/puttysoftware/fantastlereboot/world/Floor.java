// automatically generated by the FlatBuffers compiler, do not modify

package com.puttysoftware.fantastlereboot.world;

import java.nio.*;
import java.lang.*;
import java.util.*;
import com.google.flatbuffers.*;

@SuppressWarnings("unused")
public final class Floor extends Table {
  public static Floor getRootAsFloor(ByteBuffer _bb) { return getRootAsFloor(_bb, new Floor()); }
  public static Floor getRootAsFloor(ByteBuffer _bb, Floor obj) { _bb.order(ByteOrder.LITTLE_ENDIAN); return (obj.__assign(_bb.getInt(_bb.position()) + _bb.position(), _bb)); }
  public void __init(int _i, ByteBuffer _bb) { bb_pos = _i; bb = _bb; vtable_start = bb_pos - bb.getInt(bb_pos); vtable_size = bb.getShort(vtable_start); }
  public Floor __assign(int _i, ByteBuffer _bb) { __init(_i, _bb); return this; }

  public Column data(int j) { return data(new Column(), j); }
  public Column data(Column obj, int j) { int o = __offset(4); return o != 0 ? obj.__assign(__indirect(__vector(o) + j * 4), bb) : null; }
  public int dataLength() { int o = __offset(4); return o != 0 ? __vector_len(o) : 0; }
  public boolean wraparound() { int o = __offset(6); return o != 0 ? 0!=bb.get(o + bb_pos) : false; }

  public static int createFloor(FlatBufferBuilder builder,
      int dataOffset,
      boolean wraparound) {
    builder.startObject(2);
    Floor.addData(builder, dataOffset);
    Floor.addWraparound(builder, wraparound);
    return Floor.endFloor(builder);
  }

  public static void startFloor(FlatBufferBuilder builder) { builder.startObject(2); }
  public static void addData(FlatBufferBuilder builder, int dataOffset) { builder.addOffset(0, dataOffset, 0); }
  public static int createDataVector(FlatBufferBuilder builder, int[] data) { builder.startVector(4, data.length, 4); for (int i = data.length - 1; i >= 0; i--) builder.addOffset(data[i]); return builder.endVector(); }
  public static void startDataVector(FlatBufferBuilder builder, int numElems) { builder.startVector(4, numElems, 4); }
  public static void addWraparound(FlatBufferBuilder builder, boolean wraparound) { builder.addBoolean(1, wraparound, false); }
  public static int endFloor(FlatBufferBuilder builder) {
    int o = builder.endObject();
    return o;
  }
}

