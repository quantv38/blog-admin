package com.doomdev.admin_blog.services.image;

import com.doomdev.admin_blog.dtos.responses.UploadImageResponse;
import org.springframework.web.multipart.MultipartFile;

public interface ImageService {
    UploadImageResponse uploadImage(MultipartFile file);
}
