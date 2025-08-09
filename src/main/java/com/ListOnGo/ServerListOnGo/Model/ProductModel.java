package com.ListOnGo.ServerListOnGo.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "listongo_product")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String description;
    @Column(nullable = true,name = "nick_name")
    private String nickName;
    @Column(nullable = true,name = "who_admin")
    private String whoAdmin;

    private String imageName;
    private String imageType;
    @Lob   //large Object
    @Basic(fetch = FetchType.EAGER)
    @Column(nullable = false)
    private byte[] imageData;

    @Column(nullable = false)
    private Double price;
    @Column(nullable = false)
    private String category;

    private boolean isAdminApprove = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "added_by_user_id")
    @JsonIgnore
    private UserModel addedBy;
}