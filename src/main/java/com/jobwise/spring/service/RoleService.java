package com.jobwise.spring.service;

import com.jobwise.spring.exception.ResourceNotFoundException;
import com.jobwise.spring.model.ERole;
import com.jobwise.spring.model.Role;
import com.jobwise.spring.repository.RoleRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RoleService {

    final RoleRepository roleRepository;

    public Role findByName(ERole name) throws ResourceNotFoundException {
        return roleRepository
                .findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("Role is not found."));
    }

}
