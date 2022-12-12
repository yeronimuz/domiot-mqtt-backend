package com.lankheet.iot.lds.service;

import com.lankheet.iot.lds.dao.SiteDao;
import com.lankheet.iot.lds.exception.SiteException;
import jakarta.inject.Inject;
import org.lankheet.iot.model.Site;

public class SiteService
{
   @Inject
   private SiteDao siteDao;


   public Site saveNewSite(Site site)
      throws SiteException
   {
      // Validate that there is at least one user with admin rights
      if (site.getUsers()== null && !siteHasAdminUser(site))
      {
         // TODO throw WebApplicationException
         return null;
      }
      if (siteDao.getSiteByName() != null)
      {
         // TODO throw new SiteException("Site already exists");
      }
      return siteDao.saveNewSite(site);
   }


   private boolean siteHasAdminUser(Site site)
   {
      return true; // site.getUsers().stream().filter(user -> user.getPermissions().stream().filter(per))
   }


   public Site getSite(Long siteId)
   {
      return siteDao.getSiteById(siteId);
   }
}
