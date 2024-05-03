package com.son.alarm_pjt.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;

@Controller
@Slf4j
public class HomeController {
    @GetMapping("/")
    public String home(){
        return "home";
    }

    @PostMapping("/saveImage")
    @ResponseBody
    public String saveImage(@RequestBody ImageData imageData) {
        String base64Data = imageData.getImageData().split(",")[1]; // remove data:image/png;base64,
        byte[] decodedBytes = Base64.getDecoder().decode(base64Data);

        try {
            String imagePath = "C:/Users/son/Downloads/saved_images/";
            String imageName = "signature.png";
            File directory = new File(imagePath);
            if (!directory.exists()) {
                directory.mkdirs();
            }
            FileOutputStream fos = new FileOutputStream(imagePath + imageName);
            fos.write(decodedBytes);
            log.info("이미지 경로는 ={}",  imagePath+imageName);
            fos.close();
            return "Image saved successfully";
        } catch (IOException e) {
            e.printStackTrace();
            return "Error saving image";
        }
    }
    public static class ImageData {
        private String imageData;

        public String getImageData() {
            return imageData;
        }

        public void setImageData(String imageData) {
            this.imageData = imageData;
        }
    }


}
