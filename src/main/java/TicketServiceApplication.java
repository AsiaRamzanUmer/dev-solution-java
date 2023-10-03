import uk.gov.dwp.uc.pairtest.TicketServiceImpl;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.service.TicketBookingServiceImpl;

import java.util.ArrayList;
import java.util.List;

public class TicketServiceApplication {

    public static void main(String s[]) {
        TicketBookingServiceImpl ticketBookingService = new TicketBookingServiceImpl();
        TicketServiceImpl ticketServiceImpl = new TicketServiceImpl();
        TicketTypeRequest[] ticketTypeRequest = {
                new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 3),
                new TicketTypeRequest(TicketTypeRequest.Type.INFANT, 1),
                new TicketTypeRequest(TicketTypeRequest.Type.CHILD, 2),
        };
        ticketServiceImpl.purchaseTickets(123L, ticketTypeRequest);
        ArrayList<TicketTypeRequest> list = new ArrayList<>();
        list.addAll(List.of(ticketTypeRequest));

         System.out.println(ticketBookingService.printReceipt(list));
    }
}

