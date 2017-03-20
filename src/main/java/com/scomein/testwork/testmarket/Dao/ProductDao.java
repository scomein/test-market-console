package com.scomein.testwork.testmarket.Dao;

import com.scomein.testwork.testmarket.entity.Product;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by scome on 20.03.17.
 */

@Repository
@Transactional
public class ProductDao {

    @Resource(name = "sessionFactory")
    private SessionFactory sessionFactory;

    @Transactional
    public List<Product> findAll(int count) {
        Transaction tx = sessionFactory.getCurrentSession().beginTransaction();
        try {
            return sessionFactory
                    .getCurrentSession()
                    .getNamedQuery("getAll")
                    .setFetchSize(count)
                    .getResultList();
        } catch (Exception e) {
            tx.rollback();
            //log this
            throw e;
        } finally {
            tx.commit();
        }
    }

    @Transactional
    public void save(Product product) {
        Transaction tx = sessionFactory.getCurrentSession().beginTransaction();
        try {
            sessionFactory.getCurrentSession().save(product);
        } catch (Exception e) {
            tx.rollback();
            //log this
            throw e;
        } finally {
            tx.commit();
        }
    }
}
