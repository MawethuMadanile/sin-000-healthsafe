package co.wethinkcode.healthsafe;

import io.javalin.Javalin;

public class EquipmentAlertServiceApp {

    public static void main(String[] args) {
        Javalin app = Javalin.create().start(7034);

        new EquipmentAlertConsumer().start();
        
        app.get("/health", ctx -> ctx.result("OK"));

    }
}

