package com.naonao.grab12306ticket.version.springboot.web.base;

import com.naonao.grab12306ticket.version.springboot.resultclass.service.ticket.login.LoginTestReturnResult;
import com.naonao.grab12306ticket.version.springboot.service.tools.GeneralTools;
import com.naonao.grab12306ticket.version.springboot.service.tools.HttpTools;

/**
 * @program: SpringBoot
 * @description:
 * @author: Wen lyuzhao
 * @create: 2019-05-29 18:37
 **/
public class AbstractUser extends AbstractWeb{

    protected static final String REQUEST_INVALID = "request invalid.";
    protected static final String SYSTEM_MAINTENANCE_TIME = "system maintenance time.";

    protected static final String INVALID_OLD_PASSWORD = "invalid old password.";
    protected static final String CHANGE_PASSWORD_SUCCESS = "change password success.";

    /**
     * test if username and password can it log in 12306
     * @param username12306     username12306
     * @param password12306     password12306
     * @return                  LoginTestReturnResult
     */
    protected LoginTestReturnResult loginTestReturnResult(String username12306, String password12306){
        // produce
        return new com.naonao.grab12306ticket.version.springboot.service.ticket.login.Login(
                HttpTools.getSession(30000)
        ).loginTest(
                username12306,
                password12306
        );
        // test
        // LoginTestReturnResult loginTestReturnResult = new LoginTestReturnResult();
        // loginTestReturnResult.setStatus(true);
        // loginTestReturnResult.setSession(null);
        // loginTestReturnResult.setMessage(SUCCESS);
        // return loginTestReturnResult;
    }

    protected Boolean systemMaintenanceTime(){
        return GeneralTools.systemMaintenanceTime();
    }
}
