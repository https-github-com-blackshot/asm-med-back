package kz.beeset.med.gateway2.dto;

import kz.beeset.med.admin.model.User;

import java.io.Serializable;


public class TokenDTO implements Serializable {

    private static final long serialVersionUID = 6710061358371752955L;

    private String token;
    private User user;
    private String selectedOrganizationId;

    public TokenDTO() {
    }

    public String getToken() {
        return token;
    }

    public void setToken(final String token) {
        this.token = token;
    }

    public String getSelectedOrganizationId() {
        return selectedOrganizationId;
    }

    public void setSelectedOrganizationId(String selectedOrganizationId) {
        this.selectedOrganizationId = selectedOrganizationId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
