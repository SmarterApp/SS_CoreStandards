/*******************************************************************************
 * Educational Online Test Delivery System 
 * Copyright (c) 2014 American Institutes for Research
 *   
 * Distributed under the AIR Open Source License, Version 1.0 
 * See accompanying file AIR-License-1_0.txt or at
 * http://www.smarterapp.org/documents/American_Institutes_for_Research_Open_Source_Software_License.pdf
 ******************************************************************************/
package org.air.standard.security;

import static org.opentestsystem.shared.security.oauth.resource.UserAttributeConstants.EMAIL_KEY;
import static org.opentestsystem.shared.security.oauth.resource.UserAttributeConstants.FIRST_NAME_KEY;
import static org.opentestsystem.shared.security.oauth.resource.UserAttributeConstants.FULL_NAME_KEY;
import static org.opentestsystem.shared.security.oauth.resource.UserAttributeConstants.LAST_NAME_KEY;
import static org.opentestsystem.shared.security.oauth.resource.UserAttributeConstants.PHONE_KEY;
import static org.opentestsystem.shared.security.oauth.resource.UserAttributeConstants.SBAC_TENANCY_CHAIN_KEY;
import static org.opentestsystem.shared.security.oauth.resource.UserAttributeConstants.SBAC_UUID_KEY;

import java.sql.SQLException;
import java.util.Map;

import org.air.standard.dao.AuthorizationDAO;
import org.apache.commons.lang.math.RandomUtils;
import org.opentestsystem.shared.security.domain.SbacUser;
import org.opentestsystem.shared.security.service.RolesAndPermissionsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.saml.SAMLCredential;

import com.google.common.collect.Maps;


/**
 * @author mpatel
 *
 */
public class UserDetailsServiceImpl extends org.opentestsystem.shared.security.service.UserDetailsServiceImpl 
{

  private static final Logger _logger = LoggerFactory.getLogger (UserDetailsServiceImpl.class);
  
  private static final int MAX_ERROR_CODE = 100000;


  @Autowired
  private RolesAndPermissionsService rolesAndPermissionsService;

  
  private Map<String, String> extractUserAttributes(final SAMLCredential samlCred) {
    final Map<String, String> userAttributes = Maps.newHashMap(SbacUser.BLANK_USER_ATTRIBS);
    populateUserAttribute(samlCred, userAttributes, SbacUser.EMAIL_KEY, EMAIL_KEY);
    populateUserAttribute(samlCred, userAttributes, SbacUser.FIRST_NAME_KEY, FIRST_NAME_KEY);
    populateUserAttribute(samlCred, userAttributes, SbacUser.LAST_NAME_KEY, LAST_NAME_KEY);
    populateUserAttribute(samlCred, userAttributes, SbacUser.FULL_NAME_KEY, FULL_NAME_KEY);
    populateUserAttribute(samlCred, userAttributes, SbacUser.USER_UNIQUE_ID_KEY, SBAC_UUID_KEY);
    populateUserAttribute(samlCred, userAttributes, SbacUser.PHONE_KEY, PHONE_KEY);

    return userAttributes;
}

  private void populateUserAttribute(final SAMLCredential samlCred, final Map<String, String> userAttributes, final String inAttrKey, final String samlAttrKey) {
      final String attribute = samlCred.getAttributeAsString(samlAttrKey);
      if (attribute != null) {
          userAttributes.put(inAttrKey, attribute);
      }
  }
  
  @Override
  public Object loadUserBySAML(final SAMLCredential samlCred) throws UsernameNotFoundException {
    
        CoreStandardSbacUser user = null;
        try {
            final String[] pipeDelimitedChain = samlCred.getAttributeAsStringArray(SBAC_TENANCY_CHAIN_KEY);
            System.out.println("tenant chain:" + pipeDelimitedChain);
            
            final Map<String, String> userAtts = extractUserAttributes(samlCred);
            user = (CoreStandardSbacUser)rolesAndPermissionsService.createUser(pipeDelimitedChain, userAtts, CoreStandardSbacUser.class);
    
        } catch (final Exception e) {
            final String referenceNumber = String.valueOf(RandomUtils.nextInt(MAX_ERROR_CODE));
            _logger.error("failure processing user, reference number: " + referenceNumber, e);
            throw new UsernameNotFoundException("Unable to process user, reference number: " + referenceNumber, e);
        }

    
      try {
        // lets create a new sessionKey.
        AuthorizationDAO dao = new AuthorizationDAO();

        // generate a unique identifier.
        // todo: not sure if we need to make a DB call for this.
        String sessionKey = dao.generateNewSessionKey();

        // now insert a session into the table for this user.
        // todo: instead of CSR_Administrator we need to insert the
        // appropriate OpenAM roles - one record for each role.
        dao.addSessionForUser(user.getUsername (), sessionKey, "Admin");
        user.setSessionId (sessionKey);
      } catch (SQLException exp) {
        _logger.error(exp.getMessage());
        _logger.error(exp.getStackTrace().toString());
        // todo: Fix this so that it returns a proper message.
      }
      return user;
  }
  
  
  

}
