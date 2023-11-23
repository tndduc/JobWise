package com.jobwise.spring.service;

import com.jobwise.spring.exception.ResourceNotFoundException;
import com.jobwise.spring.model.ERole;
import com.jobwise.spring.model.Role;
import com.jobwise.spring.repository.RoleRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
/**
 * @author DucTN
 * @project JobWise-main
 * @on 11/11/2023
 */
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
