package game.game.player.statistics;

import de.uniba.wiai.lspi.chord.data.ID;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by FBeck on 28.12.2016.
 */
public class Statistics {

    Map<ID, List<Shoot>> gotShootFrom;
    List<Shoot> gotShootFromOrder;

    public Statistics(){
        this.gotShootFrom = new HashMap<>();
        this.gotShootFromOrder = new ArrayList<>();
    }

    public void addShoot(Shoot shoot){
        if(shoot.getShooter() != null) {
            if (!gotShootFrom.containsKey(shoot.getShooter())) {
                gotShootFrom.put(shoot.getShooter(), new ArrayList<>());
            }
            gotShootFrom.get(shoot.getShooter()).add(shoot);
        }
        gotShootFromOrder.add(shoot);
    }

    public int howOftenWasIShootFromID(ID id){
        return gotShootFrom.get(id).size();
    }

    public Shoot lastShootOnMe(){
        return gotShootFromOrder.get(gotShootFromOrder.size() - 1);
    }

    public List<Shoot> shootsFromID(ID id){
        return gotShootFrom.get(id);
    }

    public void updateFirstShootInGame(Shoot shoot){
        if(!gotShootFromOrder.isEmpty()){
            Shoot firstShoot = gotShootFromOrder.get(0);
            if(firstShoot.getTarget().compareTo(shoot.getTarget()) == 0){
                if(firstShoot.getShooter() != null){
                    gotShootFrom.get(firstShoot.getShooter()).remove(0);
                }
                firstShoot.setShooter(shoot.getShooter());
                List<Shoot> shoots = new ArrayList<>();
                shoots.add(firstShoot);
                if(gotShootFrom.containsKey(firstShoot.getShooter())) {
                    shoots.addAll(gotShootFrom.get(firstShoot.getShooter()));
                }
                gotShootFrom.put(firstShoot.getShooter(), shoots);
            }
        }
    }

    public Shoot getDestroyingShoot(){
        Shoot finalShoot = null;
        int i = gotShootFromOrder.size() - 1;
        while(finalShoot == null && i >= 0){
            Shoot shoot = gotShootFromOrder.get(i);
            if(shoot.isHit()){
                finalShoot = shoot;
            }
        }
        return finalShoot;
    }
}
