package encyclopediaApp.Service;

import encyclopediaApp.Model.Role;
import encyclopediaApp.Repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RoleService {
    @Autowired
    RoleRepository roleRepository;

    public Optional<Role> findRoleByName(String name) {
        return roleRepository.findByName(name);
    }

    public Optional<Role> findRoleById(int id) {
        return roleRepository.findById(id);
    }
}
