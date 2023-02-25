package org.domiot.backend.mapper;

import org.lankheet.domiot.entities.SiteEntity;
import org.lankheet.domiot.model.Site;
import org.mapstruct.Mapper;

@Mapper
public interface SiteMapper {

  Site mapSiteEntityToSite(SiteEntity siteEntity);

  SiteEntity mapSiteToSiteEntity(Site site);
}
