package co.wethinkcode.healthsafe;

import io.javalin.Javalin;
import java.util.Optional;
import io.javalin.http.NotFoundResponse;

public class StaffingServiceApp {

    public static void main(String[] args) {
        Javalin app = Javalin.create().start(7033);

        WardServiceClient wardServiceClient = new WardServiceClient();
        AlertLevelServiceClient alertLevelServiceClient = new AlertLevelServiceClient();
        StaffingEventPublisher eventPublisher = new StaffingEventPublisher();

        app.get("/health", ctx -> ctx.result("OK"));

        app.get("/schedule/{wardId}", ctx -> { 
            String wardId = ctx.pathParam("wardId");

            Optional<Ward> ward = wardServiceClient.fetchWard(wardId);
            if(ward.isEmpty()){
                throw new NotFoundResponse("No ward found with id " + wardId);
            }

            int alertLevel = alertLevelServiceClient.fetchAlertLevel();
            int doctorsOnCall = 1 + (alertLevel / 2);

            Schedule schedule = new Schedule(ward.get(), alertLevel, doctorsOnCall);
            eventPublisher.publish(schedule);

            ctx.json(schedule);
        });
    }
}

