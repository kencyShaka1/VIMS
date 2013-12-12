/*
 * This program is part of the OpenLMIS logistics management information system platform software.
 * Copyright © 2013 VillageReach
 *
 *  This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Affero General Public License for more details.
 *  You should have received a copy of the GNU Affero General Public License along with this program.  If not, see http://www.gnu.org/licenses.  For additional information contact info@OpenLMIS.org.
 */

package org.openlmis.distribution.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.openlmis.core.domain.ProductGroup;
import org.openlmis.distribution.domain.EpiUseLineItem;

import static java.lang.Integer.parseInt;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EpiUseLineItemDTO {

  private Long epiUseId;
  private ProductGroup productGroup;
  private Reading stockAtFirstOfMonth;
  private Reading stockAtEndOfMonth;
  private Reading received;
  private Reading loss;
  private Reading distributed;
  private Reading expirationDate;

  public EpiUseLineItem transform() {
    return new EpiUseLineItem(this.epiUseId, this.productGroup,
      parseInt(stockAtFirstOfMonth.getEffectiveValue()),
      parseInt(stockAtEndOfMonth.getEffectiveValue()),
      parseInt(received.getEffectiveValue()),
      parseInt(loss.getEffectiveValue()),
      parseInt(distributed.getEffectiveValue()),
      expirationDate.getEffectiveValue());
  }
}
