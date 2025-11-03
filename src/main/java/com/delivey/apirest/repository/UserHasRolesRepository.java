package com.delivey.apirest.repository;

import com.delivey.apirest.models.UserHasRoles;
import com.delivey.apirest.models.id.UserRoleId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserHasRolesRepository extends JpaRepository<UserHasRoles, UserRoleId> {
}
