package com.ListOnGo.ServerListOnGo.Repository;


import com.ListOnGo.ServerListOnGo.Model.AllListModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface AllListRepository extends JpaRepository<AllListModel,Long> {
    //    @Query(value = "SELECT * FROM all_list_model WHERE user_of_list = :userId",nativeQuery = true)
    List<AllListModel> findByUserOfList_Id(Long userId);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM listongo_all_list WHERE user_of_list=:userId",nativeQuery = true)
    void deleteListByUserId(Long userId);
}