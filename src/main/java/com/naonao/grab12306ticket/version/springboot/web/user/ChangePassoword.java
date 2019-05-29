package com.naonao.grab12306ticket.version.springboot.web.user;

import com.naonao.grab12306ticket.version.springboot.annotation.Authentication;
import com.naonao.grab12306ticket.version.springboot.annotation.Decrypt;
import com.naonao.grab12306ticket.version.springboot.entity.database.UserInformationEntity;
import com.naonao.grab12306ticket.version.springboot.entity.request.ChangePasswordRequest;
import com.naonao.grab12306ticket.version.springboot.entity.response.GeneralResponse;
import com.naonao.grab12306ticket.version.springboot.resultclass.service.ticket.login.LoginTestReturnResult;
import com.naonao.grab12306ticket.version.springboot.web.base.AbstractUser;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @program: SpringBoot
 * @description:
 * @author: Wen lyuzhao
 * @create: 2019-05-29 17:50
 **/
@RestController
@CrossOrigin
@RequestMapping(value = "${url.prefix}" + "user")
public class ChangePassoword extends AbstractUser {

    @Authentication
    @Decrypt
    @PutMapping(value = "changePassword", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public GeneralResponse changePassword(@RequestBody ChangePasswordRequest changePasswordRequest, HttpServletRequest request){
        String username12306 = changePasswordRequest.getUsername12306();
        String oldPassword12306 = changePasswordRequest.getOldPassword12306();
        String newPassword12306 = changePasswordRequest.getNewPassword12306();
        List<UserInformationEntity> userInformationEntityList = userInformationMapper.getEntityByUsernameAndPassword(username12306, oldPassword12306);
        if (userInformationEntityList.size() <= 0){
            return generalResponse(false, INVALID_OLD_PASSWORD);
        }
        // if need to perform 12306 login test, first check if is system maintenance time.
        if (systemMaintenanceTime()){
            return generalResponse(false, SYSTEM_MAINTENANCE_TIME);
        }
        // if login success then set session
        LoginTestReturnResult loginTestReturnResult = loginTestReturnResult(username12306, newPassword12306);
        if (loginTestReturnResult.getStatus()){
            for (UserInformationEntity userInformationEntity: userInformationEntityList){
                userInformationEntity.setPassword12306(newPassword12306);
                userInformationMapper.update(userInformationEntity);
            }
            // delete old session
            request.getSession().removeAttribute("username12306");
            request.getSession().removeAttribute("password12306");
            return generalResponse(true, CHANGE_PASSWORD_SUCCESS);
        }
        return generalResponse(false, loginTestReturnResult.getMessage());
    }


    private GeneralResponse generalResponse(Boolean status, String message){
        GeneralResponse generalResponse = new GeneralResponse();
        generalResponse.setStatus(status);
        generalResponse.setHttpStatus(HTTP_SUCCESS);
        generalResponse.setMessage(message);
        return generalResponse;
    }
}
