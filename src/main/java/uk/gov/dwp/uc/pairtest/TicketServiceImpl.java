package uk.gov.dwp.uc.pairtest;

import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;
import uk.gov.dwp.uc.pairtest.service.TicketBookingService;
import uk.gov.dwp.uc.pairtest.service.TicketBookingServiceImpl;
import uk.gov.dwp.uc.pairtest.validator.TicketRequestValidator;


import java.util.ArrayList;
import java.util.List;


public class TicketServiceImpl implements TicketService {
    /**
     * Should only have private methods other than the one below.
     */

    private TicketRequestValidator ticketRequestValidator = new TicketRequestValidator();

    private TicketBookingService ticketBookingService = new TicketBookingServiceImpl();

    @Override
    public void purchaseTickets(Long accountId, TicketTypeRequest... ticketTypeRequests) throws InvalidPurchaseException {
        ArrayList<TicketTypeRequest> list = new ArrayList<>();
        list.addAll(List.of(ticketTypeRequests));
       
        ticketRequestValidator.validateId(accountId);
        if (ticketRequestValidator.validatePurchaseRequest(list)) {
            //Calculate the correct amount for the requested tickets
            ticketBookingService.calculateTotalAndMakePayment(accountId, list);
            // Calculate the correct no of seats to reserve
            ticketBookingService.calculateAndReserveSeats(accountId, list);

            ticketBookingService.getTotalTickets(list);
        }
    }
}
