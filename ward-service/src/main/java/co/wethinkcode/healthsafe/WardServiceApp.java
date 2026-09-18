package co.wethinkcode.healthsafe;

import java.util.List;
import java.util.Optional;

import io.javalin.Javalin;
import io.javalin.http.NotFoundResponse;

public class WardServiceApp {

    public static void main(String[] args) {
        Javalin app = Javalin.create().start(7031);
        WardClient wardClient = new WardClient();

        new StaffingEventSubscriber().start();
        
        app.get("/health", ctx -> ctx.result("OK"));

        app.get("/wards", ctx -> {
            List<Ward> wards = wardClient.fetchWards();
            ctx.json(wards);
        });

        app.get("/wards/{id}", ctx -> {
            String id = ctx.pathParam("id").toUpperCase();
            List<Ward> wards = wardClient.fetchWards();

            Optional<Ward> match = wards.stream()
                    .filter(w -> w.wardId.equalsIgnoreCase(id))
                    .findFirst();

            if (match.isEmpty()) {
                throw new NotFoundResponse("No ward found with id " + id);
            }
            ctx.json(match.get());
        });
    }
}

