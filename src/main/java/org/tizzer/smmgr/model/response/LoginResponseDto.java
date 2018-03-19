package org.tizzer.smmgr.model.response;

public class LoginResponseDto extends ResultResponse {
    private Boolean admin;

    public Boolean isAdmin() {
        return admin;
    }

    public void setAdmin(Boolean admin) {
        this.admin = admin;
    }
}
