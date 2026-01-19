package com.ecommerce.ecommerce.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;

public class FileTypeValidator implements ConstraintValidator<ValidFileType, MultipartFile> {

    private String[] allowedTypes;

    @Override
    public void initialize(ValidFileType constraint) {
        this.allowedTypes = constraint.allowedTypes();
    }

    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {
        if (file == null || file.isEmpty()) {
            return true;
        }

        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null) {
            return false;
        }

        String extension = getFileExtension(originalFilename).toLowerCase();

        for (String allowedType : allowedTypes) {
            if (allowedType.equalsIgnoreCase(extension)) {
                return true;
            }
        }

        return false;
    }

    private String getFileExtension(String filename) {
        int dotIndex = filename.lastIndexOf('.');
        return (dotIndex == -1) ? "" : filename.substring(dotIndex + 1);
    }
}