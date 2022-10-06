package com.springproject.photosharing.repo;

import com.springproject.photosharing.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepo extends JpaRepository<Role, Long> {
  public Role findRoleByName(String name);
}
