package com.naonao.grab12306ticket.version.springboot.web.task;

import com.alibaba.fastjson.JSONObject;
import com.naonao.grab12306ticket.version.springboot.entity.response.DeleteResponse;
import com.naonao.grab12306ticket.version.springboot.web.base.AbstractDelete;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @program: SpringBoot
 * @description:
 * @author: Wen lyuzhao
 * @create: 2019-05-15 04:07
 **/
@RestController
@RequestMapping("/api/v1/task")
@CrossOrigin
public class Delete extends AbstractDelete {


    /***
     * delete task by hash
     * input data format is json
     * example:
     * {"hash":"1111111111111111111111111"}
     *
     * @param inputData     inputData
     * @return              DeleteResponse
     */
    @DeleteMapping(value = "delete", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public DeleteResponse deleteTaskByHash(@RequestBody String inputData, HttpServletRequest request){
        if (!authentication(request)){
            return deleteResponse(false, USERNAME_AND_PASSWORD_HAVE_NOT_BEEN_VERIFIED);
        }
        JSONObject jsonObject = JSONObject.parseObject(inputData);
        String hash = jsonObject.getString("hash");
        allInformationMapper.deleteAllByHash(hash);
        DeleteResponse deleteResponse = new DeleteResponse();
        deleteResponse.setStatus(true);
        deleteResponse.setHttpStatus(HTTP_SUCCESS);
        deleteResponse.setMessage(SUCCESS);
        return deleteResponse;
    }

    private DeleteResponse deleteResponse(Boolean status, String message){
        DeleteResponse deleteResponse = new DeleteResponse();
        deleteResponse.setStatus(status);
        deleteResponse.setHttpStatus(HTTP_SUCCESS);
        deleteResponse.setMessage(message);
        return deleteResponse;
    }
}
