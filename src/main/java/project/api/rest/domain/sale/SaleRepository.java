package project.api.rest.domain.sale;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

@Repository
@RepositoryRestResource(exported = false)
public interface SaleRepository extends PagingAndSortingRepository<Sale, Long>{
	

}
