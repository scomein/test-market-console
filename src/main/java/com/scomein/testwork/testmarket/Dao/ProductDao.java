package com.scomein.testwork.testmarket.Dao;

import com.scomein.testwork.testmarket.entity.Product;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by scome on 20.03.17.
 */

@Repository
@Transactional
public class ProductDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductDao.class);

    @Resource(name = "sessionFactory")
    private SessionFactory sessionFactory;

    @Transactional
    public List<Product> findAll(int count) {
        Transaction tx = sessionFactory.getCurrentSession().beginTransaction();
        try {
            List<Product> products = sessionFactory
                    .getCurrentSession()
                    .getNamedQuery("getAll")
                    .setFetchSize(count)
                    .getResultList();
            tx.commit();
            return products;
        } catch (Exception e) {
            tx.rollback();
            LOGGER.error("Some error occured while getting data from database:" + e.getMessage());
            return null;
        }
    }

    @Transactional
    public void save(Product product) {
        Transaction tx = sessionFactory.getCurrentSession().beginTransaction();
        try {
            Product fromDb = findEquals(product);
            if (fromDb == null) {
                sessionFactory.getCurrentSession().save(product);
            } else {
                fromDb.setCount(fromDb.getCount() + product.getCount());
                sessionFactory.getCurrentSession().save(fromDb);
            }
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            LOGGER.error("Some error occured while saving data to database:" + e.getMessage());
        }
    }

    @Transactional
    private Product findEquals(Product product) {
        try {
            return sessionFactory.getCurrentSession()
                    .createQuery(product.getQuery(sessionFactory.getCriteriaBuilder()))
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

}
