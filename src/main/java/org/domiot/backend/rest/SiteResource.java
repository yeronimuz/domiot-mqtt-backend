package org.domiot.backend.rest;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.WebApplicationException;
import org.domiot.backend.exception.SiteException;
import org.domiot.backend.service.SiteService;
import org.lankheet.domiot.api.SiteApi;
import org.lankheet.domiot.model.Site;

/**
 * REST resource for managing Domiot {@link Site}s
 */
public class SiteResource implements SiteApi {

  @Inject
  private SiteService siteService;

  @Override
  public Site addSite(@Valid Site site) {
    try {
      return siteService.addSite(site);
    } catch (SiteException e) {
      throw new WebApplicationException(e);
    }
  }

  @Override
  public Site getSite(Long siteId) {
    return siteService.getSite(siteId);
  }

  @Override
  public Site updateSite(Long siteId, @Valid Site updatedSite) {
    return siteService.updateSite(updatedSite);
  }
}

