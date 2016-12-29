package game.game.player.statistics;

import de.uniba.wiai.lspi.chord.data.ID;
import game.game.player.map.Field;

/**
 * Created by FBeck on 28.12.2016.
 */
public class Shoot {
    private ID shooter;
    private ID target;
    private boolean hit;
    private int transactionNumber;
    private Field field;

    public Shoot(ID shooter, ID target, boolean hit, int transactionNumber, Field field){
        this.shooter = shooter;
        this.target = target;
        this.hit = hit;
        this.transactionNumber = transactionNumber;
        this.field = field;
    }

    public Shoot(ID shooter, ID target, boolean hit, int transactionNumber){
        this.shooter = shooter;
        this.target = target;
        this.hit = hit;
        this.transactionNumber = transactionNumber;
    }

    public void setShooter(ID shooter) {
        this.shooter = shooter;
    }

    public ID getShooter() {
        return shooter;
    }

    public int getTransactionNumber() {
        return transactionNumber;
    }

    public Field getField() {
        return field;
    }

    public ID getTarget() {
        return target;
    }

    public boolean isHit() {
        return hit;
    }
}
