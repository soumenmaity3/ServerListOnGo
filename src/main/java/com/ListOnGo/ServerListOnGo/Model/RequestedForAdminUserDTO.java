package com.ListOnGo.ServerListOnGo.Model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
// DTO for requested admin user information
public class RequestedForAdminUserDTO {
    private Long id;
    private String email;
    private String adminReason;

}
