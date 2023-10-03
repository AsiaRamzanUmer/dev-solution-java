package uk.gov.dwp.uc.pairtest.service;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import thirdparty.paymentgateway.TicketPaymentService;
import thirdparty.seatbooking.SeatReservationService;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;

import java.util.ArrayList;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class TicketBookingServiceImplTest {

    @InjectMocks
    private TicketBookingServiceImpl ticketBookingService = new TicketBookingServiceImpl();

    @Mock
    private TicketPaymentService ticketPaymentService;

    @Mock
    private SeatReservationService seatReservationService;




    @Test
    public void testCalculateAndReserveSeats() {
        ArrayList<TicketTypeRequest> list = new ArrayList<>();
        list.add( new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 4));
        list.add(new TicketTypeRequest(TicketTypeRequest.Type.CHILD, 3));

        ticketBookingService.calculateAndReserveSeats(123L, list);
        Mockito.verify(seatReservationService).reserveSeat(123L, 7);
    }

    @Test
    public void testCalculateSeatsAndReserveNoSeatForInfant() {
        ArrayList<TicketTypeRequest> list = new ArrayList<>();
        list.add( new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 2));
        list.add(new TicketTypeRequest(TicketTypeRequest.Type.CHILD, 3));
        list.add(new TicketTypeRequest(TicketTypeRequest.Type.INFANT, 2));

        ticketBookingService.calculateAndReserveSeats(123L, list);
        Mockito.verify(seatReservationService).reserveSeat(123L, 5);
    }

    @Test
    public void testCalculateTotalAndMakePayment() {
        ArrayList<TicketTypeRequest> list = new ArrayList<>();
        list.add( new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 1));
        list.add(new TicketTypeRequest(TicketTypeRequest.Type.CHILD, 1));

        ticketBookingService.calculateTotalAndMakePayment(123L, list);
        Mockito.verify(ticketPaymentService).makePayment(123L, 30);
    }

    @Test
    public void testCalculateTotalAndMakePaymentZeroPayForInfantTicket() {
        ArrayList<TicketTypeRequest> list = new ArrayList<>();
        list.add( new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 3));
        list.add(new TicketTypeRequest(TicketTypeRequest.Type.INFANT, 2));

        ticketBookingService.calculateTotalAndMakePayment(123L, list);
        Mockito.verify(ticketPaymentService).makePayment(123L, 60);
    }

    @Test
    public void testCalculateTotalAndMakePaymentMultipleTicket() {
        ArrayList<TicketTypeRequest> list = new ArrayList<>();
        list.add( new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 7));
        list.add(new TicketTypeRequest(TicketTypeRequest.Type.CHILD, 1));
        list.add(new TicketTypeRequest(TicketTypeRequest.Type.INFANT, 2));

        ticketBookingService.calculateTotalAndMakePayment(123L, list);
        Mockito.verify(ticketPaymentService).makePayment(123L, 150);
    }

    @Test
    public void testCalculateTotalAndMakePaymentSingleTicket() {
        ArrayList<TicketTypeRequest> list = new ArrayList<>();
        list.add( new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 5));

        ticketBookingService.calculateTotalAndMakePayment(123L, list);
        Mockito.verify(ticketPaymentService).makePayment(123L, 100);
    }

    @Test
    public void testGetTotalTickets() {
        ArrayList<TicketTypeRequest> list = new ArrayList<>();
        list.add( new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 5));
        list.add(new TicketTypeRequest(TicketTypeRequest.Type.CHILD, 1));
        list.add(new TicketTypeRequest(TicketTypeRequest.Type.INFANT, 2));

        assertEquals(ticketBookingService.getTotalTickets(list), 8);
    }

    @Test
    public void testPrintReceipt() {
        ArrayList<TicketTypeRequest> list = new ArrayList<>();
        list.add( new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 5));
        list.add(new TicketTypeRequest(TicketTypeRequest.Type.CHILD, 1));
        list.add(new TicketTypeRequest(TicketTypeRequest.Type.INFANT, 2));

        assertFalse(ticketBookingService.printReceipt(list).isEmpty());
    }

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Test
    public void testPrintRecieptEmpty() {
        ArrayList<TicketTypeRequest> list = new ArrayList<>();
        exceptionRule.expect(InvalidPurchaseException.class);
        ticketBookingService.printReceipt(list).equals("");
        exceptionRule.expectMessage("child and infant tickets cannot be purchased without purchasing an adult ticket");

    }

}



