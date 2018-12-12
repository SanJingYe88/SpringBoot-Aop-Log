package it.com.aop.mongo;

import it.com.aop.entity.OperateLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class MonggoDao {

    @Autowired
    private MongoTemplate mongoTemplate;

    public void insert(OperateLog operateLog){
        mongoTemplate.insert(operateLog);
    }
}
