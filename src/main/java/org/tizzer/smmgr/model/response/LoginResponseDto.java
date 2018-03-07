package org.tizzer.smmgr.model.response;

public class LoginResponseDto extends ResultResponse {
    private boolean admin;

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }
}
