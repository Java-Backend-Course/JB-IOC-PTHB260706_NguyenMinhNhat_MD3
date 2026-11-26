package com.rikkei.buildingmanagement.service;

import com.rikkei.buildingmanagement.exception.BadRequestException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;

@Service
public class FileUploadService {
    private final String UPLOAD_DIR = "uploads/";
    public String uploadFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new BadRequestException("Vui lòng chọn file ảnh bản thiết kế");
        }
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new BadRequestException("File tải lên phải là định dạng hình ảnh (jpg, png, ...)");
        }
        try {
            Path uploadPath = Paths.get(UPLOAD_DIR);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            return UPLOAD_DIR + fileName;
        } catch (IOException e) {
            throw new RuntimeException("Lỗi khi lưu file ảnh: " + e.getMessage());
        }
    }
}