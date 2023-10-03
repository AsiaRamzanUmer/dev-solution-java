package uk.gov.dwp.uc.pairtest;

import org.junit.Before;
import org.junit.Test;
import org.mockito.*;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.service.TicketBookingService;



import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;


public class TicketServiceImplTest {

    @InjectMocks
    private TicketServiceImpl ticketService = new TicketServiceImpl();

    @Spy
    private TicketBookingService ticketBookingService;

    @Captor ArgumentCaptor<ArrayList<TicketTypeRequest>> argumentCaptor;

    ArgumentCaptor<Long> accountIdargument = ArgumentCaptor.forClass(Long.class);

    @Before
    public void init(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testPurchaseTicketCalculateTotalAndMakePayment() {

        TicketTypeRequest[] ticketTypeRequest = {
                new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 2),
                new TicketTypeRequest(TicketTypeRequest.Type.CHILD, 2),
                new TicketTypeRequest(TicketTypeRequest.Type.INFANT, 1)
        };
        ticketService.purchaseTickets(123L, ticketTypeRequest);
        ArrayList<TicketTypeRequest> list = new ArrayList<TicketTypeRequest>();
        list.addAll(List.of(ticketTypeRequest));

        Mockito.verify(ticketBookingService).calculateTotalAndMakePayment(accountIdargument.capture(), argumentCaptor.capture());

        assertEquals(123L, accountIdargument.getValue().longValue());

        assertEquals(TicketTypeRequest.Type.ADULT, argumentCaptor.getValue().get(0).getTicketType());
        assertEquals(TicketTypeRequest.Type.CHILD, argumentCaptor.getValue().get(1).getTicketType());
        assertEquals(TicketTypeRequest.Type.INFANT, argumentCaptor.getValue().get(2).getTicketType());

        assertEquals(2, argumentCaptor.getValue().get(0).getNoOfTickets());
        assertEquals(2, argumentCaptor.getValue().get(1).getNoOfTickets());
        assertEquals(1, argumentCaptor.getValue().get(2).getNoOfTickets());
    }

    @Test
    public void testPurchaseTicketCalculateAndReserveSeats() {

        TicketTypeRequest[] ticketTypeRequest = {
                new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 3),
                new TicketTypeRequest(TicketTypeRequest.Type.CHILD, 3),
                new TicketTypeRequest(TicketTypeRequest.Type.INFANT, 2)
        };
        ticketService.purchaseTickets(123L, ticketTypeRequest);
        ArrayList<TicketTypeRequest> list = new ArrayList<TicketTypeRequest>();
        list.addAll(List.of(ticketTypeRequest));

        Mockito.verify(ticketBookingService).calculateAndReserveSeats(accountIdargument.capture(), argumentCaptor.capture());

        assertEquals(123L, accountIdargument.getValue().longValue());

        assertEquals(TicketTypeRequest.Type.ADULT, argumentCaptor.getValue().get(0).getTicketType());
        assertEquals(TicketTypeRequest.Type.CHILD, argumentCaptor.getValue().get(1).getTicketType());
        assertEquals(TicketTypeRequest.Type.INFANT, argumentCaptor.getValue().get(2).getTicketType());

        assertEquals(3, argumentCaptor.getValue().get(0).getNoOfTickets());
        assertEquals(3, argumentCaptor.getValue().get(1).getNoOfTickets());
        assertEquals(2, argumentCaptor.getValue().get(2).getNoOfTickets());
    }

    @Test
    public void testPurchaseTicketGetTotalTickets() {

        TicketTypeRequest[] ticketTypeRequest = {
                new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 4),
                new TicketTypeRequest(TicketTypeRequest.Type.CHILD, 3),
                new TicketTypeRequest(TicketTypeRequest.Type.INFANT, 1)
        };
        ticketService.purchaseTickets(123L, ticketTypeRequest);
        ArrayList<TicketTypeRequest> list = new ArrayList<TicketTypeRequest>();
        list.addAll(List.of(ticketTypeRequest));

        Mockito.verify(ticketBookingService).getTotalTickets(argumentCaptor.capture());

        assertEquals(TicketTypeRequest.Type.ADULT, argumentCaptor.getValue().get(0).getTicketType());
        assertEquals(TicketTypeRequest.Type.CHILD, argumentCaptor.getValue().get(1).getTicketType());
        assertEquals(TicketTypeRequest.Type.INFANT, argumentCaptor.getValue().get(2).getTicketType());

        assertEquals(4, argumentCaptor.getValue().get(0).getNoOfTickets());
        assertEquals(3, argumentCaptor.getValue().get(1).getNoOfTickets());
        assertEquals(1, argumentCaptor.getValue().get(2).getNoOfTickets());
    }
}



