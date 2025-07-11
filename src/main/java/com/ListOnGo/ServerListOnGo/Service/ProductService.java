package com.ListOnGo.ServerListOnGo.Service;

import com.ListOnGo.ServerListOnGo.*;
import com.ListOnGo.ServerListOnGo.Api.ApiLink;
import com.ListOnGo.ServerListOnGo.Model.ProductDTO;
import com.ListOnGo.ServerListOnGo.Model.ProductModel;
import com.ListOnGo.ServerListOnGo.Repository.ProductModelRepository;
import jakarta.persistence.criteria.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    ApiLink api=new ApiLink();
    String link=api.apiUrl();
    @Autowired
    private ProductModelRepository productRepository;

    public ProductModel addProduct(ProductModel productModel, MultipartFile image) throws IOException {

        productModel.setImageName(image.getOriginalFilename());
        productModel.setImageType(image.getContentType());
        productModel.setImageData(image.getBytes());

        return productRepository.save(productModel);
    }


    public List<ProductDTO> getFilteredProducts(String category, String title, String nickname) {
        List<ProductModel> products = productRepository.findAll((root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            predicates.add(cb.isTrue(root.get("isAdminApprove")));

            if (category != null && !category.isEmpty()) {
                predicates.add(
                        cb.equal(cb.lower(root.get("category")), category.toLowerCase())
                );
            }

            if (title != null && !title.isEmpty()) {
                predicates.add(
                        cb.equal(cb.lower(root.get("title")), title.toLowerCase())
                );
            }
            if (nickname != null && !nickname.isEmpty()) {
                predicates.add(
                        cb.equal(cb.lower(root.get("nickName")), nickname.toLowerCase())
                );
            }


            return cb.and(predicates.toArray(new Predicate[0]));
        });
        return products.stream().map(product -> new ProductDTO(
                product.getId(),
                product.getTitle(),
                product.getDescription(),
                product.getPrice(),
                product.getCategory(),
                link+"product/image/" + product.getId()
        )).collect(Collectors.toList());

    }


}
