package co.wethinkcode.healthsafe;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class WardCsvCleaner {
    
    public List<Ward> clean(Path csvPath) throws IOException {
        List<String> lines = Files.readAllLines(csvPath);
        List<Ward> wards = new ArrayList<>();


        for (int i = 1; i < lines.size(); i++){
            String line = lines.get(i);
            if (line.isBlank()) continue;

            String[] fields = line.split(",", -1);
        }
        return wards;
    }
}
