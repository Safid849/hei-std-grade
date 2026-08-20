package school.hei.stdgrade.service;

import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import school.hei.stdgrade.model.Role;
import school.hei.stdgrade.repository.JRoleRepository;
import school.hei.stdgrade.repository.mapper.JRoleMapper;

@Service
@AllArgsConstructor
public class RoleService {
    private final JRoleRepository jRepository;
    private final JRoleMapper jMapper;

    public List<Role> findAll() {
        return jRepository.findAll().stream().map(jMapper::toDomain).toList();
    }
}