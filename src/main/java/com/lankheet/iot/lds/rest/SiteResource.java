package com.lankheet.iot.lds.rest;

import com.lankheet.iot.lds.exception.SiteException;
import com.lankheet.iot.lds.service.SiteService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.GenericEntity;
import jakarta.ws.rs.core.Response;
import org.lankheet.iot.model.Site;


@Path("/site")
@Api(
   value = "/", authorizations = {@Authorization(value = "Bearer", scopes = {})}
)
public class SiteResource
{
   @Inject
   private SiteService siteService;

   @POST
   @Consumes({"application/json"})
   @Produces({"application/json"})
   @ApiOperation(
      value = "Add a new site",
      notes = "",
      response = Site.class,
      authorizations = {@Authorization("Bearer")},
      tags = {"Site"}
   )
   @ApiResponses({@ApiResponse(
      code = 200,
      message = "Site saved",
      response = Site.class
   )})
   public Response addSite(@ApiParam("") @Valid Site site)
   {
      Site siteReturned = null;
      try
      {
         siteReturned = siteService.saveNewSite(site);
      }
      catch (SiteException e)
      {
         return Response.notModified().build();
      }
      GenericEntity<Site> entity = new GenericEntity<Site>(siteReturned){};
      return Response.ok(entity).build();
   }


   @GET
   @Path("/{siteId}")
   @Produces({"application/json"})
   @ApiOperation(
      value = "Get a site",
      notes = "",
      response = Site.class,
      responseContainer = "List",
      authorizations = {@Authorization("Bearer")},
      tags = {"Site"}
   )
   @ApiResponses({@ApiResponse(
      code = 200,
      message = "Site content",
      response = Site.class,
      responseContainer = "List"
   )})
   public Site getSite(@PathParam("siteId") Long siteId)
   {
      return siteService.getSite(siteId);
   }


   @PUT
   @Path("/{siteId}")
   @Consumes({"application/json"})
   @Produces({"application/json"})
   @ApiOperation(
      value = "Update a site",
      notes = "",
      response = Site.class,
      responseContainer = "List",
      authorizations = {@Authorization("Bearer")},
      tags = {"Site"}
   )
   @ApiResponses({@ApiResponse(
      code = 200,
      message = "Site",
      response = Site.class,
      responseContainer = "List"
   )})
   public Site updateSite(@PathParam("siteId") Long siteId, @ApiParam("Site") @Valid Site site)
   {
      return null;
   }
}

