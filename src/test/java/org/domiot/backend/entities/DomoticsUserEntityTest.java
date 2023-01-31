package org.domiot.backend.entities;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;
import org.domiot.backend.entities.DomoticsUserEntity;
import org.domiot.backend.entities.SiteEntity;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Test for {@link DomoticsUserEntity}.
 */
@ExtendWith(MockitoExtension.class)
class DomoticsUserEntityTest
{
   private EntityManagerFactory entityManagerFactory;

   private              EntityManager entityManager;
   private static final String        PERSISTENCE_UNIT = "measurements-pu";


   @BeforeEach
   public void testSetup()
   {
      entityManagerFactory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
      entityManager = entityManagerFactory.createEntityManager();
   }


   @Test
   void test()
   {
      SiteEntity siteEntity = new SiteEntity("site description");
      DomoticsUserEntity domoticsUserEntity = new DomoticsUserEntity("user1", siteEntity);
      siteEntity.setUserList(Arrays.asList(domoticsUserEntity));

      entityManager.getTransaction().begin();
      entityManager.persist(siteEntity);
      entityManager.persist(domoticsUserEntity);
      entityManager.getTransaction().commit();

      TypedQuery<DomoticsUserEntity> query = entityManager.createQuery("SELECT u FROM users u", DomoticsUserEntity.class);
      List<DomoticsUserEntity> userList = query.getResultList();

      assertThat(userList.size(), is(1));
      System.out.println(userList);
   }


   @AfterEach
   public void doCleanup()
   {
      entityManager.close();
      entityManagerFactory.close();
   }
}

