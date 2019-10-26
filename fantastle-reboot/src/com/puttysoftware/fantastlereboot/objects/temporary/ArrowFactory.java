package com.puttysoftware.fantastlereboot.objects.temporary;

import com.puttysoftware.diane.utilties.Directions;
import com.puttysoftware.fantastlereboot.objectmodel.FantastleObjectModel;

public class ArrowFactory {
  private ArrowFactory() {
    // Do nothing
  }

  public static FantastleObjectModel createArrow(final ArrowType type,
      final int dir) {
    switch (type) {
    case BEAUTY:
      switch (dir) {
      case Directions.EAST:
        return new BeautyArrowEast();
      case Directions.NORTHWEST:
        return new BeautyArrowNorthwest();
      case Directions.NORTH:
        return new BeautyArrowNorth();
      case Directions.NORTHEAST:
        return new BeautyArrowNortheast();
      case Directions.SOUTHWEST:
        return new BeautyArrowSouthwest();
      case Directions.SOUTH:
        return new BeautyArrowSouth();
      case Directions.SOUTHEAST:
        return new BeautyArrowSoutheast();
      case Directions.WEST:
        return new BeautyArrowWest();
      default:
        return null;
      }
    case CHARGE:
      switch (dir) {
      case Directions.EAST:
        return new ChargeArrowEast();
      case Directions.NORTHWEST:
        return new ChargeArrowNorthwest();
      case Directions.NORTH:
        return new ChargeArrowNorth();
      case Directions.NORTHEAST:
        return new ChargeArrowNortheast();
      case Directions.SOUTHWEST:
        return new ChargeArrowSouthwest();
      case Directions.SOUTH:
        return new ChargeArrowSouth();
      case Directions.SOUTHEAST:
        return new ChargeArrowSoutheast();
      case Directions.WEST:
        return new ChargeArrowWest();
      default:
        return null;
      }
    case FREEZE:
      switch (dir) {
      case Directions.EAST:
        return new FreezeArrowEast();
      case Directions.NORTHWEST:
        return new FreezeArrowNorthwest();
      case Directions.NORTH:
        return new FreezeArrowNorth();
      case Directions.NORTHEAST:
        return new FreezeArrowNortheast();
      case Directions.SOUTHWEST:
        return new FreezeArrowSouthwest();
      case Directions.SOUTH:
        return new FreezeArrowSouth();
      case Directions.SOUTHEAST:
        return new FreezeArrowSoutheast();
      case Directions.WEST:
        return new FreezeArrowWest();
      default:
        return null;
      }
    case LIQUID:
      switch (dir) {
      case Directions.EAST:
        return new LiquidArrowEast();
      case Directions.NORTHWEST:
        return new LiquidArrowNorthwest();
      case Directions.NORTH:
        return new LiquidArrowNorth();
      case Directions.NORTHEAST:
        return new LiquidArrowNortheast();
      case Directions.SOUTHWEST:
        return new LiquidArrowSouthwest();
      case Directions.SOUTH:
        return new LiquidArrowSouth();
      case Directions.SOUTHEAST:
        return new LiquidArrowSoutheast();
      case Directions.WEST:
        return new LiquidArrowWest();
      default:
        return null;
      }
    case MAGNET:
      switch (dir) {
      case Directions.EAST:
        return new MagnetArrowEast();
      case Directions.NORTHWEST:
        return new MagnetArrowNorthwest();
      case Directions.NORTH:
        return new MagnetArrowNorth();
      case Directions.NORTHEAST:
        return new MagnetArrowNortheast();
      case Directions.SOUTHWEST:
        return new MagnetArrowSouthwest();
      case Directions.SOUTH:
        return new MagnetArrowSouth();
      case Directions.SOUTHEAST:
        return new MagnetArrowSoutheast();
      case Directions.WEST:
        return new MagnetArrowWest();
      default:
        return null;
      }
    case PLASMA:
      switch (dir) {
      case Directions.EAST:
        return new PlasmaArrowEast();
      case Directions.NORTHWEST:
        return new PlasmaArrowNorthwest();
      case Directions.NORTH:
        return new PlasmaArrowNorth();
      case Directions.NORTHEAST:
        return new PlasmaArrowNortheast();
      case Directions.SOUTHWEST:
        return new PlasmaArrowSouthwest();
      case Directions.SOUTH:
        return new PlasmaArrowSouth();
      case Directions.SOUTHEAST:
        return new PlasmaArrowSoutheast();
      case Directions.WEST:
        return new PlasmaArrowWest();
      default:
        return null;
      }
    case POISON:
      switch (dir) {
      case Directions.EAST:
        return new PoisonArrowEast();
      case Directions.NORTHWEST:
        return new PoisonArrowNorthwest();
      case Directions.NORTH:
        return new PoisonArrowNorth();
      case Directions.NORTHEAST:
        return new PoisonArrowNortheast();
      case Directions.SOUTHWEST:
        return new PoisonArrowSouthwest();
      case Directions.SOUTH:
        return new PoisonArrowSouth();
      case Directions.SOUTHEAST:
        return new PoisonArrowSoutheast();
      case Directions.WEST:
        return new PoisonArrowWest();
      default:
        return null;
      }
    case SACRED:
      switch (dir) {
      case Directions.EAST:
        return new SacredArrowEast();
      case Directions.NORTHWEST:
        return new SacredArrowNorthwest();
      case Directions.NORTH:
        return new SacredArrowNorth();
      case Directions.NORTHEAST:
        return new SacredArrowNortheast();
      case Directions.SOUTHWEST:
        return new SacredArrowSouthwest();
      case Directions.SOUTH:
        return new SacredArrowSouth();
      case Directions.SOUTHEAST:
        return new SacredArrowSoutheast();
      case Directions.WEST:
        return new SacredArrowWest();
      default:
        return null;
      }
    case SCORCH:
      switch (dir) {
      case Directions.EAST:
        return new ScorchArrowEast();
      case Directions.NORTHWEST:
        return new ScorchArrowNorthwest();
      case Directions.NORTH:
        return new ScorchArrowNorth();
      case Directions.NORTHEAST:
        return new ScorchArrowNortheast();
      case Directions.SOUTHWEST:
        return new ScorchArrowSouthwest();
      case Directions.SOUTH:
        return new ScorchArrowSouth();
      case Directions.SOUTHEAST:
        return new ScorchArrowSoutheast();
      case Directions.WEST:
        return new ScorchArrowWest();
      default:
        return null;
      }
    case SHADOW:
      switch (dir) {
      case Directions.EAST:
        return new ShadowArrowEast();
      case Directions.NORTHWEST:
        return new ShadowArrowNorthwest();
      case Directions.NORTH:
        return new ShadowArrowNorth();
      case Directions.NORTHEAST:
        return new ShadowArrowNortheast();
      case Directions.SOUTHWEST:
        return new ShadowArrowSouthwest();
      case Directions.SOUTH:
        return new ShadowArrowSouth();
      case Directions.SOUTHEAST:
        return new ShadowArrowSoutheast();
      case Directions.WEST:
        return new ShadowArrowWest();
      default:
        return null;
      }
    case VORTEX:
      switch (dir) {
      case Directions.EAST:
        return new VortexArrowEast();
      case Directions.NORTHWEST:
        return new VortexArrowNorthwest();
      case Directions.NORTH:
        return new VortexArrowNorth();
      case Directions.NORTHEAST:
        return new VortexArrowNortheast();
      case Directions.SOUTHWEST:
        return new VortexArrowSouthwest();
      case Directions.SOUTH:
        return new VortexArrowSouth();
      case Directions.SOUTHEAST:
        return new VortexArrowSoutheast();
      case Directions.WEST:
        return new VortexArrowWest();
      default:
        return null;
      }
    case WOODEN:
      switch (dir) {
      case Directions.EAST:
        return new WoodenArrowEast();
      case Directions.NORTHWEST:
        return new WoodenArrowNorthwest();
      case Directions.NORTH:
        return new WoodenArrowNorth();
      case Directions.NORTHEAST:
        return new WoodenArrowNortheast();
      case Directions.SOUTHWEST:
        return new WoodenArrowSouthwest();
      case Directions.SOUTH:
        return new WoodenArrowSouth();
      case Directions.SOUTHEAST:
        return new WoodenArrowSoutheast();
      case Directions.WEST:
        return new WoodenArrowWest();
      default:
        return null;
      }
    default:
      break;
    }
    return null;
  }
}
