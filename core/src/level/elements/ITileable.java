package level.elements;

import basiselements.DungeonElement;
import java.util.Random;
import level.elements.tile.Tile;
import level.tools.Coordinate;
import level.tools.LevelElement;
import tools.Point;

public interface ITileable extends IPathable {
    Random RANDOM = new Random();
    /**
     * @return The layout of the level
     */
    Tile[][] getLayout();

    /**
     * Get a tile on the global position.
     *
     * @param globalPoint Position form where to get the tile.
     * @return The tile on that point. null if there is no Tile or the Coordinate is out of bound
     */
    default Tile getTileAt(Coordinate globalPoint) {
        try {
            return getLayout()[globalPoint.y][globalPoint.x];
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    /**
     * @return a random Tile in the Level
     */
    default Tile getRandomTile() {
        return getLayout()[RANDOM.nextInt(getLayout().length)][
                RANDOM.nextInt(getLayout()[0].length)];
    }

    /**
     * Get the end tile.
     *
     * @return The end tile.
     */
    Tile getEndTile();

    /**
     * Get the start tile.
     *
     * @return The start tile.
     */
    Tile getStartTile();
    /**
     * Checks if the passed entity is on the tile to the next level.
     *
     * @param entity entity to check for.
     * @return if the passed entity is on the tile to the next level
     */
    default boolean isOnEndTile(DungeonElement entity) {
        if (getEndTile() == null) {
            return false;
        }
        return entity.getPosition().toCoordinate().equals(getEndTile().getCoordinate());
    }

    /**
     * Returns the tile the given entity is standing on.
     *
     * @param entity entity to check for.
     * @return tile at the coordinate of the entity
     */
    default Tile getTileAtEntity(DungeonElement entity) {

        return getTileAt(entity.getPosition().toCoordinate());
    }

    /**
     * Get a random Tile
     *
     * @param elementType Type of the Tile
     * @return A random Tile of the given Type
     */
    default Tile getRandomTile(LevelElement elementType) {
        Tile randomTile = getRandomTile();
        if (randomTile.getLevelElement() == elementType) {
            return randomTile;
        } else {
            return getRandomTile(elementType);
        }
    }
    /**
     * Get the position of a random Tile as Point
     *
     * @return Position of the Tile as Point
     */
    default Point getRandomTilePoint() {
        return getRandomTile().getCoordinate().toPoint();
    }

    /**
     * Get the position of a random Tile as Point
     *
     * @param elementTyp Type of the Tile
     * @return Position of the Tile as Point
     */
    default Point getRandomTilePoint(LevelElement elementTyp) {
        return getRandomTile(elementTyp).getCoordinate().toPoint();
    }
}
