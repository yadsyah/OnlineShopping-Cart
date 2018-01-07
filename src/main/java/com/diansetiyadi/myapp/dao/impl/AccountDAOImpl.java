package com.diansetiyadi.myapp.dao.impl;

import com.diansetiyadi.myapp.dao.AccountDAO;
import com.diansetiyadi.myapp.entity.AccountsEntity;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.CriteriaQuery;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class AccountDAOImpl implements AccountDAO {

    @Autowired
    private SessionFactory sessionFactory;

    public AccountsEntity findAccount(String name) {
       Session session = sessionFactory.getCurrentSession();
        Criteria criteria  = session.createCriteria(AccountsEntity.class);
        criteria.add(Restrictions.eq("userName",name));
        return (AccountsEntity) criteria.uniqueResult();
    }

}
