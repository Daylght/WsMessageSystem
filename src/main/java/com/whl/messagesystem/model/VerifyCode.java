package com.whl.messagesystem.model;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author whl
 * @date 2021/12/8 16:22
 */
@Data
@NoArgsConstructor
public class VerifyCode {
    private String code;
    private byte[] imgBytes;
    private long expireTime;
}
