package kz.beeset.med.dmp.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.repository.query.MongoEntityInformation;
import org.springframework.data.mongodb.repository.support.SimpleMongoRepository;

import java.io.Serializable;
import java.util.List;

public class ResourceUtilRepositoryImpl<T, I extends Serializable> extends SimpleMongoRepository<T, I> implements ResourceUtilRepository<T, I> {

    private MongoOperations mongoOperations;
    private MongoEntityInformation entityInformation;

    public ResourceUtilRepositoryImpl(final MongoEntityInformation entityInformation, final MongoOperations mongoOperations) {
        super(entityInformation, mongoOperations);

        this.entityInformation = entityInformation;
        this.mongoOperations = mongoOperations;
    }

    @Override
    public Page<T> findAll(final Query query, final Pageable pageable) {

        return new PageImpl<T>(
                mongoOperations.find(query.with(pageable), entityInformation.getJavaType(), entityInformation.getCollectionName()),
                pageable,
                mongoOperations.count(query, entityInformation.getJavaType(), entityInformation.getCollectionName())
        );
    }

    @Override
    public List<T> findAll(final Query query) {

        return mongoOperations.find(query, entityInformation.getJavaType(), entityInformation.getCollectionName());
    }
}

