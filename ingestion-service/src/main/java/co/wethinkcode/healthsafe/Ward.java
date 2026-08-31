package co.wethinkcode.healthsafe;

public class Ward {
    public final String wardId;
    public final String wing;
    public final String department;
    public final Integer bedsAvailable;
    public final String notes;

    public Ward(String wardId, String wing, String department, Integer bedsAvailable, String notes){
        this.wardId = wardId;
        this.wing = wing;
        this.department = department;
        this.bedsAvailable = bedsAvailable;
        this.notes = notes;
    }
    
}
