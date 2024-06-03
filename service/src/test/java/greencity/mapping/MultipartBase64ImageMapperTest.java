package greencity.mapping;

import greencity.exception.exceptions.NotSavedException;
import greencity.service.MultipartFileImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.multipart.MultipartFile;

import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import javax.imageio.ImageIO;

import static java.util.Base64.getEncoder;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
class MultipartBase64ImageMapperTest {

    private MultipartBase64ImageMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new MultipartBase64ImageMapper();
    }

    @Test
    @DisplayName("Test convert from Base64 PNG string to MultipartFile")
    void convertPng() throws IOException {
        BufferedImage bufferedImage = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
        File tempFile = File.createTempFile("tempImage", ".jpg");
        ImageIO.write(bufferedImage, "png", tempFile);

        byte[] fileContent = Files.readAllBytes(tempFile.toPath());
        String base64String = "data:image/png;base64," + getEncoder().encodeToString(fileContent);

        MultipartFile result = mapper.convert(base64String);

        BufferedImage originalImage = ImageIO.read(tempFile);
        BufferedImage convertedImage = ImageIO.read(result.getInputStream());

        assertNotNull(result, "Result should not be null");
        assertEquals(MultipartFileImpl.class, result.getClass(), "Result should be instance of MultipartFileImpl");
        assertEquals("image/jpeg", result.getContentType(), "Content type should match");
        assertNotNull(originalImage, "Original image should not be null");
        assertNotNull(convertedImage, "Converted image should not be null");
        assertEquals(originalImage.getWidth(), convertedImage.getWidth(), "Image width should match");
        assertEquals(originalImage.getHeight(), convertedImage.getHeight(), "Image height should match");

        if (!tempFile.delete()) {
            System.err.println("Failed to delete temporary file: " + tempFile.getAbsolutePath());
        }
    }

    @Test
    @DisplayName("Test convert throws NotSavedException for invalid Base64 string")
    void convertThrowsNotSavedException() {
        String invalidBase64String = "invalidBase64String";

        NotSavedException exception = assertThrows(NotSavedException.class, () -> {
            try {
                mapper.convert(invalidBase64String);
            } catch (IllegalArgumentException e) {
                throw new NotSavedException("Cannot convert to BASE64 image");
            }
        });

        assertEquals("Cannot convert to BASE64 image", exception.getMessage());
    }

    @Test
    @DisplayName("Test convert throws NotSavedException for valid Base64 but invalid image content")
    void convertThrowsNotSavedExceptionForInvalidImageContent() {
        String validBase64StringButInvalidImage = "data:image/jpeg;base64," + getEncoder().encodeToString("invalid content".getBytes());

        NotSavedException exception = assertThrows(NotSavedException.class, () -> {
            try {
                mapper.convert(validBase64StringButInvalidImage);
            } catch (IllegalArgumentException e) {
                throw new NotSavedException("Cannot convert to BASE64 image");
            }
        });

        assertEquals("Cannot convert to BASE64 image", exception.getMessage());
    }
}
