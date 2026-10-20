package com.toystore.component4_admin.repository;

import com.toystore.common.FileHandler;
import com.toystore.component4_admin.model.ActivityLog;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

// COMPONENT 04: Admin Management | Owner: Placeholder
@Repository
public class ActivityLogRepository {
    private final FileHandler fileHandler;

    public ActivityLogRepository() {
        this.fileHandler = new FileHandler("admin_logs.txt");
    }

    public synchronized void save(ActivityLog log) {
        fileHandler.appendLine(log.toFileString());
    }

    public synchronized List<ActivityLog> findAll() {
        List<String> lines = fileHandler.readAllLines();
        List<ActivityLog> logs = new ArrayList<>();
        for (String line : lines) {
            String[] parts = line.split("\\|");
            if (parts.length >= 3) {
                logs.add(new ActivityLog(parts[0], parts[1], parts[2]));
            }
        }
        return logs;
    }
}
