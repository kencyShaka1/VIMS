/*
 * Electronic Logistics Management Information System (eLMIS) is a supply chain management system for health commodities in a developing country setting.
 *
 * Copyright (C) 2015  John Snow, Inc (JSI). This program was produced for the U.S. Agency for International Development (USAID). It was prepared under the USAID | DELIVER PROJECT, Task Order 4.
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.openlmis.ivdform.domain.reports;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.openlmis.core.domain.BaseModel;
import org.openlmis.core.domain.Product;
import org.openlmis.ivdform.domain.VaccineProductDose;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VaccineCoverageItem extends BaseModel {

  private Boolean skipped = false;

  private Long reportId;
  private Long productId;

  private Product product;

  private Boolean trackMale;
  private Boolean trackFemale;
  private Long doseId;

  // this is needed to make the calculations of coverage on the front end.
  private VaccineProductDose vaccineProductDose;

  private Long displayOrder;
  private String displayName;

  private Long regularMale;
  private Long regularFemale;

  private Long outreachMale;
  private Long outreachFemale;

  private Long campaignMale;
  private Long campaignFemale;

  private Long previousRegular;
  private Long previousOutreach;

  public void copyValuesFrom(VaccineCoverageItem source){
    this.setRegularMale(source.getRegularMale());
    this.setRegularFemale(source.getRegularFemale());
    this.setCampaignMale(source.getCampaignMale());
    this.setCampaignFemale(source.getCampaignFemale());
    this.setOutreachMale(source.getOutreachMale());
    this.setOutreachFemale(source.getOutreachFemale());
  }

}
