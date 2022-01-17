package com.whl.messagesystem.commons.utils;

import com.whl.messagesystem.commons.constant.ResultEnum;
import com.whl.messagesystem.model.Result;

/**
 * @author whl
 * @date 2021/12/7 16:53
 */
@SuppressWarnings("all")
public class ResultUtil {

    public static Result<Object> success(Object object) {
        Result<Object> result = new Result<>();
        result.setStatus(ResultEnum.SUCCESS.getStatus());
        result.setMessage(ResultEnum.SUCCESS.getMsg());
        result.setData(object);
        return result;
    }

    public static Result<Object> success() {
        return success(null);
    }

    public static Result<Object> error(Object object) {
        Result<Object> result = new Result<>();
        result.setStatus(ResultEnum.ERROR.getStatus());
        result.setMessage(ResultEnum.ERROR.getMsg());
        result.setData(object);
        return result;
    }

    private static Result error(Integer status, String msg) {
        Result result = new Result();
        result.setStatus(status);
        result.setMessage(msg);
        return result;
    }

    public static Result userExist() {
        Result<Object> result = new Result<>();
        result.setStatus(ResultEnum.USER_EXIST.getStatus());
        result.setMessage(ResultEnum.USER_EXIST.getMsg());
        result.setData(null);
        return result;
    }

    public static Result error() {
        return error(ResultEnum.ERROR.getStatus(), ResultEnum.ERROR.getMsg());
    }

}
