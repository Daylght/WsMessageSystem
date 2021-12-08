package com.whl.messagesystem.commons.utils.verifyCode;

import com.whl.messagesystem.model.VerifyCode;

import java.io.IOException;
import java.io.OutputStream;

/**
 * @author whl
 * @date 2021/12/8 16:21
 */
public interface IVerifyCodeGen {
    /**
     * 生成字符串形式的验证码
     */
    String generate(int width, int height, OutputStream os) throws IOException;

    /**
     * 生成图片形式的验证码
     */
    VerifyCode generate(int width, int height) throws IOException;
}
