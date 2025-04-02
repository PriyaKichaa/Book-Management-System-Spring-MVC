package com.cts.lms.utils;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;

@Component
public class ResetBookId {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void resetAutoIncrement(){
        entityManager.createNativeQuery("ALTER TABLE book AUTO_INCREMENT = 1").executeUpdate();
    }
}