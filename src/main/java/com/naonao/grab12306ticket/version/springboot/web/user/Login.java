package com.naonao.grab12306ticket.version.springboot.web.user;

import com.naonao.grab12306ticket.version.springboot.annotation.Decrypt;
import com.naonao.grab12306ticket.version.springboot.entity.database.UserInformationEntity;
import com.naonao.grab12306ticket.version.springboot.entity.request.LoginRequest;
import com.naonao.grab12306ticket.version.springboot.entity.response.LoginTestResponse;
import com.naonao.grab12306ticket.version.springboot.resultclass.service.ticket.login.LoginTestReturnResult;
import com.naonao.grab12306ticket.version.springboot.util.RSAUtil;
import com.naonao.grab12306ticket.version.springboot.web.base.AbstractUser;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @program: SpringBoot
 * @description:
 * @author: Wen lyuzhao
 * @create: 2019-05-28 23:16
 **/
@RestController
@CrossOrigin
@RequestMapping(value = "${url.prefix}" + "user")
public class Login extends AbstractUser {


    @Autowired
    private RSAUtil rsaUtil;

    /**
     * login test
     * request data format is json:
     * {
     *     "username12306":"...",
     *     "password12306":"..."
     * }
     * @param loginRequest      LoginRequest
     * @param request           request
     * @return                  LoginTestResponse
     */
    @Decrypt
    @PostMapping("login")
    public LoginTestResponse login(@RequestBody @NonNull LoginRequest loginRequest,
                                   HttpServletRequest request){
        String username12306 = loginRequest.getUsername12306();
        String password12306 = loginRequest.getPassword12306();
        // get post username and password
        if (username12306 != null && password12306 != null){
            // authentication, if session is valid return current session
            if (super.authentication(request)){
                return loginTestResponse(true, SUCCESS, request.getSession().getId(), username12306);
            }
            // find user table, check if is not first login
            UserInformationEntity userInformationEntity = findUserInformationEntity(username12306, password12306);
            if (userInformationEntity != null){
                // whether the password has matches, if it dose not match, the password
                // has been changed and a new test is required
                if (usernameAndPasswordIsMatch(userInformationEntity, username12306, password12306)){
                    request.getSession().setAttribute("username12306", username12306);
                    request.getSession().setAttribute("password12306", password12306);
                    return loginTestResponse(true, SUCCESS, request.getSession().getId(), username12306);
                }
            }
            // if need to perform 12306 login test, first check if is system maintenance time.
            if (systemMaintenanceTime()){
                return loginTestResponse(false, SYSTEM_MAINTENANCE_TIME, null, null);
            }
            // if login success then set session
            if (loginTestReturnResult(username12306, password12306).getStatus()){
                request.getSession().setAttribute("username12306", username12306);
                request.getSession().setAttribute("password12306", password12306);
                // update user table user password
                updatePassword(username12306, password12306);
                return loginTestResponse(true, SUCCESS, request.getSession().getId(), username12306);
            }
        }
        return loginTestResponse(false, REQUEST_INVALID, null, null);
    }

    /**
     * logout username, clear session username and password
     * @param request   request
     * @return          LoginTestResponse
     */
    @GetMapping("logout")
    @ResponseBody
    public LoginTestResponse logout(HttpServletRequest request){
        request.getSession().removeAttribute("username12306");
        request.getSession().removeAttribute("password12306");
        return loginTestResponse(true, SUCCESS, null, null);
    }

    /**
     * find user information from database, for check user is first login,
     * if exist return entity, otherwise return a null.
     * @param username12306     username12306
     * @param password12306     password12306
     * @return                  UserInformationEntity
     */
    private UserInformationEntity findUserInformationEntity(String username12306, String password12306){
        List<UserInformationEntity> userInformationEntityList = userInformationMapper.getEntityByUsernameAndPassword(username12306, password12306);
        if (userInformationEntityList.size() > 0){
            return userInformationEntityList.get(0);
        }
        return null;
    }

    /**
     * check user post username and password is match with database user information
     * @param userInformationEntity userInformationEntity
     * @param username12306         username12306
     * @param password12306         password12306
     * @return                      Boolean
     */
    private Boolean usernameAndPasswordIsMatch(UserInformationEntity userInformationEntity,
                                               String username12306,
                                               String password12306){
        String entityUsername12306 = userInformationEntity.getUsername12306().trim();
        String entityPassword12306 = userInformationEntity.getPassword12306().trim();
        username12306 = username12306.trim();
        password12306 = password12306.trim();
        if (entityUsername12306.equals(username12306) && entityPassword12306.equals(password12306)){
            return true;
        }
        return false;
    }

    /**
     * update user password, if user exist in database
     * @param username12306     username12306
     * @param password12306     password12306
     */
    private void updatePassword(String username12306, String password12306){
        List<UserInformationEntity> userInformationEntityList = userInformationMapper.getEntityByUsername(username12306);
        if (userInformationEntityList.size() > 0){
            for (UserInformationEntity userInformationEntity: userInformationEntityList){
                userInformationEntity.setPassword12306(password12306);
                userInformationMapper.update(userInformationEntity);
            }
        }
    }


    private LoginTestResponse loginTestResponse(LoginTestReturnResult loginTestReturnResult, String sessionId, String username12306){
        LoginTestResponse loginTestResponse = new LoginTestResponse();
        loginTestResponse.setStatus(loginTestReturnResult.getStatus());
        loginTestResponse.setHttpStatus(HTTP_SUCCESS);
        loginTestResponse.setMessage(loginTestReturnResult.getMessage());
        loginTestResponse.setSessionId(sessionId);
        loginTestResponse.setUsername12306(username12306);
        return loginTestResponse;
    }

    private LoginTestResponse loginTestResponse(Boolean status, String message, String sessionId, String username12306){
        LoginTestResponse loginTestResponse = new LoginTestResponse();
        loginTestResponse.setStatus(status);
        loginTestResponse.setHttpStatus(HTTP_SUCCESS);
        loginTestResponse.setMessage(message);
        loginTestResponse.setSessionId(sessionId);
        loginTestResponse.setUsername12306(username12306);
        return loginTestResponse;
    }


}
