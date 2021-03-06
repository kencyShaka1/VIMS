/*
 * This program is part of the OpenLMIS logistics management information system platform software.
 * Copyright © 2013 VillageReach
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *  
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License along with this program.  If not, see http://www.gnu.org/licenses.  For additional information contact info@OpenLMIS.org. 
 */

package org.openlmis.order.service;

import org.ict4h.atomfeed.server.service.EventService;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.openlmis.db.categories.UnitTests;
import org.openlmis.order.domain.Order;
import org.openlmis.order.event.OrderStatusChangeEvent;
import org.openlmis.rnr.domain.Rnr;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.modules.junit4.PowerMockRunnerDelegate;

import static com.natpryce.makeiteasy.MakeItEasy.a;
import static com.natpryce.makeiteasy.MakeItEasy.make;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.openlmis.order.domain.OrderStatus.READY_TO_PACK;
import static org.openlmis.rnr.builder.RequisitionBuilder.defaultRequisition;
import static org.powermock.api.mockito.PowerMockito.whenNew;

@Category(UnitTests.class)
@RunWith(PowerMockRunner.class)
@PowerMockRunnerDelegate(BlockJUnit4ClassRunner.class)
@PrepareForTest(OrderEventService.class)
public class OrderEventServiceTest {

  @Mock
  EventService eventService;

  @InjectMocks
  OrderEventService service;

  @Test
  public void shouldTriggerNotifyOnEventService() throws Exception {
    Rnr requisition = make(a(defaultRequisition));
    Order order = new Order(requisition);
    order.setStatus(READY_TO_PACK);

    OrderStatusChangeEvent event = mock(OrderStatusChangeEvent.class);
    whenNew(OrderStatusChangeEvent.class).withArguments(order).thenReturn(event);

    service.notifyForStatusChange(order);

    verify(eventService).notify(event);
  }
}
