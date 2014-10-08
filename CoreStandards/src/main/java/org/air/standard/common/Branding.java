/*******************************************************************************
 * Educational Online Test Delivery System 
 * Copyright (c) 2014 American Institutes for Research
 *   
 * Distributed under the AIR Open Source License, Version 1.0 
 * See accompanying file AIR-License-1_0.txt or at
 * http://www.smarterapp.org/documents/American_Institutes_for_Research_Open_Source_Software_License.pdf
 ******************************************************************************/
package org.air.standard.common;

/**
 * @author mpatel
 *
 */
public class Branding
{
  private String imgURL;
  
  private String title;
  
  public Branding () {
    super ();
  }

  /**
   * @param imgURL
   * @param title
   */
  public Branding (String imgURL, String title) {
    super ();
    this.imgURL = imgURL;
    this.title = title;
  }

  public String getImgURL () {
    return imgURL;
  }

  public String getTitle () {
    return title;
  }

  public void setImgURL (String imgURL) {
    this.imgURL = imgURL;
  }

  public void setTitle (String title) {
    this.title = title;
  }
  
  
}
