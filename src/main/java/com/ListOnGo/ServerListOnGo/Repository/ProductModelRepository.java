package com.ListOnGo.ServerListOnGo.Repository;


import com.ListOnGo.ServerListOnGo.Model.ProductDTO;
import com.ListOnGo.ServerListOnGo.Model.ProductModel;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.Optional;

public interface ProductModelRepository extends JpaRepository<ProductModel,Long>, JpaSpecificationExecutor<ProductModel> {

    List<ProductModel> findAll(Specification<ProductModel> spec);
    List<ProductModel> findByIsAdminApproveTrue();

    @Query(value = "SELECT * FROM listongo_product WHERE is_admin_approve=false", nativeQuery = true)
    List<ProductModel> findAllWhereAdminApproveIsFalse();
    @Transactional
    @Modifying
    @Query(value = "UPDATE listongo_product SET is_admin_approve=true WHERE id=:imaId",nativeQuery = true)
    int makeFalseToTrue(Long imaId);

    @Query(value = "SELECT * FROM listongo_product WHERE who_admin=:userEmail", nativeQuery = true)
    List<ProductModel> findAllForAdminSpecific(String userEmail);

    @Transactional
    @Modifying
    @Query(value = "UPDATE listongo_product SET who_admin=:adminEmail WHERE id=:imaId",nativeQuery = true)
    int makeApproveAdmin(Long imaId,String adminEmail);

    @Query(value = "SELECT * FROM listongo_product WHERE LOWER(title) = LOWER(:title) LIMIT 1", nativeQuery = true)
    Optional<ProductModel> findOneByTitleIgnoreCase( String title);

    @Modifying
    @Transactional
    @Query(value = "UPDATE listongo_product  SET price = :price, description = :description,nick_name=:nickName WHERE id = :id",nativeQuery = true)
    void updateProduct(@Param("id") Long id, @Param("price") double price, @Param("description") String description,@Param("nickName")String nickName);

    @Query(value = "SELECT * FROM listongo_product WHERE id = :proId LIMIT 1",nativeQuery = true)
    Optional<ProductModel> findProductById(Long proId);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM listongo_product WHERE id=:proId",nativeQuery = true)
    void deleteProductById(Long proId);

    @Query("SELECT p FROM ProductModel p WHERE p.isAdminApprove = true AND (" +
            "LOWER(p.title) LIKE LOWER(CONCAT('%', :keyword ,'%')) OR " +
            "LOWER(p.description) LIKE LOWER(CONCAT('%', :keyword ,'%')) OR " +
            "LOWER(p.nickName) LIKE LOWER(CONCAT('%', :keyword ,'%')) OR " +
            "LOWER(p.category) LIKE LOWER(CONCAT('%', :keyword ,'%')))")
    List<ProductDTO> searchProductByKeyword(@Param("keyword") String keyword);



}