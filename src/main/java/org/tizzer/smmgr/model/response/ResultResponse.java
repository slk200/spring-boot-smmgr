package org.tizzer.smmgr.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "resultResponse", description = "返回对象")
public class ResultResponse {
    @ApiModelProperty(value = "状态码")
    private Integer code;
    @ApiModelProperty(value = "响应信息")
    private String message;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
