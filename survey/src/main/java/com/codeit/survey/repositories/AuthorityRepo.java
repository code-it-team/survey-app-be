package com.codeit.survey.repositories;

import com.codeit.survey.entities.Authority;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface AuthorityRepo extends CrudRepository<Authority, Integer> {
    Authority findAuthorityByRole(String role);
}
