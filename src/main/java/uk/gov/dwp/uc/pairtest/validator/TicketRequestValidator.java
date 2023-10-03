package uk.gov.dwp.uc.pairtest.validator;

import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.Collectors;

/**
 * Class to validate id, max ticket purchase, has adult and sufficient number of adults are included in the purchase request,
 */
public class TicketRequestValidator {

    private static final int MAX_TICKET_PURCHASED = 20;


    /**
     * Method to validate id
     *
     * @param id
     */
    public void validateId(long id) {
        if (id <= 0) {
            throw new InvalidPurchaseException("Invalid account id");
        }
    }

    /**
     * Method to validate maximum tickets purchase
     *
     * @param purchaseList
     * @return boolean
     */
    public boolean validateMaxTicketPurchaseQty(ArrayList<TicketTypeRequest> purchaseList) {
        int noOfTickets = purchaseList.stream().mapToInt(TicketTypeRequest::getNoOfTickets).sum();
        if (noOfTickets > MAX_TICKET_PURCHASED) {
            throw new InvalidPurchaseException("Only a maximum of " + MAX_TICKET_PURCHASED + " tickets can be purchased at a time");
        }
        return true;
    }

    /**
     * Method to validate has adult include in tickets purchase request
     *
     * @param purchaseList
     * @return boolean
     */
    public boolean validateHasAdultTicket(ArrayList<TicketTypeRequest> purchaseList) {
        boolean hasAdultTicket = purchaseList.stream().anyMatch(request -> request.getTicketType() == TicketTypeRequest.Type.ADULT);
        if (!hasAdultTicket) {
            throw new InvalidPurchaseException("child and infant tickets cannot be purchased without purchasing an adult ticket");
        }
        return true;
    }

    /**
     * Method to validate sufficient adult present in tickets purchase request
     *
     * @param purchaseList
     * @return boolean
     */
    public boolean validateSufficientAdultTicketPresent(ArrayList<TicketTypeRequest> purchaseList) {
        boolean areSufficientAdultPresent = purchaseList.stream()
                .filter(t -> t.getTicketType().equals(TicketTypeRequest.Type.INFANT) || t.getTicketType().equals(TicketTypeRequest.Type.ADULT))
                .collect(Collectors.partitioningBy(t -> t.getTicketType().equals(TicketTypeRequest.Type.ADULT)))
                .getOrDefault(true, Collections.emptyList())
                .stream()
                .mapToInt(n -> n.getNoOfTickets())
                .sum() >= purchaseList.stream()
                .filter(t -> t.getTicketType().equals(TicketTypeRequest.Type.INFANT))
                .mapToInt(n -> n.getNoOfTickets())
                .sum();

        if (!areSufficientAdultPresent) {
            throw new InvalidPurchaseException("Only infant ticket requests are processed. Ensure sufficient adult laps are available for the number of infants");
        }
        return true;
    }


    public boolean validatePurchaseRequest(ArrayList<TicketTypeRequest> list) {
        return (validateMaxTicketPurchaseQty(list) && validateHasAdultTicket(list) && validateSufficientAdultTicketPresent(list));
    }
}
