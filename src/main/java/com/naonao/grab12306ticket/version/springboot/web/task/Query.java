package com.naonao.grab12306ticket.version.springboot.web.task;

import com.google.common.base.Joiner;
import com.naonao.grab12306ticket.version.springboot.annotation.Authentication;
import com.naonao.grab12306ticket.version.springboot.constants.TaskStatusName;

import com.naonao.grab12306ticket.version.springboot.entity.database.AllInformationEntity;
import com.naonao.grab12306ticket.version.springboot.entity.database.StatusInformationEntity;
import com.naonao.grab12306ticket.version.springboot.entity.database.UserInformationEntity;
import com.naonao.grab12306ticket.version.springboot.entity.request.QueryRequest;
import com.naonao.grab12306ticket.version.springboot.entity.response.QueryResponse;
import com.naonao.grab12306ticket.version.springboot.web.base.AbstractQuery;
import lombok.NonNull;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @program: SpringBoot
 * @description:
 * @author: Wen lyuzhao
 * @create: 2019-05-14 00:15
 **/
@RestController
@EnableAutoConfiguration
@RequestMapping(value = "${url.prefix}" + "task")
@CrossOrigin
public class Query extends AbstractQuery {


    /**
     * get the user task by status is wait
     * @param request   request
     * @return          QueryResponse
     */
    @Authentication
    @GetMapping(value = "getInformationByStatusWait", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public QueryResponse getInformationByStatusWait(HttpServletRequest request){
        Map<String, String> usernameAndPasswordMap = getUsernameAndPasswordBySession(request);
        // if (usernameAndPasswordMap == null){
        //     // not session, please log in
        //     return queryResponse(false, USERNAME_AND_PASSWORD_HAVE_NOT_BEEN_VERIFIED);
        // }
        List<String> hashList = getHashListByUserInformation(usernameAndPasswordMap.get("username12306"));
        if (hashList == null){
            return queryResponse(true, SUCCESS, TaskStatusName.WAIT, new ArrayList<>());
        }
        return encapsulationsQueryResponse(hashList, TaskStatusName.WAIT);
    }

    /**
     * get the user task by status is running
     * @param request   request
     * @return          QueryResponse
     */
    @GetMapping(value = "getInformationByStatusRunning", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public QueryResponse getInformationByStatusRunning(HttpServletRequest request){
        Map<String, String> usernameAndPasswordMap = getUsernameAndPasswordBySession(request);
        if (usernameAndPasswordMap == null){
            // not session, please log in
            return queryResponse(false, USERNAME_AND_PASSWORD_HAVE_NOT_BEEN_VERIFIED);
        }
        List<String> hashList = getHashListByUserInformation(usernameAndPasswordMap.get("username12306"));
        if (hashList == null){
            return queryResponse(true, SUCCESS, TaskStatusName.RUNNING, new ArrayList<>());
        }
        return encapsulationsQueryResponse(hashList, TaskStatusName.RUNNING);
    }


    /**
     * get the user task by status is booking failed
     * @param request   request
     * @return          QueryResponse
     */
    @GetMapping(value = "getInformationByStatusBookingFailed", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public QueryResponse getInformationByStatusBookingFailed(HttpServletRequest request){
        Map<String, String> usernameAndPasswordMap = getUsernameAndPasswordBySession(request);
        if (usernameAndPasswordMap == null){
            // not session, please log in
            return queryResponse(false, USERNAME_AND_PASSWORD_HAVE_NOT_BEEN_VERIFIED);
        }
        List<String> hashList = getHashListByUserInformation(usernameAndPasswordMap.get("username12306"));
        if (hashList == null){
            return queryResponse(true, SUCCESS, TaskStatusName.BOOKING_FAILED, new ArrayList<>());
        }
        return encapsulationsQueryResponse(hashList, TaskStatusName.BOOKING_FAILED);
    }

    /**
     * get the user task by status is booking success but send notification failed.
     * @param request   request
     * @return          QueryResponse
     */
    @GetMapping(value = "getInformationByStatusBookingSuccessButNotificationFailed", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public QueryResponse getInformationByStatusBookingSuccessButNotificationFailed(HttpServletRequest request){
        Map<String, String> usernameAndPasswordMap = getUsernameAndPasswordBySession(request);
        if (usernameAndPasswordMap == null){
            // not session, please log in
            return queryResponse(false, USERNAME_AND_PASSWORD_HAVE_NOT_BEEN_VERIFIED);
        }
        List<String> hashList = getHashListByUserInformation(usernameAndPasswordMap.get("username12306"));
        if (hashList == null){
            return queryResponse(true, SUCCESS, TaskStatusName.BOOKING_SUCCEED_BUT_SEND_NOTIFICATION_FAILED, new ArrayList<>());
        }
        return encapsulationsQueryResponse(hashList, TaskStatusName.BOOKING_SUCCEED_BUT_SEND_NOTIFICATION_FAILED);
    }

    /**
     * get the user task by status is completed
     * @param request   request
     * @return          QueryResponse
     */
    @GetMapping(value = "getInformationByStatusCompleted", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public QueryResponse getInformationByStatusCompleted(HttpServletRequest request){
        Map<String, String> usernameAndPasswordMap = getUsernameAndPasswordBySession(request);
        // Authentication
        if (usernameAndPasswordMap == null){
            // not session, please log in
            return queryResponse(false, USERNAME_AND_PASSWORD_HAVE_NOT_BEEN_VERIFIED);
        }
        List<String> hashList = getHashListByUserInformation(usernameAndPasswordMap.get("username12306"));
        if (hashList == null){
            return queryResponse(true, SUCCESS, TaskStatusName.COMPLETED, new ArrayList<>());
        }
        return encapsulationsQueryResponse(hashList, TaskStatusName.COMPLETED);
    }


    /**
     * get user all task all information
     * @return              AllInformationResponse
     */
    @GetMapping(value = "getUserAllTask", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public QueryResponse getUserAllTask(HttpServletRequest request){
        Map<String, String> usernameAndPasswordMap = getUsernameAndPasswordBySession(request);
        if (usernameAndPasswordMap == null){
            // not session, please log in
            return queryResponse(false, USERNAME_AND_PASSWORD_HAVE_NOT_BEEN_VERIFIED);
        }
        List<String> hashList = getHashListByUserInformation(usernameAndPasswordMap.get("username12306"));
        if (hashList == null){
            return queryResponse(true, SUCCESS, new ArrayList<>());
        }
        return encapsulationsQueryResponse(hashList);
    }
    /**
     * get grab ticket information and notify information by hash
     * @param queryRequest  QueryRequest
     * @param request       request
     * @return              QueryResponse
     */
    @GetMapping(value = "getGrabTicketAndNotificationInformationByHash", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public QueryResponse getGrabTicketAndNotifyInformationByHash(QueryRequest queryRequest, HttpServletRequest request){
        Map<String, String> usernameAndPasswordMap = getUsernameAndPasswordBySession(request);
        if (usernameAndPasswordMap == null){
            // not session, please log in
            return queryResponse(false, USERNAME_AND_PASSWORD_HAVE_NOT_BEEN_VERIFIED);
        }
        List<String> hashList = new ArrayList<>();
        hashList.add(queryRequest.getHash());
        return encapsulationsQueryResponse(hashList);
    }
    /**
     * get the task that the user has successded
     * @param request   request
     * @return          QueryResponse
     */
    @GetMapping(value = "getStatusInformationTableListBySuccess", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public QueryResponse getStatusInformationTableListBySuccess(HttpServletRequest request){
        Map<String, String> usernameAndPasswordMap = getUsernameAndPasswordBySession(request);
        if (usernameAndPasswordMap == null){
            // not session, please log in
            return queryResponse(false, USERNAME_AND_PASSWORD_HAVE_NOT_BEEN_VERIFIED);
        }
        List<String> hashList = getHashListByUserInformation(usernameAndPasswordMap.get("username12306"));
        if (hashList == null){
            return queryResponse(true, SUCCESS, new ArrayList<>());
        }
        List<TaskStatusName> successList = Arrays.asList(
                TaskStatusName.BOOKING_SUCCEED_BUT_SEND_NOTIFICATION_FAILED,
                TaskStatusName.COMPLETED
        );
        return encapsulationsQueryResponse(hashList, successList);
    }
    /**
     * get the user failed task
     * @param request   request
     * @return          QueryResponse
     */
    @GetMapping(value = "getStatusInformationTableListByFailed", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public QueryResponse getStatusInformationTableListByFailed(HttpServletRequest request){
        Map<String, String> usernameAndPasswordMap = getUsernameAndPasswordBySession(request);
        if (usernameAndPasswordMap == null){
            // not session, please log in
            return queryResponse(false, USERNAME_AND_PASSWORD_HAVE_NOT_BEEN_VERIFIED);
        }
        List<String> hashList = getHashListByUserInformation(usernameAndPasswordMap.get("username12306"));
        if (hashList == null){
            return queryResponse(true, SUCCESS, new ArrayList<>());
        }
        return encapsulationsQueryResponse(hashList, TaskStatusName.BOOKING_FAILED);
    }

    private QueryResponse queryResponse(Boolean status, String message, String taskStatusName, List<?> resultList){
        QueryResponse queryResponse = new QueryResponse();
        queryResponse.setStatus(status);
        queryResponse.setHttpStatus(HTTP_SUCCESS);
        queryResponse.setMessage(message);
        queryResponse.setTaskStatus(taskStatusName);
        queryResponse.setResult(resultList);
        return queryResponse;

    }
    private QueryResponse queryResponse(Boolean status, String message, TaskStatusName taskStatusName, List<?> resultList){
        QueryResponse queryResponse = new QueryResponse();
        queryResponse.setStatus(status);
        queryResponse.setHttpStatus(HTTP_SUCCESS);
        queryResponse.setMessage(message);
        queryResponse.setTaskStatus(taskStatusName.getTaskStatusName());
        queryResponse.setResult(resultList);
        return queryResponse;

    }
    private QueryResponse queryResponse(Boolean status, String message, List<?> resultList){
        QueryResponse queryResponse = new QueryResponse();
        queryResponse.setStatus(status);
        queryResponse.setHttpStatus(HTTP_SUCCESS);
        queryResponse.setMessage(message);
        queryResponse.setTaskStatus(null);
        queryResponse.setResult(resultList);
        return queryResponse;

    }
    private QueryResponse queryResponse(Boolean status, String message, TaskStatusName taskStatusName){
        QueryResponse queryResponse = new QueryResponse();
        queryResponse.setStatus(status);
        queryResponse.setHttpStatus(HTTP_SUCCESS);
        queryResponse.setMessage(message);
        queryResponse.setTaskStatus(taskStatusName.getTaskStatusName());
        queryResponse.setResult(null);
        return queryResponse;

    }
    private QueryResponse queryResponse(Boolean status, String message){
        QueryResponse queryResponse = new QueryResponse();
        queryResponse.setStatus(status);
        queryResponse.setHttpStatus(HTTP_SUCCESS);
        queryResponse.setMessage(message);
        queryResponse.setTaskStatus(null);
        queryResponse.setResult(null);
        return queryResponse;
    }

    /**
     * encapsulation AllInformationEntity by hashList
     * @param hashList          hashList
     * @param taskStatusName    taskStatusName
     * @return                  QueryResponse
     */
    private QueryResponse encapsulationsQueryResponse(@NonNull List<String> hashList, @NotNull TaskStatusName taskStatusName){
        hashList = filterHashListByTaskStatusName(hashList, taskStatusName);
        List<AllInformationEntity> allInformationEntityList = getAllInformationEntityList(hashList);
        if (allInformationEntityList.size() == 0){
            return queryResponse(true, SUCCESS, taskStatusName, new ArrayList<>());
        }
        return queryResponse(true, SUCCESS, taskStatusName, allInformationEntityList);
    }
    /**
     * encapsulation AllInformationEntity by hashList and taskStatusNameList
     * @param hashList              hashList
     * @param taskStatusNameList    taskStatusNameList
     * @return                      QueryResponse
     */
    private QueryResponse encapsulationsQueryResponse(@NonNull List<String> hashList, @NotNull List<TaskStatusName> taskStatusNameList){
        hashList = filterHashListByTaskStatusName(hashList, taskStatusNameList);
        List<AllInformationEntity> allInformationEntityList = getAllInformationEntityList(hashList);
        if (allInformationEntityList.size() == 0){
            return queryResponse(true, SUCCESS, Joiner.on(",").join(taskStatusNameList), new ArrayList<>());
        }
        return queryResponse(true, SUCCESS, Joiner.on(",").join(taskStatusNameList), allInformationEntityList);
    }

    /**
     * encapsulation AllInformationEntity by hashList
     * @param hashList  hashList
     * @return          QueryResponse
     */
    private QueryResponse encapsulationsQueryResponse(@NonNull List<String> hashList){
        List<AllInformationEntity> allInformationEntityList = getAllInformationEntityList(hashList);
        if (allInformationEntityList.size() == 0){
            return queryResponse(true, SUCCESS, new ArrayList<>());
        }
        return queryResponse(true, SUCCESS,  allInformationEntityList);
    }


    /**
     * get hash list by user information.
     * input username12306, then use to it get this user all task hash,
     * return a list result.
     * @param username12306     username12306
     * @return                  List<String>
     */
    private List<String> getHashListByUserInformation(@NonNull String username12306) {
        List<UserInformationEntity> userInformationEntityList = userInformationMapper.getEntityByUsername(username12306);
        if (userInformationEntityList != null){
            List<String> hashList = new ArrayList<>();
            for (UserInformationEntity userInformationEntity: userInformationEntityList){
                hashList.add(userInformationEntity.getHash());
            }
            return hashList;
        }
        return null;
    }

    /**
     * get AllInformationEntity list
     * @param hashList  hashList
     * @return          List<AllInformationEntity>
     */
    private List<AllInformationEntity> getAllInformationEntityList(@NonNull List<String> hashList){
        List<AllInformationEntity> allInformationEntityList = new ArrayList<>();
        for (String hash: hashList){
            AllInformationEntity allInformationEntity = allInformationMapper.getAllInformationEntityByHash(hash);
            if (allInformationEntity != null){
                allInformationEntityList.add(allInformationEntity);
            }
        }
        return allInformationEntityList;
    }
    /**
     * filter hash list by task status
     * @param hashList          hashList
     * @param taskStatusName    taskStatusName
     * @return                  List<String>
     */
    private List<String> filterHashListByTaskStatusName(List<String> hashList, TaskStatusName taskStatusName){
        List<String> newHashList = new ArrayList<>();
        for (String hash: hashList){
            StatusInformationEntity statusInformationEntity = statusInformationMapper.getStatusInformationByHashAndStatus(hash, taskStatusName.getTaskStatusName());
            if (statusInformationEntity != null){
                newHashList.add(hash);
            }
        }
        return newHashList;
    }
    /**
     * filter hash list by task status list
     * @param hashList              hashList
     * @param taskStatusNameList    taskStatusNameList
     * @return                      List<String>
     */
    private List<String> filterHashListByTaskStatusName(List<String> hashList, List<TaskStatusName> taskStatusNameList){
        List<String> newHashList = new ArrayList<>();
        for (TaskStatusName taskStatusName: taskStatusNameList){
            for (String hash: hashList){
                StatusInformationEntity statusInformationEntity = statusInformationMapper.getStatusInformationByHashAndStatus(hash, taskStatusName.getTaskStatusName());
                if (statusInformationEntity != null){
                    newHashList.add(hash);
                }
            }
        }
        return newHashList;
    }
}
