package com.naonao.grab12306ticket.version.springboot.web.task;

import com.naonao.grab12306ticket.version.springboot.annotation.Authentication;
import com.naonao.grab12306ticket.version.springboot.entity.database.GrabTicketInformationEntity;
import com.naonao.grab12306ticket.version.springboot.entity.database.NotificationInformationEntity;
import com.naonao.grab12306ticket.version.springboot.entity.request.UpdateRequest;
import com.naonao.grab12306ticket.version.springboot.entity.response.UpdateResponse;
import com.naonao.grab12306ticket.version.springboot.resultclass.service.preprocess.CheckInformationReturnResult;
import com.naonao.grab12306ticket.version.springboot.service.preprocess.CheckInformation;
import com.naonao.grab12306ticket.version.springboot.web.base.AbstractTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @program: SpringBoot
 * @description:
 * @author: Wen lyuzhao
 * @create: 2019-05-28 23:15
 **/
@RestController
@RequestMapping("${url.prefix}" + "task")
@CrossOrigin
public class Update extends AbstractTask {

    /**
     * update you need to input complete data, example:
     * {
     *     "grabticketinformation":"...",
     *     "notificationinformation":"...",
     * }
     * you can't just enter the date to update.
     */

    @Autowired
    private CheckInformation checkInformation;

    @Authentication
    @PutMapping(value = "update", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public UpdateResponse updateTask(@RequestBody UpdateRequest updateRequest, HttpServletRequest request){
        Map<String, String> usernameAndPasswordMap = getUsernameAndPasswordBySession(request);
        // Authentication
        // if (usernameAndPasswordMap == null){
        //     // not session, please log in
        //     return updateResponse(false, USERNAME_AND_PASSWORD_HAVE_NOT_BEEN_VERIFIED);
        // }
        String hash = updateRequest.getGrabTicketInformation().getHash();
        // get entity use to insert to database
        GrabTicketInformationEntity grabTicketInformationEntity = grabTicketInformationEntity(updateRequest.getGrabTicketInformation(), hash);
        NotificationInformationEntity notificationInformationEntity = notificationInformationEntity(updateRequest.getNotificationInformation(), hash);
        // check if the data is valid
        CheckInformationReturnResult checkInformationReturnResult= checkInformation.check(grabTicketInformationEntity, notificationInformationEntity);
        if (!checkInformationReturnResult.getStatus()){
            return updateResponse(false, checkInformationReturnResult.getMessage());
        }
        // insert to database
        grabTicketInformationMapper.update(grabTicketInformationEntity);
        notificationInformationMapper.update(notificationInformationEntity);
        return updateResponse(true, SUCCESS);
    }


    private UpdateResponse updateResponse(Boolean status, String message){
        UpdateResponse updateResponse = new UpdateResponse();
        updateResponse.setStatus(status);
        updateResponse.setHttpStatus(HTTP_SUCCESS);
        updateResponse.setMessage(message);
        return updateResponse;

    }
}
