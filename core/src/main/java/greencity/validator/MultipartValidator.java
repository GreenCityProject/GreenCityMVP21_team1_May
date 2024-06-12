package greencity.validator;

import greencity.annotations.MultipartValidation;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

public class MultipartValidator implements ConstraintValidator<MultipartValidation, Object> {
    private List<String> validFileTypes;
    private long maxFileSize = 0;

    @Override
    public void initialize(MultipartValidation constraintAnnotation) {
        this.validFileTypes = Arrays.asList(constraintAnnotation.fileTypes().split(","));
        this.maxFileSize = constraintAnnotation.maxFileSize();
    }

    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext constraintValidatorContext) {
        if (obj == null) {
            return true;
        }

        if (obj instanceof MultipartFile mpf) {
            return isValidImage(mpf, constraintValidatorContext);
        } else if (obj.getClass().isArray()) {
            return isValidImageList((Object[]) obj, constraintValidatorContext);
        }
        return false;

    }

    private boolean isValidImage(MultipartFile image, ConstraintValidatorContext constraintValidatorContext) {
        String originalFilename = image != null ? image.getOriginalFilename() : "";
        String fileType = originalFilename != null ? originalFilename.toLowerCase().substring(originalFilename.lastIndexOf(".") + 1) : "";

        if (!validFileTypes.contains(fileType)) {
            constraintValidatorContext.buildConstraintViolationWithTemplate(String.format("Unsupported format: '%s'. Supported formats: {%s}",
                    fileType,
                    validFileTypes)).addConstraintViolation();
            return false;
        }

        if (image != null && maxFileSize != 0 && image.getSize() > maxFileSize) {
            constraintValidatorContext.buildConstraintViolationWithTemplate(
                            String.format("File size exceeds the maximum limit of %d bytes", maxFileSize))
                    .addConstraintViolation();
            return false;
        }

        return true;
    }

    private boolean isValidImageList(Object[] images, ConstraintValidatorContext constraintValidatorContext) {
        for (Object obj : images) {
            if (!(obj instanceof MultipartFile mpf) || !isValidImage(mpf, constraintValidatorContext)) {
                return false;
            }
        }
        return true;
    }
}
