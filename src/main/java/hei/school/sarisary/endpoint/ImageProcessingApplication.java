package src.main.java.hei.school.sarisary.endpoint;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;*/

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
@RestController
public class ImageProcessingApplication {

    private final Map<String, String> resultsMap = new HashMap<>();

    public static void main(String[] args) {
        SpringApplication.run(ImageProcessingApplication.class, args);
    }

    @PostMapping("/black-and-white/{id}")
    public ResponseEntity<Void> convertToBlackAndWhite(@PathVariable String id, @RequestBody String base64Image) {
        try {
            BufferedImage originalImage = decodeToImage(base64Image);

            BufferedImage blackAndWhiteImage = convertToBlackAndWhite(originalImage);

            String blackAndWhiteBase64 = encodeToString(blackAndWhiteImage);

            resultsMap.put(id, blackAndWhiteBase64);

            return ResponseEntity.ok().build();
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    private BufferedImage decodeToImage(String imageString) throws IOException {
        byte[] imageByte = Base64.getDecoder().decode(imageString);
        try (ByteArrayInputStream bis = new ByteArrayInputStream(imageByte)) {
            return ImageIO.read(bis);
        }
    }

    private BufferedImage convertToBlackAndWhite(BufferedImage originalImage) {
        BufferedImage bwImage = new BufferedImage(originalImage.getWidth(), originalImage.getHeight(), BufferedImage.TYPE_BYTE_BINARY);
        Graphics2D graphics = bwImage.createGraphics();
        graphics.drawImage(originalImage, 0, 0, Color.WHITE, null);
        graphics.dispose();
        return bwImage;
    }

    private String encodeToString(BufferedImage image) throws IOException {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            ImageIO.write(image, "png", bos);
            byte[] imageBytes = bos.toByteArray();
            return Base64.getEncoder().encodeToString(imageBytes);
        }
    }
}

