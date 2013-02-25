package org.springframework.data.simpledb.repository.support;

import java.io.Serializable;

import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;
import org.springframework.data.repository.query.QueryLookupStrategy;
import org.springframework.data.simpledb.config.SimpleDbConfig;
import org.springframework.data.simpledb.core.SimpleDbOperations;
import org.springframework.data.simpledb.core.SimpleDbOperationsImpl;
import org.springframework.data.simpledb.core.domain.DomainManager;
import org.springframework.data.simpledb.query.SimpleDbQueryLookupStrategy;
import org.springframework.data.simpledb.repository.support.entityinformation.SimpleDbEntityInformation;
import org.springframework.data.simpledb.repository.support.entityinformation.SimpleDbEntityInformationSupport;

import com.amazonaws.services.simpledb.AmazonSimpleDB;

/**
 * SimpleDB specific generic repository factory.
 *
 * See JpaRepositoryFactory
 */
public class SimpleDbRepositoryFactory extends RepositoryFactorySupport {

    private SimpleDbOperations<?, Serializable> simpledbOperations;
    private DomainManager domainManager;

    public SimpleDbRepositoryFactory(AmazonSimpleDB sdb, SimpleDbConfig config) {
        this.domainManager = new DomainManager(sdb, config.getDomainManagementPolicy());

        this.simpledbOperations = new SimpleDbOperationsImpl(sdb);

    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.repository.core.support.RepositoryFactorySupport#getTargetRepository(org.springframework.data.repository.core.RepositoryMetadata)
     */
    @Override
    protected Object getTargetRepository(RepositoryMetadata metadata) {
        SimpleDbEntityInformation<?, Serializable> entityInformation = getEntityInformation(metadata.getDomainType());

        domainManager.manageDomain(entityInformation.getDomain());

        SimpleDbRepositoryImpl<?, ?> repo = new SimpleDbRepositoryImpl(entityInformation, simpledbOperations);

        return repo;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.springframework.data.repository.support.RepositoryFactorySupport#
     * getRepositoryBaseClass()
     */
    @Override
    protected Class<?> getRepositoryBaseClass(RepositoryMetadata metadata) {
        return SimpleDbRepositoryImpl.class;
    }


    /*
     * (non-Javadoc)
     *
     * @see
     * org.springframework.data.repository.support.RepositoryFactorySupport#
     * getQueryLookupStrategy
     * (org.springframework.data.repository.query.QueryLookupStrategy.Key)
     */
    @Override
    protected QueryLookupStrategy getQueryLookupStrategy(QueryLookupStrategy.Key key) {
        return SimpleDbQueryLookupStrategy.create(simpledbOperations, key);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.springframework.data.repository.support.RepositoryFactorySupport#
     * getEntityInformation(java.lang.Class)
     */
    @Override
    @SuppressWarnings("unchecked")
    public <T, ID extends Serializable> SimpleDbEntityInformation<T, ID> getEntityInformation(Class<T> domainClass) {

        return (SimpleDbEntityInformation<T, ID>) SimpleDbEntityInformationSupport.getMetadata(domainClass);
    }
}
