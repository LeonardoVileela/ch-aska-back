package project.api.rest.domain.user;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

@Repository
@RepositoryRestResource(exported = true)
public interface AppUserRepository extends CrudRepository<AppUser, Long> {

	AppUser findByUsername(String username);
}
