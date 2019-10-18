package kz.beeset.med.admin.model;

import kz.beeset.med.admin.service.impl.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class UserAuthentication implements Authentication {

    private static final long serialVersionUID = -7170337143687707450L;

    private final User user;
    private boolean authenticated = true;
    private String token;

//    private final UsersService usersService;

    @Autowired
    public UserAuthentication(final User user, String token){
        this.user = user;
//        this.usersService = usersService;
        this.token = token;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return user.getAuthorities();
    }

    @Override
    public Object getCredentials() {
        return user.getPassword();
    }

    @Override
    public Object getDetails() {
        return user;
    }

    @Override
    public Object getPrincipal() {
        return user.getUsername();
    }

    @Override
    public boolean isAuthenticated() {
        return authenticated;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        this.authenticated = isAuthenticated;
    }

    @Override
    public String getName() {
        return user.getUsername();
    }

//    public kz.beeset.med.admin.model.User getUser() {
//
//        final kz.beeset.med.admin.model.User applicationUser = usersService.findUserByIdn(user.getUsername());
//
//        return applicationUser;
//    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
