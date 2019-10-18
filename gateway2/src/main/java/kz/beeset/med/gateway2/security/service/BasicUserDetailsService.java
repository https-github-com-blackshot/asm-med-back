package kz.beeset.med.gateway2.security.service;

import kz.beeset.med.admin.model.User;
import kz.beeset.med.gateway2.exception.model.ServiceException;
import kz.beeset.med.gateway2.service.UsersService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Primary
@Service
public class BasicUserDetailsService implements UserDetailsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BasicUserDetailsService.class);

    private UsersService usersService;


    @Autowired
    public BasicUserDetailsService(UsersService usersService){
        this.usersService = usersService;

    }

    @Override
    public UserDetails loadUserByUsername(String idnOrMobilePhoneOrUsernameOrEmail) throws UsernameNotFoundException {

        LOGGER.info("idnOrMobilePhoneOrUsernameOrEmail: " + idnOrMobilePhoneOrUsernameOrEmail);

        User user = usersService.findUserByIdnOrMobilePhoneOrUsernameOrEmail(idnOrMobilePhoneOrUsernameOrEmail);

        if (user != null) {

            if (user.isActive()) {
                Set<GrantedAuthority> grantedAuthorities = new HashSet<>();

                if(user.getIdn() != null && user.getPassword() != null){

                    return new org.springframework.security.core.userdetails.User(user.getIdn(), user.getPassword(), grantedAuthorities);

                }else{

                    throw new ServiceException("User with idn or password is empty");

                }

            } else {
                throw new UsernameNotFoundException("User with username:" + idnOrMobilePhoneOrUsernameOrEmail + " is not active");
            }
        } else {
            throw new UsernameNotFoundException("User with username:" + idnOrMobilePhoneOrUsernameOrEmail + " not found");
        }

    }
}
