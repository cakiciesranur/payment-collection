package com.eny.paymentcollection.dto.response;

import com.eny.paymentcollection.model.UserEntity;
import lombok.Data;

import java.util.List;

@Data
public class UserListResponse {
    List<UserEntity> userList;
}
