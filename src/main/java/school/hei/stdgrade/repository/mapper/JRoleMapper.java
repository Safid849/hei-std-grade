package school.hei.stdgrade.repository.mapper;

import org.springframework.stereotype.Component;
import school.hei.stdgrade.model.Role;
import school.hei.stdgrade.model.RoleName;
import school.hei.stdgrade.repository.model.JRole;

@Component
public class JRoleMapper {
    public Role toDomain(JRole entity) {
        return new Role(entity.getId(), RoleName.valueOf(entity.getName()));
    }
}