package co.wethinkcode.healthsafe;

public class EquipmentFailure {
    public String wardId;
    public String description;

    public EquipmentFailure(){
    }

    public EquipmentFailure(String wardId, String description) {
        this.wardId = wardId;
        this.description = description;
    }
}