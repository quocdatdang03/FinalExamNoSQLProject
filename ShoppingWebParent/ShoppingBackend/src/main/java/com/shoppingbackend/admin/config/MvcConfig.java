package com.shoppingbackend.admin.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    // Handle path for exposing user image
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // -------- Config Image Resource for Product:
        String productImageDirName = "product-images";
        // get path category-images dir :
        Path productImageDir = Paths.get("../"+productImageDirName); // Vì ta lưu folder brand-images ở ngoài ShoppingBackend nên phải ../

        // Những request bắt đầu bằng /brand-images/** -> thì đều có thể truy cập vào folder brand-images
        registerImageResources(productImageDirName, productImageDir, registry);
    }

    private void registerImageResources(String imageDirName, Path imageDir, ResourceHandlerRegistry registry) {

        // get image path:
        String imageDirPath = imageDir.toFile().getAbsolutePath();

        // Những request bắt đầu bằng /<imageDirName>/** -> thì đều có thể truy cập vào folder <imageDirName>
        registry.addResourceHandler("/"+imageDirName+"/**")
                .addResourceLocations("file:/"+imageDirPath+"/");
    }
}