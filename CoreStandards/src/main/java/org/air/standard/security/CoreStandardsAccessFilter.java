/*******************************************************************************
 * Educational Online Test Delivery System 
 * Copyright (c) 2014 American Institutes for Research
 *   
 * Distributed under the AIR Open Source License, Version 1.0 
 * See accompanying file AIR-License-1_0.txt or at
 * http://www.smarterapp.org/documents/American_Institutes_for_Research_Open_Source_Software_License.pdf
 ******************************************************************************/
package org.air.standard.security;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.opentestsystem.shared.security.domain.SbacRole;
import org.opentestsystem.shared.security.domain.SbacUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @author mpatel
 *
 */
public class CoreStandardsAccessFilter implements Filter
{
  private static final Logger _logger = LoggerFactory.getLogger (CoreStandardsAccessFilter.class);
  private static final String _subscriptionRedirectPage = "pages/subscription-needed.jsp";
  /*
   * (non-Javadoc)
   * 
   * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
   */
  @Override
  public void init (FilterConfig filterConfig) throws ServletException {
    
  }

  /*
   * (non-Javadoc)
   * 
   * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest,
   * javax.servlet.ServletResponse, javax.servlet.FilterChain)
   */
  @Override
  public void doFilter (ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
    HttpServletRequest request = (HttpServletRequest) req;
    HttpServletResponse response = (HttpServletResponse) res;
    String path = ((HttpServletRequest) request).getRequestURI ();
    try {
        if(path.contains (_subscriptionRedirectPage)) {
            chain.doFilter (request, response);
        } else {
            SbacUser user = (SbacUser) SecurityContextHolder.getContext ().getAuthentication ().getPrincipal ();
            Collection<SbacRole> sbacRoles = user.getRoles ();
            boolean isAdministratorRole = false;
            if (sbacRoles != null && !sbacRoles.isEmpty ()) {
              for (SbacRole sbacRole : sbacRoles) {
                if (sbacRole.getRoleName () != null && sbacRole.getRoleName ().equalsIgnoreCase ("Administrator")) {
                  isAdministratorRole = true;
                  break;
                }
              }
            }
            if (isAdministratorRole) {
              chain.doFilter (request, response);
            } else {
              response.sendRedirect (request.getContextPath () + "/" +_subscriptionRedirectPage);
            }
        }
    } catch (Exception e) {
      _logger.error (e.toString (),e);
      response.sendRedirect (request.getContextPath () + "/" +_subscriptionRedirectPage);
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see javax.servlet.Filter#destroy()
   */
  @Override
  public void destroy () {
    
  }

}
