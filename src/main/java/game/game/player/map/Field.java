package game.game.player.map;

import de.uniba.wiai.lspi.chord.data.ID;

/**
 * Created by beckf on 17.12.2016.
 */
public class Field {
    private int fieldID;
    private ID highBoarder;
    private ID lowBoarder;
    private ID shootAt;

    private FieldType fieldType;

    public Field(int fieldID, ID lowBoarder, ID highBoarder, ID shootAt) {
        this.fieldID = fieldID;
        this.highBoarder = highBoarder;
        this.lowBoarder = lowBoarder;
        this.shootAt = shootAt;
        this.fieldType = FieldType.UNKNOWN;
    }

    public int getFieldID() {
        return fieldID;
    }

    public ID getShootAt() {
        return shootAt;
    }

    public FieldType getFieldType() {
        return fieldType;
    }

    public boolean checkID(ID id) {
        if (id.isInInterval(lowBoarder, highBoarder.addPowerOfTwo(0))) {
            return true;
        }
        return false;
    }

    public boolean isUnknown(){
        return fieldType == FieldType.UNKNOWN;
    }

    public boolean isWater(){
        return fieldType == FieldType.WATER;
    }

    public boolean isShip(){
        return fieldType == FieldType.SHIP;
    }

    public void toWater(){
        fieldType = FieldType.WATER;
    }

    public void toShip(){
        fieldType = FieldType.SHIP;
    }

    @Override
    public String toString() {
        return "Field{" +
                "fieldID=" + fieldID +
                ", highBoarder=" + highBoarder +
                ", lowBoarder=" + lowBoarder +
                ", shootAt=" + shootAt +
                ", fieldType=" + fieldType +
                '}';
    }


        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Field)) return false;

            Field field = (Field) o;

            if (getFieldID() != field.getFieldID()) return false;
            if (highBoarder != null ? !highBoarder.equals(field.highBoarder) : field.highBoarder != null) return false;
            if (lowBoarder != null ? !lowBoarder.equals(field.lowBoarder) : field.lowBoarder != null) return false;
            if (getShootAt() != null ? !getShootAt().equals(field.getShootAt()) : field.getShootAt() != null)
                return false;
            return getFieldType() == field.getFieldType();

        }

        @Override
        public int hashCode() {
            int result = getFieldID();
            result = 31 * result + (highBoarder != null ? highBoarder.hashCode() : 0);
            result = 31 * result + (lowBoarder != null ? lowBoarder.hashCode() : 0);
            result = 31 * result + (getShootAt() != null ? getShootAt().hashCode() : 0);
            result = 31 * result + (getFieldType() != null ? getFieldType().hashCode() : 0);
            return result;
        }
}
