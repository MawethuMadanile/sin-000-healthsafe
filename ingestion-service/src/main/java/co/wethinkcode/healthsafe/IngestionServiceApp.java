package co.wethinkcode.healthsafe;

import io.javalin.Javalin;

import java.nio.file.Path;
import java.util.List;

public class IngestionServiceApp {

    public static void main(String[] args) {
        Javalin app = Javalin.create().start(7030);

        app.get("/health", ctx -> ctx.result("OK"));

        app.get("/wards", ctx -> {
            Path cvsPath = Path.of("src/main/resources/wards-outdated.cvs");
            List<Ward> wards = new WardCsvCleaner().clean(cvsPath);
            ctx.json(wards);
        });
    }
}
