package com.toystore.common;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class FileHandler {

    private static final String DATA_DIR = "data";
    private final Path filePath;
    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    public FileHandler(String fileName) {
        this.filePath = Paths.get(DATA_DIR, fileName);
        initFile();
    }

    private void initFile() {
        try {
            Path dirPath = Paths.get(DATA_DIR);
            if (!Files.exists(dirPath)) {
                Files.createDirectories(dirPath);
            }
            if (!Files.exists(filePath)) {
                Files.createFile(filePath);
            }
        } catch (IOException e) {
            System.err.println("Failed to initialize file: " + filePath + " - " + e.getMessage());
        }
    }

    public List<String> readAllLines() {
        lock.readLock().lock();
        try {
            return Files.readAllLines(filePath);
        } catch (IOException e) {
            System.err.println("Error reading file: " + filePath + " - " + e.getMessage());
            return new ArrayList<>();
        } finally {
            lock.readLock().unlock();
        }
    }

    public void appendLine(String line) {
        lock.writeLock().lock();
        try (BufferedWriter writer = Files.newBufferedWriter(filePath, StandardOpenOption.APPEND)) {
            writer.write(line);
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Error appending to file: " + filePath + " - " + e.getMessage());
        } finally {
            lock.writeLock().unlock();
        }
    }

    public void writeAllLines(List<String> lines) {
        lock.writeLock().lock();
        try (BufferedWriter writer = Files.newBufferedWriter(filePath, StandardOpenOption.TRUNCATE_EXISTING)) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error writing to file: " + filePath + " - " + e.getMessage());
        } finally {
            lock.writeLock().unlock();
        }
    }
}
