package uk.gov.dwp.uc.pairtest.service;

import thirdparty.paymentgateway.TicketPaymentService;
import thirdparty.paymentgateway.TicketPaymentServiceImpl;
import thirdparty.seatbooking.SeatReservationService;
import thirdparty.seatbooking.SeatReservationServiceImpl;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.validator.TicketRequestValidator;

import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * Class to calculate no of seats, tickets, ticket price, reserve seatts, make payments and print receipt.
 */
public class TicketBookingServiceImpl implements TicketBookingService {
    private  SeatReservationService seatReservationService = new SeatReservationServiceImpl();

    private  TicketPaymentService ticketPaymentService = new TicketPaymentServiceImpl();

    private  TicketRequestValidator ticketRequestValidator = new TicketRequestValidator();

    private static final int CHILD_TICKET_PRICE = 10;

    private static final int ADULT_TICKET_PRICE = 20;


    /**
     * Method to calculate and reserve seats
     *
     * @param seatList
     */
    @Override
    public void calculateAndReserveSeats(Long accountId, ArrayList<TicketTypeRequest> seatList) {
        //reserve seats
        seatReservationService.reserveSeat(accountId, getNoOfSeats(seatList));
    }

    private int getNoOfSeats(ArrayList<TicketTypeRequest> list){
       return list.stream()
                .filter(t -> t.getTicketType() != TicketTypeRequest.Type.INFANT)
                .mapToInt(TicketTypeRequest::getNoOfTickets)
                .sum();
    }

    /**
     * Method to calculate price and make payment
     *
     * @param purchaseList
     */
    @Override
    public void calculateTotalAndMakePayment(long accountId, ArrayList<TicketTypeRequest> purchaseList) {
        ticketRequestValidator.validateId(accountId);
        //make payment
        ticketPaymentService.makePayment(accountId, getTotalAmountToPay(purchaseList));
    }

    /**
     * Method to return total tickets
     *
     * @param purchaseList
     * @return totTickets
     */
    @Override
    public int getTotalTickets(ArrayList<TicketTypeRequest> purchaseList) {
        return purchaseList.stream().mapToInt(TicketTypeRequest::getNoOfTickets).sum();
    }

    /**
     * Method to return total amount
     *
     * @param purchaseList
     * @return totTickets
     */
    private int getTotalAmountToPay(ArrayList<TicketTypeRequest> purchaseList) {
        return purchaseList.stream().mapToInt(t -> {
            int ticketPrice = 0;
            switch (t.getTicketType()) {
                case ADULT -> ticketPrice = t.getNoOfTickets() * ADULT_TICKET_PRICE;

                case CHILD -> ticketPrice = t.getNoOfTickets() * CHILD_TICKET_PRICE;
            }
            return ticketPrice;
        }).sum();
    }


    /**
     * Method to return purchase tickets, price and seats
     *
     * @param purchaseList
     * @return printReceiptStr
     */
    @Override
    public String printReceipt(ArrayList<TicketTypeRequest> purchaseList) {
        String header = "TicketType      |      No.Tickets ";
        String price = "Price";
        String dateTimeStr = "Date and Time";
        String seats = "Seats";
        String ticket="Tickets";
        String printReceiptStr = "";
        String content = header + "\n";
        if (ticketRequestValidator.validatePurchaseRequest(purchaseList)) {
            for (TicketTypeRequest l : purchaseList) {
            content+=l.getTicketType().toString()+ "          |          "+l.getNoOfTickets()+ "\n";
            }
            printReceiptStr+= content + "\n";
            printReceiptStr+= price +":    "+ getTotalAmountToPay(purchaseList)+ "\n";
            printReceiptStr+= seats +":    "+  getNoOfSeats(purchaseList)+ "\n";
            printReceiptStr+= ticket +":    "+  getTotalTickets(purchaseList)+ "\n";
            printReceiptStr+= dateTimeStr +":    "+  LocalDateTime.now() +"\n";
        }
        return printReceiptStr;
    }

}
