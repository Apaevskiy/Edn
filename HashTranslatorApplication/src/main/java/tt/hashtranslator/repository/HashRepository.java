package tt.hashtranslator.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import tt.hashtranslator.models.HashesRequest;

public interface HashRepository extends MongoRepository<HashesRequest, Long> {

}
