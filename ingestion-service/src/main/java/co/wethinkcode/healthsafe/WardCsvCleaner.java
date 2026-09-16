package co.wethinkcode.healthsafe;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public class WardCsvCleaner {
    
    public List<Ward> clean(Path csvPath) throws IOException {
        List<String> lines = Files.readAllLines(csvPath);
        Map<String, Ward> byId = new LinkedHashMap<>();


        for (int i = 1; i < lines.size(); i++){
            String line = lines.get(i);
            if (line.isBlank()) continue;

            String[] fields = line.split(",", -1);
            if (fields.length < 4) continue;

            String rawId = fields[0];
            String rawWing = fields[1];
            String rawDept = fields[2];
            String rawBeds = fields[3];

            String id = normalizeText(rawId).toUpperCase();
            String wing = titleCase(normalizeText(rawWing));
            String department = titleCase(normalizeText(rawDept));

            String note = null;
            Integer beds = parseBeds(rawBeds);
            if (beds == null && !isPlaceholder(rawBeds)) {
                note = "bedsAvailable was non-numeric ('" + rawBeds.trim() + "') — flagged for follow-up";
            } else if (beds == null) {
                note = "bedsAvailable was missing/placeholder ('" + rawBeds.trim() + "') — flagged for follow-up";
            }

             if (byId.containsKey(id)) {
                Ward existing = byId.get(id);
                String dupNote = "duplicate entry also seen (wing='" + wing + "', dept='" + department
                        + "', beds='" + rawBeds.trim() + "')";
                String combinedNote = existing.notes == null ? dupNote : existing.notes + "; " + dupNote;
                byId.put(id, new Ward(existing.wardId, existing.wing, existing.department,
                        existing.bedsAvailable, combinedNote));
                continue;
            }
            byId.put(id, new Ward(id, wing, department, beds, note));
        }
        return new ArrayList<>(byId.values());
    }

    private String normalizeText(String s) {
        return s.trim().replaceAll("\\s+", " ");
    }

    private String titleCase(String s) {
        if (s.isEmpty()) return s;
        String[] words = s.toLowerCase().split(" ");
        StringBuilder sb = new StringBuilder();
        for (String w : words) {
            if (w.isEmpty()) continue;
            sb.append(Character.toUpperCase(w.charAt(0))).append(w.substring(1)).append(" ");
        }
        return sb.toString().trim();
    }

    private boolean isPlaceholder(String raw) {
        String v = raw.trim().toLowerCase();
        return v.isEmpty() || v.equals("n/a") || v.equals("tbd") || v.equals("unknown") || v.equals("-") || v.equals("nan");
    }

    private Integer parseBeds(String raw) {
        String v = raw.trim();
        if (isPlaceholder(v)) return null;
        try {
            int n = Integer.parseInt(v);
            if (n < 0) return null;
            return n;
        } catch (NumberFormatException e) {
            return null; 
        }
    }
}