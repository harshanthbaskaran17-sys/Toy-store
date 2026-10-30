package com.toystore.common;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class ImageStorageUtil {

    public static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5MB
    public static final List<String> ALLOWED_EXTENSIONS = Arrays.asList(".jpg", ".jpeg", ".png", ".webp");
    public static final List<String> ALLOWED_CONTENT_TYPES = Arrays.asList(
            "image/jpeg", "image/png", "image/webp"
    );

    /**
     * Validates and saves an uploaded image file into uploads/<component>/
     * 
     * @param component The component subfolder (e.g. "toys", "users", "suppliers")
     * @param entityId  The unique ID of the entity (e.g. "T123456")
     * @param file      The uploaded MultipartFile
     * @return The unique saved filename (ID + UUID + extension) or empty string if file is empty
     * @throws IOException If file writing fails
     * @throws IllegalArgumentException If file validation fails (type or size)
     */
    public static String saveImage(String component, String entityId, MultipartFile file) throws IOException, IllegalArgumentException {
        if (file == null || file.isEmpty()) {
            return "";
        }

        // Validate max size (5MB)
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new IllegalArgumentException("File size exceeds 5MB limit. Please upload a smaller image.");
        }

        // Validate extension
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || originalFilename.lastIndexOf(".") == -1) {
            throw new IllegalArgumentException("Invalid file: Must include a valid extension (.jpg, .png, or .webp).");
        }

        String extension = originalFilename.substring(originalFilename.lastIndexOf(".")).toLowerCase();
        if (!ALLOWED_EXTENSIONS.contains(extension)) {
            throw new IllegalArgumentException("Invalid file type '" + extension + "'. Allowed types are JPG, PNG, and WEBP.");
        }

        // Validate content type if provided
        String contentType = file.getContentType();
        if (contentType != null && !contentType.isEmpty() && !ALLOWED_CONTENT_TYPES.contains(contentType.toLowerCase())) {
            throw new IllegalArgumentException("Invalid file format. Allowed formats are JPG, PNG, and WEBP.");
        }

        // Target directory: uploads/<component>/
        Path uploadDirectory = Paths.get("uploads", component);
        if (!Files.exists(uploadDirectory)) {
            Files.createDirectories(uploadDirectory);
        }

        // Unique filename: ID + UUID + extension
        String cleanId = entityId != null ? entityId.replaceAll("[^a-zA-Z0-9]", "") : "IMG";
        String uuidPart = UUID.randomUUID().toString().replace("-", "").substring(0, 12);
        String uniqueFilename = cleanId + "_" + uuidPart + extension;

        Path targetPath = uploadDirectory.resolve(uniqueFilename);
        Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);

        return uniqueFilename;
    }

    /**
     * Deletes an image from uploads/<component>/
     * 
     * @param component The component subfolder (e.g. "toys", "users", "suppliers")
     * @param filename  The filename to delete
     */
    public static void deleteImage(String component, String filename) {
        if (filename == null || filename.trim().isEmpty()) {
            return;
        }

        try {
            Path targetPath = Paths.get("uploads", component, filename);
            Files.deleteIfExists(targetPath);
        } catch (IOException e) {
            System.err.println("Warning: Could not delete image " + filename + ": " + e.getMessage());
        }
    }

    /**
     * Returns the placeholder image path for a given component
     */
    public static String getPlaceholderPath(String component) {
        switch (component) {
            case "toys":
                return "/images/placeholder-toy.svg";
            case "users":
                return "/images/placeholder-user.svg";
            case "suppliers":
                return "/images/placeholder-supplier.svg";
            default:
                return "/images/placeholder-toy.svg";
        }
    }
}
