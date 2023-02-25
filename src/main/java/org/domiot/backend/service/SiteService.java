package org.domiot.backend.service;

import jakarta.inject.Inject;
import org.domiot.backend.constants.Errors;
import org.domiot.backend.dao.SiteDao;
import org.domiot.backend.exception.SiteException;
import org.domiot.backend.mapper.SiteMapper;
import org.lankheet.domiot.model.Permission;
import org.lankheet.domiot.model.Site;

/**
 * Service layer (business logic) between REST resource and DAO
 */
public class SiteService {

  @Inject
  private SiteDao siteDao;
  @Inject
  private SiteMapper siteMapper;

  private Site saveNewSite(Site site)
      throws SiteException {
    // Validate that there is at least one user with admin rights
    if (site.getUsers() == null || !siteHasAdminUser(site)) {
      throw new SiteException(Errors.ADMIN_USER_MISSING);
    }

    if (siteDao.get(site.getSiteId()).isPresent()) {
      // TODO: Or do we just overwrite the Site?
      throw new SiteException(Errors.SITE_EXISTS);
    }

    return siteMapper.mapSiteEntityToSite(siteDao.save(siteMapper.mapSiteToSiteEntity(site)));
  }

  private boolean siteHasAdminUser(Site site) {
    return site.getUsers().stream()
        .anyMatch(user -> user.getRole().getPermissions().stream().anyMatch(permission -> permission.equals(
            Permission.ADMIN)));
  }

  /**
   * Get one specific Site
   *
   * @param siteId The sequence of the Site
   */
  public Site getSite(Long siteId) {
    return siteMapper.mapSiteEntityToSite(siteDao.get(siteId).orElse(null));
  }

  /**
   * Add a new site
   *
   * @param site The new site to add
   * @return The saved site
   * @throws SiteException No admin user or site already exists
   */
  public Site addSite(Site site) throws SiteException {
    return this.saveNewSite(site);
  }

  /**
   * Update a site
   * @param updatedSite The site to update
   * @return
   */
  public Site updateSite(Site updatedSite) {
    return siteMapper.mapSiteEntityToSite(siteDao.update(siteMapper.mapSiteToSiteEntity(updatedSite)));
  }
}
