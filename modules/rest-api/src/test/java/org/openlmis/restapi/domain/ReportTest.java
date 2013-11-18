/*
 * This program is part of the OpenLMIS logistics management information system platform software.
 * Copyright © 2013 VillageReach
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *  
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License along with this program.  If not, see http://www.gnu.org/licenses.  For additional information contact info@OpenLMIS.org. 
 */

package org.openlmis.restapi.domain;

import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.rules.ExpectedException;
import org.openlmis.core.exception.DataException;
import org.openlmis.db.categories.UnitTests;
import org.openlmis.restapi.builder.ReportBuilder;
import org.openlmis.rnr.builder.RequisitionBuilder;
import org.openlmis.rnr.builder.RnrLineItemBuilder;
import org.openlmis.rnr.domain.Rnr;
import org.openlmis.rnr.domain.RnrLineItem;

import java.util.List;

import static com.natpryce.makeiteasy.MakeItEasy.*;
import static java.util.Arrays.asList;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.openlmis.restapi.builder.ReportBuilder.defaultReport;
import static org.openlmis.restapi.builder.ReportBuilder.products;
import static org.openlmis.rnr.builder.RnrLineItemBuilder.*;

@Category(UnitTests.class)
public class ReportTest {

  @Rule
  public ExpectedException expectedEx = ExpectedException.none();

  private String nullString = null;

  @Test
  public void shouldThrowExceptionIfReportDoesNotContainAgentCode() {
    Report report = make(a(ReportBuilder.defaultReport, with(ReportBuilder.agentCode, nullString)));

    expectedEx.expect(DataException.class);
    expectedEx.expectMessage("error.restapi.mandatory.missing");

    report.validate();
  }

  @Test
  public void shouldThrowExceptionIfReportContainsBlankAgentCode() {
    Report report = make(a(ReportBuilder.defaultReport, with(ReportBuilder.agentCode, "")));

    expectedEx.expect(DataException.class);
    expectedEx.expectMessage("error.restapi.mandatory.missing");

    report.validate();
  }

  @Test
  public void shouldThrowExceptionIfReportDoesNotContainProgramCode() {
    Report report = make(a(ReportBuilder.defaultReport, with(ReportBuilder.programCode, nullString)));

    expectedEx.expect(DataException.class);
    expectedEx.expectMessage("error.restapi.mandatory.missing");

    report.validate();
  }

  @Test
  public void shouldThrowExceptionIfReportContainsBlankProgramCode() {
    Report report = make(a(ReportBuilder.defaultReport, with(ReportBuilder.programCode, "")));

    expectedEx.expect(DataException.class);
    expectedEx.expectMessage("error.restapi.mandatory.missing");

    report.validate();
  }

  @Test
  public void shouldGetRequisitionFromReport() throws Exception {
    Report report = make(a(ReportBuilder.defaultReport));
    Rnr requisition = report.getRequisition();
    assertThat(requisition.getId(), is(report.getRequisitionId()));
    assertThat(requisition.getFullSupplyLineItems(), is(report.getProducts()));
  }

  @Test
  public void shouldThrowExceptionIfProductsAreMissing() throws Exception {
    List<RnrLineItem> rnrLineItems = null;
    Report report = make(a(ReportBuilder.defaultReport, with(products, rnrLineItems)));

    expectedEx.expect(DataException.class);
    expectedEx.expectMessage("error.restapi.mandatory.missing");

    report.validateForApproval();
  }

  @Test
  public void shouldThrowExceptionIfProductCodeMissing() throws Exception {
    Report report = make(a(ReportBuilder.defaultReport));
    String productCode = null;
    report.setProducts(asList(make(a(RnrLineItemBuilder.defaultRnrLineItem, with(RnrLineItemBuilder.productCode, productCode)))));

    expectedEx.expect(DataException.class);
    expectedEx.expectMessage("error.restapi.mandatory.missing");

    report.validateForApproval();
  }

  @Test
  public void shouldThrowExceptionIfQuantityApprovedMissing() throws Exception {
    Report report = make(a(ReportBuilder.defaultReport));
    Integer quantityApproved = null;
    report.setProducts(asList(make(a(RnrLineItemBuilder.defaultRnrLineItem, with(RnrLineItemBuilder.quantityApproved, quantityApproved)))));

    expectedEx.expect(DataException.class);
    expectedEx.expectMessage("error.restapi.mandatory.missing");

    report.validateForApproval();
  }

  @Test
  public void shouldThrowExceptionIfQuantityApprovedIsNegative() throws Exception {
    Report report = make(a(ReportBuilder.defaultReport));
    Integer quantityApproved = -1;
    report.setProducts(asList(make(a(RnrLineItemBuilder.defaultRnrLineItem, with(RnrLineItemBuilder.quantityApproved, quantityApproved)))));

    expectedEx.expect(DataException.class);
    expectedEx.expectMessage("error.restapi.quantity.approved.negative");

    report.validateForApproval();
  }


  @Test
  public void shouldMarkProductNotPresentInReportAsSkipped() throws Exception {
    List<RnrLineItem> productList = asList(make(a(defaultRnrLineItem, with(productCode, "P10"))));
    Report report = make(a(defaultReport, with(products, productList)));

    Rnr rnr = make(a(RequisitionBuilder.defaultRnr));
    rnr.setFullSupplyLineItems(asList(make(a(defaultRnrLineItem, with(productCode, "P10"))), make(a(defaultRnrLineItem, with(productCode, "P11")))));

    Rnr rnrWithSkippedProducts = report.getRnrWithSkippedProducts(rnr);

    assertTrue(rnrWithSkippedProducts.getFullSupplyLineItems().get(1).getSkipped());
  }

  @Test
  public void shouldCopyReportLineItemToRnrIfProductIsNotSkipped() throws Exception {
    List<RnrLineItem> productList = asList(make(a(defaultRnrLineItem, with(productCode, "P10"), with(stockInHand, 100))));
    Report report = make(a(defaultReport, with(products, productList)));

    Rnr rnr = make(a(RequisitionBuilder.defaultRnr));
    RnrLineItem rnrLineItem = make(a(defaultRnrLineItem, with(productCode, "P10")));
    rnr.setFullSupplyLineItems(asList(rnrLineItem));

    report.getRnrWithSkippedProducts(rnr);

    assertThat(rnr.getFullSupplyLineItems().get(0).getStockInHand(), is(100));
  }
}

