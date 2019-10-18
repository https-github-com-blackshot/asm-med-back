package kz.beeset.med.gateway2.converter;

import kz.beeset.med.gateway2.converter.converter.ConverterFactory;
import kz.beeset.med.gateway2.dto.UserDTO;
import kz.beeset.med.admin.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ConverterFacade {
    @Autowired
    private ConverterFactory converterFactory;

    public User convert(final UserDTO dto) {
        return (User) converterFactory.getConverter(dto.getClass()).convert(dto);
    }
}
