package co.wethinkcode.healthsafe;

public class Schedule {
    public Ward ward;
    public int alertLevel;
    public int doctorsOnCall;

    public Schedule(){
    }

public Schedule(Ward ward, int alertLevel, int doctorsOnCall) {
    this.ward = ward;
    this.alertLevel = alertLevel;
    this.doctorsOnCall = doctorsOnCall;
    }
}