package co.wethinkcode.healthsafe;

import java.util.concurrent.atomic.AtomicInteger;

import io.javalin.Javalin;
import io.javalin.http.BadRequestResponse;

public class AlertLevelServiceApp {

    public static void main(String[] args) {
        Javalin app = Javalin.create().start(7032);

        AtomicInteger alertLevel = new AtomicInteger(0);

        app.get("/health", ctx -> ctx.result("OK"));

        app.get("/alert-Level", ctx -> {
            ctx.json(new AlertLevel(alertLevel.get()));
        });

        app.put("/alert-level", ctx -> {
            AlertLevel body = ctx.bodyAsClass(AlertLevel.class);
            if (body.level < 0 || body.level > 8) {
                throw new BadRequestResponse("level must be between ) and 8");
            }
            alertLevel.set(body.level);
            ctx.json(new AlertLevel(alertLevel.get()));
        });

    }
}
