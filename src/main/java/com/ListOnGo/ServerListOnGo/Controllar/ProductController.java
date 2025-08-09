package com.ListOnGo.ServerListOnGo.Controllar;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ListOnGo.ServerListOnGo.Api.ApiLink;
import com.ListOnGo.ServerListOnGo.Model.ProductDTO;
import com.ListOnGo.ServerListOnGo.Model.ProductModel;
import com.ListOnGo.ServerListOnGo.Model.UserModel;
import com.ListOnGo.ServerListOnGo.Repository.ProductModelRepository;
import com.ListOnGo.ServerListOnGo.Repository.UserModelRepository;
import com.ListOnGo.ServerListOnGo.Service.ProductService;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/list-on-go")
@CrossOrigin("*")
public class ProductController {
    ApiLink api=new ApiLink();
//    String url_forImage="http://192.168.111.150:8080/api/list-on-go";
    String link=api.apiUrl();
    @Autowired
    ProductModelRepository productRepo;
    @Autowired
    UserModelRepository userRepo;
    @Autowired
    ProductService productService;

    @GetMapping("/product/hello")
    public String hello(){
        return "Hello from Product";
    }

    @GetMapping("/product/get-filter-product")
    public ResponseEntity<?> getFilteredProducts(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String nickname
    ) {try {
        List<ProductDTO> filteredProducts = productService.getFilteredProducts(category, title,nickname);
        return ResponseEntity.ok(filteredProducts);
    }catch (Exception e){

        System.out.println(e.getMessage());
        return ResponseEntity.ok(e.getMessage());
    }
    }

    @GetMapping("product/keyword")
    public ResponseEntity<?>searchKeyword(@RequestParam("keyword") String keyword){
        try{
          List<ProductDTO> product=  productRepo.searchProductByKeyword(keyword);
            return new ResponseEntity<>(product,HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>("Error",HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/product/get-all-product")
    public ResponseEntity<List<ProductDTO>> getAllProduct() {
        List<ProductModel> approvedProducts = productRepo.findByIsAdminApproveTrue();  // Only approved products
        List<ProductDTO> dtoList = approvedProducts.stream()
                .map(ProductDTO::new)
                .collect(Collectors.toList());

        return new ResponseEntity<>(dtoList, HttpStatus.OK);
    }

    @GetMapping("/product/image/{id}")
    public ResponseEntity<byte[]> getProductImage(@PathVariable Long id) {
        ProductModel product = productRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(product.getImageType()));
        return new ResponseEntity<>(product.getImageData(), headers, HttpStatus.OK);
    }


    @PostMapping(value = "/product/add-product", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> addProduct(
            @RequestPart("productModel") @NonNull String productJson,
            @RequestPart("image") MultipartFile image,
            @RequestParam("userId") Long userId) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            ProductModel productModel = objectMapper.readValue(productJson, ProductModel.class);

            UserModel user = userRepo.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

            productModel.setAddedBy(user);
            ProductModel savedProduct = productService.addProduct(productModel, image);
            return new ResponseEntity<>("Done", HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    @GetMapping("/product/for-admin")
    public ResponseEntity<?> forAdmin() {
        List<ProductModel> getAllProduct = productRepo.findAllWhereAdminApproveIsFalse();

        List<ProductDTO> dtoList = getAllProduct.stream()
                .map(product -> new ProductDTO(
                        product.getId(),
                        product.getTitle(),
                        product.getDescription(),
                        product.getPrice(),
                        product.getCategory(),
                        link+"/product/image/" + product.getId()
                ))
                .collect(Collectors.toList());

        return new ResponseEntity<>(dtoList, HttpStatus.OK);
    }

    @PutMapping("/product/make-for-user")
    public ResponseEntity<?>forUser(@RequestParam("imaId") Long imaId,@RequestParam("adminEmail")String adminEmail){
        try {
           int result= productRepo.makeFalseToTrue(imaId);
           int approveBy=productRepo.makeApproveAdmin(imaId,adminEmail);
           if (result>0&&approveBy>0){
               return new ResponseEntity<>("done",HttpStatus.OK);
           }else {
               return new ResponseEntity<>("error", HttpStatus.BAD_REQUEST);
           }
        }catch (Exception e){
            System.out.println(e.getMessage());
            return new ResponseEntity<>("Item not found",HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("product/approve/{Email}")
    public ResponseEntity<?> approve(@PathVariable("Email") String userEmail){
        List<ProductModel> approveProduct=productRepo.findAllForAdminSpecific(userEmail);
        List<ProductDTO> dtoList = approveProduct.stream()
                .map(product -> new ProductDTO(
                        product.getId(),
                        product.getTitle(),
                        product.getDescription(),
                        product.getPrice(),
                        product.getCategory(),
                        link+"/product/image/" + product.getId()
                ))
                .collect(Collectors.toList());
        return new ResponseEntity<>(dtoList,HttpStatus.OK);
    }
    @GetMapping("product/is-exist/{title}")
    public ResponseEntity<?>isExist(@PathVariable String title){
        Optional<ProductModel> present=productRepo.findOneByTitleIgnoreCase(title.toLowerCase());
        if (present.isPresent()) {
            return new ResponseEntity<>("This product already present",HttpStatus.BAD_REQUEST);
        }else {
            return new ResponseEntity<>("Done",HttpStatus.OK);
        }
    }

    @PutMapping("/product/update")
    public ResponseEntity<String> updateProduct(@RequestParam("price") double price,
                                                @RequestParam("description") String description,
                                                @RequestParam("id") Long proId,
                                                @RequestParam(value = "nickName",required = false) String nickName) {
        try {
            productRepo.updateProduct(proId, price, description,nickName);
            return ResponseEntity.ok("Product updated successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to update product: " + e.getMessage());
        }
    }

    @DeleteMapping("/product/delete")
    public  ResponseEntity<?> deleteProduct(@RequestParam("proId")Long proId){
        Optional<ProductModel> present=productRepo.findProductById(proId);
        if (present.isPresent()) {
            productRepo.deleteProductById(proId);
            return new ResponseEntity<>("Deleted Done",HttpStatus.OK);
        }else {
            return new ResponseEntity<>("Don't Find this product",HttpStatus.BAD_REQUEST);
        }
    }

}
