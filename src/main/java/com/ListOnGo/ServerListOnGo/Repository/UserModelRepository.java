package com.ListOnGo.ServerListOnGo.Repository;

import com.ListOnGo.ServerListOnGo.Model.RequestedForAdminUserDTO;
import com.ListOnGo.ServerListOnGo.Model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface UserModelRepository extends JpaRepository<UserModel,Long> {
    @Query(value = "SELECT email FROM listongo_user WHERE email=:email",nativeQuery = true)
    List<String> checkUserEmail(String email);
    @Query(value = "SELECT password FROM listongo_user WHERE email=:email",nativeQuery = true)
    String checkUserPasswordByEmail(String email);
    @Query(value = "SELECT id FROM listongo_user WHERE email=:email",nativeQuery = true)
    Long getUserIdByEmail(String email);
    @Query(value = "SELECT username FROM listongo_user WHERE id=:id",nativeQuery = true)
    String findUserNameById(long id);

    @Query(value = "SELECT is_admin FROM listongo_user  WHERE email=:email",nativeQuery = true)
    Boolean checkUserAdminOrNotByEmail(String email);

    @Modifying
    @Transactional
    @Query(value = "UPDATE listongo_user SET otp = :otp, otp_used = false WHERE email = :email",nativeQuery = true)
    void storeOtpWithUsedFalse(String otp, String email);

    @Modifying
    @Transactional
    @Query(value = "UPDATE listongo_user SET otp_used = true WHERE email = :email",nativeQuery = true)
    int afterUseOtpUsedOtpFalse(String email);

    @Query(value = "SELECT COUNT(*) > 0 FROM listongo_user WHERE email=:email AND otp=:otp AND otp_used=false", nativeQuery = true)
    boolean checkOtpByEmail(@Param("email") String email, @Param("otp") String otp);

    @Transactional
    @Modifying
    @Query(value = "UPDATE listongo_user SET password = :password WHERE email = :email", nativeQuery = true)
    int resetPasswordByEmail(@Param("email") String email, @Param("password") String password);


    @Modifying
    @Transactional
    @Query(value = "UPDATE listongo_product SET added_by_user_id = NULL WHERE added_by_user_id = :userId", nativeQuery = true)
    int detachUserProducts(@Param("userId") Long userId);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM listongo_user WHERE email = :email ", nativeQuery = true)
    int deleteByEmail(@Param("email") String email);

    @Query(value = "SELECT * FROM listongo_user WHERE email=:email ",nativeQuery = true)
    Optional<UserModel> findUserEmail(String email);

    @Query(value = "SELECT credit FROM listongo_user WHERE email=:email",nativeQuery = true)
    int getUserCreditByEmail(String email);

    @Modifying
    @Transactional
    @Query(value = "UPDATE listongo_user SET credit = credit + :credit WHERE email = :email", nativeQuery = true)
    int buyCredit(String email, Integer credit);

    @Modifying
    @Transactional
    @Query(value = "UPDATE listongo_user SET is_admin = false , req_done='null' WHERE email = :email",nativeQuery = true)
    void demotionAdmin(String email);

    @Modifying
    @Transactional
    @Query(value = "UPDATE listongo_user SET credit = credit - :cost WHERE email = :email", nativeQuery = true)
    void costCreditByUserEmail(String email, int cost);

    @Modifying
    @Transactional
    @Query(value = "UPDATE listongo_user SET admin_reason=:reason, req_done ='approved', is_admin = true,approve_by=:adEmail WHERE email = :userEmail",nativeQuery = true)
    int makeUserAdmin(@Param("adEmail") String adEmail,@Param("userEmail") String userEmail,@Param("reason")String reason);

    @Modifying
    @Transactional
    @Query(value = "UPDATE listongo_user SET req_done ='requested', admin_reason = :reason WHERE email = :email",nativeQuery = true)
    int reqUserAdmin(String email, String reason);

    @Modifying
    @Transactional
    @Query(value = "SELECT id,email,admin_reason FROM listongo_user WHERE req_done = 'requested'",nativeQuery = true)
    List<RequestedForAdminUserDTO> pendingRequest();
    @Modifying
    @Transactional
    @Query(value = "UPDATE listongo_user SET approve_by=:adEmail,req_done ='cancel', admin_reason = :reason WHERE email = :userEmail",nativeQuery = true)
    void denyAdminRequest(String adEmail,String userEmail,String reason);

    @Query(value = "SELECT req_done FROM listongo_user WHERE email=:email",nativeQuery = true)
    String checkStatus(String email);
}
