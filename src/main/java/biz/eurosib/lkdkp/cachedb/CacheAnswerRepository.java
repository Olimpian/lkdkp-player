package biz.eurosib.lkdkp.cachedb;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CacheAnswerRepository extends CrudRepository<CacheAnswer, Integer> {
    List<CacheAnswer> findByRequest(String request);
}
