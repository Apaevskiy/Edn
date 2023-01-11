package tt.hashtranslator.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import tt.hashtranslator.models.Application;

@Repository
public interface AppRepository extends ReactiveMongoRepository<Application, String> {

}
