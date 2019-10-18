package kz.beeset.med.gateway2.converter.dto;

import kz.beeset.med.gateway2.dto.UserDTO;
import kz.beeset.med.admin.model.User;
import org.springframework.core.convert.converter.Converter;

public class UserDTOConverter implements Converter<UserDTO, User> {

    @Override
    public User convert(final UserDTO dto) {
        final User user = new User();

        user.setUsername(dto.getIdn());
        user.setIdn(dto.getIdn());
        user.setPassword(dto.getPassword());
        user.setEmail(dto.getEmail());
        user.setName(dto.getName());
        user.setSurname(dto.getSurname());
        user.setMiddlename(dto.getMiddlename());

        return user;
    }
}
