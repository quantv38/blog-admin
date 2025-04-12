package com.doomdev.admin_blog.services.image;

import com.doomdev.admin_blog.contants.errorCode.ErrorCode;
import com.doomdev.admin_blog.dtos.responses.UploadImageResponse;
import com.doomdev.admin_blog.exceptions.AppException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tika.Tika;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {
    private static final String UPLOAD_DIR = "uploads/";

    @Override
    public UploadImageResponse uploadImage(MultipartFile file) {
        if (!isValidMimeType(file)) {
            throw new AppException(ErrorCode.TYPE_IMAGE_INVALID);
        }
        try {
            Path path = Paths.get(UPLOAD_DIR);
            if (!Files.exists(path)) {
                Files.createDirectories(path);
            }

            String fileName = UUID.randomUUID() + "." + getFileExtension(file);
            Path filePath = path.resolve(fileName);

            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            return UploadImageResponse.builder()
                    .path(filePath.toString())
                    .build();
        } catch (IOException exception) {
            throw new AppException(ErrorCode.UPLOAD_IMAGE_FAILED);
        }
    }

    private String getFileExtension(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        if (originalFilename != null && originalFilename.contains(".")) {
            return originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
        } else {
            return "";
        }
    }

    private boolean isValidMimeType(MultipartFile file) {
        String[] allowedMimeTypes = {"image/png", "image/jpeg"};
        Tika tika = new Tika();
        try {
            String mimeType = tika.detect(file.getInputStream());
            return Arrays.asList(allowedMimeTypes).contains(mimeType);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return false;
    }
}
