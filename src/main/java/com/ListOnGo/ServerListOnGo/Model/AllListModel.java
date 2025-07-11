package com.ListOnGo.ServerListOnGo.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "listongo_all_list")
public class AllListModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "title",nullable = false)
    private String title;
    @Column(name = "price",nullable = false)
    private double price;
    @Column(name = "date_time", nullable = false)
    @UpdateTimestamp
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime dateAndTime;
    @Column(name = "quantity")
    private int quantity=1;
    @Column(name = "list_name",nullable = true)
    private String listName;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_of_list")
    @JsonIgnore
    private UserModel userOfList;
}