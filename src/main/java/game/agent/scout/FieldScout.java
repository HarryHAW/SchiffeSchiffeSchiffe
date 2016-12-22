package game.agent.scout;

import de.uniba.wiai.lspi.chord.data.ID;
import game.game.player.map.Field;

/**
 * Created by beckf on 22.12.2016.
 */
public interface FieldScout {
    Field getTarget(ID playerID);
}
