/*******************************************************************************
 * Educational Online Test Delivery System 
 * Copyright (c) 2014 American Institutes for Research
 *   
 * Distributed under the AIR Open Source License, Version 1.0 
 * See accompanying file AIR-License-1_0.txt or at
 * http://www.smarterapp.org/documents/American_Institutes_for_Research_Open_Source_Software_License.pdf
 ******************************************************************************/
package org.air.standard.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.air.standard.common.Branding;
import org.opentestsystem.shared.progman.client.domain.Tenant;
import org.opentestsystem.shared.security.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author mpatel
 *
 */
@Scope("session")
@Controller
public class BrandingImageController extends AbstractController
{
  private static final Logger _logger = LoggerFactory.getLogger (BrandingImageController.class);
  
  @Autowired
  private UserService _userService;
  
  @SuppressWarnings ("unchecked")
  @RequestMapping(method=RequestMethod.GET, value = "getBranding")
  @ResponseBody
  public Branding getBrandingImageUrl(HttpServletRequest request) throws Exception{
  //Path for the default SBAC Image
    String logoImageURL = request.getContextPath () + "/images/logo_sbac.png";
    String logoImageTitle = "Smarter Balanced Assessment Consortium";
    try {
      String tenantId = "";
      List<Tenant> tenantsList = _userService.getUniqueTenantsForUser ().getTenants ();
      //If Only one tenant associated than get the image from progman and display it else display the default sbac image.
      if(tenantsList!=null && tenantsList.size ()==1){
         Tenant tenant  = tenantsList.get (0);
         tenantId = tenant.getId ();
 
         Map<String, Object> assets = _userService.getAssetsForTenant(tenantId);
         if(assets!=null && !assets.isEmpty ()) {
            List<Map<String,String>> assetsList = (List<Map<String,String>>)assets.get("assets");
            for(Map<String,String> asset:assetsList) {
              if(asset.get ("name").equals ("logo")) {
                logoImageURL = asset.get ("url");
              } else if(asset.get ("name").equalsIgnoreCase ("title" )) {
                logoImageTitle = asset.get ("url");
              }
            }
          }
      }
    } catch (Exception e) {
      _logger.error (e.toString (),e);
    }
    Branding branding = new Branding (logoImageURL, logoImageTitle);
    return branding;
  }
}
