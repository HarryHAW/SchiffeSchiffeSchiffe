package game.agent.scout.implementations;

import de.uniba.wiai.lspi.chord.com.Broadcast;
import de.uniba.wiai.lspi.chord.data.ID;
import de.uniba.wiai.lspi.util.logging.Logger;
import game.agent.Agent;
import game.agent.scout.PlayerScout;
import game.game.Game;


import java.util.*;

import game.game.history.History;
import game.game.player.Player;
import sun.rmi.runtime.Log;

/**
 * Created by admin on 22.12.2016.
 */
public class StrategicPlayerScout implements PlayerScout {
    private Random random;
    private Game game;
    private final static Integer criticalAmountOfUnknownFields = 10;
    private final static Integer criticalAmountOfShotsFired = 8;
    private final static Double criticalRateOfShotsFiredAtUs = 0.75;

    private static Logger LOG = Logger.getLogger(StrategicPlayerScout.class);

    public StrategicPlayerScout(Random random, Game game) {
        this.random = random;
        this.game = game;
    }

    //Choose Player as target
    @Override
    public ID getTarget() {
        ID playerID = game.getSelf();
        ID playerIDFewestShiphits = game.getSelf();
        List<ID> tmpPlayerIDList = game.getPlayers();
        List<Player> tmpPlayerList = new ArrayList<Player>();
        List<Player> primaryTargets = new ArrayList<Player>();
        Integer tmpAmountShips = Game.SHIPS;
        Map<Player, Double> playerWhotargetedUsAtHighRate = new HashMap<Player, Double>();

        Player tmpPlayer;
        //iterate over all Players
        for(int i =0; i < tmpPlayerList.size(); i++){
            LOG.info("Iterating in PlayerScout " + i);
            tmpPlayer = game.getPlayer(tmpPlayerIDList.get(i));
            if(tmpPlayer != game.getPlayer(game.getSelf())){
                tmpPlayerList.add(tmpPlayer);
                // Player with all Ships hit
                if(tmpPlayer.getShip() == Game.SHIPS){
                    return tmpPlayer.getPlayer();
                }
                // All Players with all ships hit but one except yourself
                if((tmpPlayer.getShip() == Game.SHIPS-1) && tmpPlayer != game.getPlayer((game.getSelf()))) {
                    primaryTargets.add(tmpPlayer);
                }
                // get Player with fewest losses
                if(tmpPlayer.getShip() <= tmpAmountShips){
                    tmpAmountShips = tmpPlayer.getShip();
                    playerIDFewestShiphits = tmpPlayer.getPlayer();
                }
                //get target history from Player
                List<Broadcast> tmpBroadcastHistory = game.getHistoryForPlayer(tmpPlayer.getPlayer());
                Integer tmpTargetCounter = 0;
                for (int j = 0; i < tmpBroadcastHistory.size() ; i++){
                    if(tmpBroadcastHistory.get(j).getTarget() == game.getSelf()){
                        tmpTargetCounter++;
                    }
                }
                if((tmpBroadcastHistory.size() > criticalAmountOfShotsFired) && (criticalRateOfShotsFiredAtUs <= (Double.valueOf(tmpTargetCounter) / tmpBroadcastHistory.size()))){
                    playerWhotargetedUsAtHighRate.put(tmpPlayer,(Double.valueOf(tmpTargetCounter) / tmpBroadcastHistory.size()));
                }

            }
        }
        //If only one primaryTarget found, choose as target
        if(primaryTargets.size() == 1){
            playerID = primaryTargets.get(0).getPlayer();
            return playerID;
        }
        //choose primaryTarget with the smallest amount of unknown fields
        else if(primaryTargets.size() > 0) {
            int tmpUnknown = Game.FIELDS;
            for (int i = 1; i < primaryTargets.size(); i++) {
                if (primaryTargets.get(i).getUnknown() <= tmpUnknown) {
                    playerID = primaryTargets.get(i).getPlayer();
                }
            }
        }
        // No primaryTarget Found ==> Secondary strategy

        // If there is a player who targets always us, choose him. If our status is better.
        else if(playerWhotargetedUsAtHighRate.size() >= 0){
            // determine player who always targets us at most
            tmpPlayer = game.getPlayer(game.getSelf());
            double tmpRate = 0;
            for(Player p : playerWhotargetedUsAtHighRate.keySet()){
                if(tmpRate < playerWhotargetedUsAtHighRate.get(p) && isOurStatusBetterThan(p)){
                    tmpRate = playerWhotargetedUsAtHighRate.get(p);
                    tmpPlayer = p;
                }
            }
            if(tmpPlayer != game.getPlayer(game.getSelf())){
                playerID = tmpPlayer.getPlayer();
            }
        }
        //Choose Player with the fewest ships as target
        else{
            playerID = playerIDFewestShiphits;
        }
        // nothing found? ==> random
        if (playerID.compareTo(game.getSelf()) == 0){
            if(playerIDFewestShiphits.compareTo(game.getSelf()) != 0){
                playerID = playerIDFewestShiphits;
            }
            else {
                while (playerID.compareTo(game.getSelf()) == 0) {
                    playerID = game.getPlayers().get(random.nextInt(game.getPlayers().size()));
                }
            }
        }
        return playerID;
    }
    //determine status from the amount of ships and the amount of unknown fields
    private boolean isOurStatusBetterThan(Player player){
        int shipDifference = player.getShip() - game.getOwnShips();
        int unknownFieldsDifference = player.getUnknown() - game.getPlayer(game.getSelf()).getUnknown();

        //if we have less damage and more or the same amount of unknown fields
        if(shipDifference > 0 && unknownFieldsDifference >= 0){
            return true;
        }
        //if we have less damage but less unknown fields ||  more damage and more unknown fields
        else if((shipDifference > 0 && unknownFieldsDifference < 0) || (shipDifference < 0 && unknownFieldsDifference > 0)){
            // Felder im Verhältnis zur positiven Schiffsdifferenz setzen.
            return (((shipDifference*criticalAmountOfUnknownFields) + unknownFieldsDifference) > 0);
        }
        // more damage, but less unknown fields
        else if(shipDifference < 0 && unknownFieldsDifference < 0){
            return false;
        }
        else {
            return false;
        }
    }
}