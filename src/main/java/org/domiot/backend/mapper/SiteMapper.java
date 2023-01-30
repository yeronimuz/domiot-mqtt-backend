package org.domiot.backend.mapper;

import org.domiot.backend.entities.SiteEntity;
import org.lankheet.domiot.model.Site;
import org.mapstruct.Mapper;

@Mapper
public interface SiteMapper {

  Site mapSiteEntityToSite(SiteEntity siteEntity);

  SiteEntity mapSiteToSiteEntity(Site site);
}
