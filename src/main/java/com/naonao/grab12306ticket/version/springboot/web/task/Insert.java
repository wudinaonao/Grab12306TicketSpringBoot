package com.naonao.grab12306ticket.version.springboot.web.task;

import com.naonao.grab12306ticket.version.springboot.annotation.Authentication;
import com.naonao.grab12306ticket.version.springboot.entity.IResponseInterface;
import com.naonao.grab12306ticket.version.springboot.entity.database.GrabTicketInformationEntity;
import com.naonao.grab12306ticket.version.springboot.entity.database.NotificationInformationEntity;
import com.naonao.grab12306ticket.version.springboot.entity.database.StatusInformationEntity;
import com.naonao.grab12306ticket.version.springboot.entity.database.UserInformationEntity;
import com.naonao.grab12306ticket.version.springboot.entity.request.InsertRequest;
import com.naonao.grab12306ticket.version.springboot.entity.response.GeneralResponse;
import com.naonao.grab12306ticket.version.springboot.entity.response.InsertResponse;
import com.naonao.grab12306ticket.version.springboot.resultclass.service.preprocess.CheckInformationReturnResult;
import com.naonao.grab12306ticket.version.springboot.service.preprocess.CheckInformation;
import com.naonao.grab12306ticket.version.springboot.service.tools.ComputeHash;
import com.naonao.grab12306ticket.version.springboot.web.base.AbstractTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Map;

/**
 * @program: SpringBoot
 * @description:
 * @author: Wen lyuzhao
 * @create: 2019-05-15 04:41
 **/
@RestController
@RequestMapping("${url.prefix}" + "task")
@CrossOrigin
public class Insert extends AbstractTask {

    @Autowired
    private CheckInformation checkInformation;

    /**
     * input data format:
     *{
     *    "grabticketinformation":{...},
     *    "notificationinformation":{...}
     * }
     * @param insertRequest InsertRequest
     * @return              InsertResponse
     */
    @Authentication
    @PostMapping(value = "insert")
    public InsertResponse insertTask(@RequestBody InsertRequest insertRequest, HttpServletRequest request){
        Map<String, String> usernameAndPasswordMap = getUsernameAndPasswordBySession(request);
        // Authentication
        // if (usernameAndPasswordMap == null){
        //     // not session, please log in
        //     return insertResponse(false, USERNAME_AND_PASSWORD_HAVE_NOT_BEEN_VERIFIED);
        // }
        // get hash
        String hash = ComputeHash.fromGrabTicketInformation(insertRequest.getGrabTicketInformation());
        // get entity use to insert to database
        UserInformationEntity userInformationEntity = userInformationEntity(usernameAndPasswordMap, hash);
        GrabTicketInformationEntity grabTicketInformationEntity = grabTicketInformationEntity(insertRequest.getGrabTicketInformation(), hash);
        NotificationInformationEntity notificationInformationEntity = notificationInformationEntity(insertRequest.getNotificationInformation(), hash);
        StatusInformationEntity statusInformationEntity = statusInformationEntity(hash);
        // check if the data is valid
        CheckInformationReturnResult checkInformationReturnResult= checkInformation.check(grabTicketInformationEntity, notificationInformationEntity);
        if (!checkInformationReturnResult.getStatus()){
            return insertResponse(false, checkInformationReturnResult.getMessage());
        }
        // check for duplicates
        if (userInformationMapper.getUsernameAndPasswordByHash(hash) != null){
            return insertResponse(false, THE_SAME_TASK_ALREADY_EXISTS);
        }
        // check entity
        IResponseInterface iResponseInterface = checkInformationEntity(
                userInformationEntity,
                grabTicketInformationEntity,
                notificationInformationEntity,
                statusInformationEntity
        );
        if (!iResponseInterface.getStatus()){
            return insertResponse(false, iResponseInterface.getMessage());
        }
        // insert to database
        userInformationMapper.insert(userInformationEntity);
        grabTicketInformationMapper.insert(grabTicketInformationEntity);
        notificationInformationMapper.insert(notificationInformationEntity);
        statusInformationMapper.insert(statusInformationEntity);
        return insertResponse(true, SUCCESS);
    }


    private InsertResponse insertResponse(Boolean status, String message){
        InsertResponse insertResponse = new InsertResponse();
        insertResponse.setStatus(status);
        insertResponse.setHttpStatus(HTTP_SUCCESS);
        insertResponse.setMessage(message);
        return insertResponse;
    }

    /**
     * check insert information entity is valid,
     * whether there is a Null value.
     * @param entitys   entity list
     * @param <T>       generic
     * @return          IResponseInterface
     */
    private <T> IResponseInterface checkInformationEntity(T... entitys){
        String message;
        String[] excludeProperty = {
                "id",
        };
        // These items need to be converted from null to empty
        String[] nullToEmpty = {
                "backTrainDate",
                "trainName"
        };
        for (T entity: entitys){
            Field[] declaredFields = entity.getClass().getDeclaredFields();
            for (Field field: declaredFields){
                field.setAccessible(true);
                try{
                    // need conversation null to empty value, because database not allowed null.
                    if (Arrays.asList(nullToEmpty).contains(field.getName())){
                        field.set(entity, "");
                    }
                    // database allow id is null, then exclude it.
                    if (!Arrays.asList(excludeProperty).contains(field.getName())){
                        if (field.get(entity) == null){
                            message = field.getName() + " is null.";
                            return new GeneralResponse(false, 200, message);
                        }
                    }

                }catch (IllegalAccessException e){
                    return new GeneralResponse(false, 200, INFORMATION_INVALID);
                }
            }
        }
        return new GeneralResponse(true, 200, SUCCESS);
    }


}
