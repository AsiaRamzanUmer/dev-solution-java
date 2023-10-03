package uk.gov.dwp.uc.pairtest.service;

import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;

import java.util.ArrayList;

public interface TicketBookingService {

    void calculateAndReserveSeats(Long accountId, ArrayList<TicketTypeRequest> list);

    void calculateTotalAndMakePayment(long accountId, ArrayList<TicketTypeRequest> list);

    int getTotalTickets(ArrayList<TicketTypeRequest> list);

    public String printReceipt(ArrayList<TicketTypeRequest> list);
}
