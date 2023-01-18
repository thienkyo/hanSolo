package com.hanSolo.kinhNguyen.response;

import com.hanSolo.kinhNguyen.models.KeyManagement;

public class KeyManagementResponse extends BaseResponse{
    private KeyManagement keyManagement;

    public KeyManagementResponse(KeyManagement keyManagement,String errorCode, String errorMessage) {
        super(errorCode, errorMessage);
        this.keyManagement = keyManagement;
    }

    public KeyManagement getKeyManagement() {
        return keyManagement;
    }

    public void setKeyManagement(KeyManagement keyManagement) {
        this.keyManagement = keyManagement;
    }
}
