package com.shoppingbackend.admin.product.controller;

import com.shopping.common.entity.Category;
import com.shopping.common.entity.Product;
import com.shopping.common.entity.ProductImage;
import com.shoppingbackend.admin.category.service.CategoryServiceImpl;
import com.shoppingbackend.admin.product.service.ProductServiceImpl;
import com.shoppingbackend.admin.productImage.ProductImageService;
import com.shoppingbackend.admin.util.FileUploadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Controller
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductServiceImpl productService;

    @Autowired
    private ProductImageService productImageService;

    @Autowired
    private CategoryServiceImpl categoryService;

    @ModelAttribute("categoryList")
    public List<Category> getCategoryList() {
        return categoryService.getAllCategories();
    }


    @GetMapping("")
    public String showProductList(Model model) {

        List<Product> productList = productService.getAllProducts();
        model.addAttribute("productList", productList);

        return showProductsByPage(1,"asc","name",null,"0",model);
    }

    @GetMapping("/page/{pageNumber}")
    public String showProductsByPage(
            @PathVariable("pageNumber") Integer pageNumber,
            @Param("sortDir") String sortDir,
            @Param("sortField") String sortField,
            @Param("keyword") String keyword,
            @Param("categoryId") String categoryId,
            Model model
    ) {
        if(sortDir==null || sortDir.isEmpty())
            sortDir = "asc";
        if(sortField==null || sortField.isEmpty())
            sortField = "name";

        String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";

        Page<Product> pageResult = productService.getProductsByPage(pageNumber, sortField, sortDir, keyword, categoryId);

        // get information for pagination:
        List<Product> productList = pageResult.getContent();
        Map<String, String> categoryNameOfProducts = new HashMap<>();
        for(Product product : productList) {
            String categoryName = categoryService.getCategoryById(product.getCategoryId()).getName();
            categoryNameOfProducts.put(product.getId(), categoryName);
        }

        int totalPages = pageResult.getTotalPages();
        long totalElements = pageResult.getTotalElements();
        int firstPageNumber = ((pageNumber-1)*categoryService.ITEMS_NUMBER_PER_PAGE)+1;
        int lastPageNumber = pageNumber * categoryService.ITEMS_NUMBER_PER_PAGE;
        if(lastPageNumber>=totalElements)
            lastPageNumber = (int) totalElements;

        model.addAttribute("productList", productList);
        model.addAttribute("categoryNameOfProducts", categoryNameOfProducts);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("totalElements", totalElements);
        model.addAttribute("pageNumber", pageNumber);
        model.addAttribute("reverseSortDir", reverseSortDir);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("sortField", sortField);
        model.addAttribute("keyword", keyword);
        model.addAttribute("firstPageNumber", firstPageNumber);
        model.addAttribute("lastPageNumber", lastPageNumber);

        if(categoryId!=null)
           model.addAttribute("selectedCategoryId", categoryId);
        return "product/product";
    }


    @GetMapping("/new")
    public String showProductForm(@ModelAttribute("product") Product product, Model model) {
        model.addAttribute("pageTitle", "Create New Product");

        // Create mode -> extra image is 0
        model.addAttribute("numberOfExistingExtraImages", 0);
        return "product/product_form";
    }

    // SAVE : CREATE OR UPDATE :
    @PostMapping("/save")
    public String saveProduct(
            @ModelAttribute("product") Product product,
            @RequestParam(value = "inputImageFile", required = false) MultipartFile mainImageMultipartFile,
            @RequestParam(value = "inputExtraImageFile", required = false) MultipartFile[] extraImagesMultipartFiles,
            @RequestParam(value = "imageIds", required = false) String[] extraImageIds,
            @RequestParam(value = "imageNames", required = false) String[] extraImageNames,
            RedirectAttributes redirectAttributes) throws IOException {

        // Nếu ID là chuỗi rỗng, đặt nó thành null để MongoDB tự động tạo ID mới
        if (product.getId() == null || product.getId().trim().isEmpty()) {
            product.setId(null);
        }

        // ---------- 1. handle product images:
        // set main image Product
        setMainImageName(mainImageMultipartFile, product);

        // Then, set existing extra images for Product :
        setExistingExtraImages(extraImageIds, extraImageNames, product);

        // Then, set new extra images :
        setNewExtraImageNames(extraImagesMultipartFiles, product);

        // Delete các extra images trong Local Dir mà không còn tồn tại trong Form (hay DB) nữa:
        deleteExtraImagesWereRemovedOnForm(product);

        // save product to DB:
        Product savedProduct = productService.saveProduct(product);


        saveUploadedImage(mainImageMultipartFile, extraImagesMultipartFiles, savedProduct);

        redirectAttributes.addFlashAttribute("message","The Product( name: "+product.getName()+" ) has been saved successfully!");

        return "redirect:/products";
    }


    // ------------------ Method for create and edit product images and product details
    // ------- for product images :
    private void setMainImageName(MultipartFile multipartFile, Product product) {
        // in case file input has uploaded file:
        if(multipartFile!=null && !multipartFile.isEmpty()) {
            // get uploaded file from file input:
            String nameUploadedFile = StringUtils.cleanPath(multipartFile.getOriginalFilename());

            product.setMainImage(nameUploadedFile);
        }
    }

    private void setExistingExtraImages(String[] extraImageIds, String[] extraImageNames, Product product) {
        // Nếu mà extraImagesIds hay extraImageNames không có giá trị thì thoát hàm không làm gì cả
        if(extraImageIds==null || extraImageIds.length==0)
            return;

        Set<ProductImage> productImageSet = new HashSet<ProductImage>();

        // get existing extra images from Form
        for(int i=0; i<extraImageIds.length; i++) {
            String extraImageId = extraImageIds[i];
            String extraImageName = extraImageNames[i];

            productImageSet.add(new ProductImage(extraImageId, extraImageName, product));
        }

        // set existing extra images
        product.setExtraImages(productImageSet);
    }

    private void setNewExtraImageNames(MultipartFile[] multipartFiles, Product product) {
        // Nếu mà có thêm file new extraImage thì set, không thì không làm gì cả
        if(multipartFiles!=null && multipartFiles.length > 0)
        {
            for(MultipartFile multipartFile : multipartFiles) {
                // Nếu multipartFile mà empty thì add nó vào extraImages
                if(multipartFile.isEmpty())
                    continue;
                // get uploaded file from file input:
                String nameUploadedFile = StringUtils.cleanPath(multipartFile.getOriginalFilename());

                // check if this image name doesn't exist in DB -> add new extra image:
                if(!product.containsExtraImage(nameUploadedFile))
                    product.addExtraImages(nameUploadedFile);
            }
        }
    }

    private void deleteExtraImagesWereRemovedOnForm(Product product) {
        String extraImageDir = "../product-images/"+product.getId()+"/extraImages";
        Path extraImageDirPath = Paths.get(extraImageDir);
        try {
            // Lặp qua các file trong extraImageDir -> check nếu extra images của Product không chứa file nào trong extraImageDir thì xóa file đó khỏi đó
            Files.list(extraImageDirPath).forEach(fileItem -> {
                String fileName = fileItem.getFileName().toString();
                if(!product.containsExtraImage(fileName)) {
                    try {
                        Files.delete(fileItem);
                    } catch (IOException e) {
                        System.out.println("Could not delete extra image file: "+fileName);
                    }
                }
            });
        }
        catch(IOException e)
        {
            System.out.println("Could not list extra image directory: "+extraImageDir);
        }
    }

    private void saveUploadedImage(
            MultipartFile mainImageMultipartFile,
            MultipartFile[] extraImageMultipartFiles,
            Product savedProduct) throws IOException {
        // in case inputImageFile has uploaded file:
        if(!mainImageMultipartFile.isEmpty()) {
            // get uploaded file from file input:
            String nameUploadedFile = StringUtils.cleanPath(mainImageMultipartFile.getOriginalFilename());

            // create name dir : product-images/<Id of Product just saved >
            String nameUploadDir = "../product-images/"+savedProduct.getId();

            // clean files in advanced of dir :
            FileUploadUtil.cleanDirectory(nameUploadDir);

            // Then upload new file to dir :
            FileUploadUtil.uploadFileToLocalDirectory(nameUploadDir, nameUploadedFile, mainImageMultipartFile);
        }

        // in case inputExtraImageFiles have uploaded file:
        if(extraImageMultipartFiles.length > 0) {

            String nameUploadDir = "../product-images/"+savedProduct.getId()+"/extras";
            for(MultipartFile multipartFile : extraImageMultipartFiles) {
                // Nếu multipartFile mà empty thì add nó vào extraImages
                if(multipartFile.isEmpty())
                    continue;

                // get uploaded file from file input:
                String nameUploadedFile = StringUtils.cleanPath(multipartFile.getOriginalFilename());

                // Then upload new file to dir :
                FileUploadUtil.uploadFileToLocalDirectory(nameUploadDir, nameUploadedFile, multipartFile);
            }
        }
    }

    // EDIT
    @GetMapping("/edit/{id}")
    public String showProductEditForm(@PathVariable("id") String id, Model model) {
        Product product = productService.getProductById(id);
        model.addAttribute("product", product);
        model.addAttribute("pageTitle", "Edit Product");

        // Edit mode -> extra images are
        model.addAttribute("numberOfExistingExtraImages", product.getExtraImages().size());
        return "product/product_form";
    }

    // DELETE
    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable("id") String id, RedirectAttributes redirectAttributes) {
            productService.deleteProduct(id);
            // after delete product, remove image dir of that product:
            String imageProductDirPath = "../product-images/"+id;
            String extraImageProductDirPath = "../product-images/"+id+"/extras";

            // remove extras Dir before, then remove product-images Dir:
            FileUploadUtil.removeDirectory(extraImageProductDirPath);
            FileUploadUtil.removeDirectory(imageProductDirPath);

            redirectAttributes.addFlashAttribute("message", "The Product(id: "+id+") has been deleted successfully");
            return "redirect:/products";
    }

}
