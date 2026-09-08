package co.wethinkcode.healthsafe;

import java.nio.file.Path;
import java.util.List;

import io.javalin.Javalin;

public class IngestionServiceApp {

    public static void main(String[] args) {
        Javalin app = Javalin.create().start(7030);

        app.get("/health", ctx -> ctx.result("OK"));

        app.get("/wards", ctx -> {
            Path cvsPath = Path.of("src/main/resources/wards-outdated.csv");
            List<Ward> wards = new WardCsvCleaner().clean(cvsPath);
            ctx.json(wards);
        });
    }
}
