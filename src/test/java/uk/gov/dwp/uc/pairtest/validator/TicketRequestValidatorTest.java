package uk.gov.dwp.uc.pairtest.validator;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;

import java.util.ArrayList;

import static org.junit.Assert.assertTrue;

public class TicketRequestValidatorTest {


    private TicketRequestValidator ticketRequestValidator = new TicketRequestValidator();

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Test
    public void whenExceptionThrown_thenRuleAppliedOnInvalidAccountId() {
        long id=-1;
        exceptionRule.expect(InvalidPurchaseException.class);
        exceptionRule.expectMessage("Invalid account id");
        ticketRequestValidator.validateId(id);
    }

    @Test
    public void testValidateMaxTicketPurchaseQtyException() {
        ArrayList<TicketTypeRequest> list = new ArrayList<>();
        list.add( new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 12));
        list.add(new TicketTypeRequest(TicketTypeRequest.Type.CHILD, 10));
        exceptionRule.expect(InvalidPurchaseException.class);
        exceptionRule.expectMessage("Only a maximum of 20 tickets can be purchased at a time");
        ticketRequestValidator.validateMaxTicketPurchaseQty(list);
    }

    @Test
    public void testValidateMaxTicketPurchaseQtyWithin() {
        ArrayList<TicketTypeRequest> list = new ArrayList<>();
        list.add( new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 10));
        list.add(new TicketTypeRequest(TicketTypeRequest.Type.CHILD, 10));
        assertTrue(ticketRequestValidator.validateHasAdultTicket(list));
    }

    @Test
    public void testValidateHasAdultTicketException() {
        ArrayList<TicketTypeRequest> list = new ArrayList<>();
        list.add( new TicketTypeRequest(TicketTypeRequest.Type.CHILD, 1));
        list.add(new TicketTypeRequest(TicketTypeRequest.Type.INFANT, 2));
        exceptionRule.expect(InvalidPurchaseException.class);
        exceptionRule.expectMessage("child and infant tickets cannot be purchased without purchasing an adult ticket");
        ticketRequestValidator.validateHasAdultTicket(list);
    }

    @Test
    public void testEnsureValidateyHasAdultTicketPresent() {
        ArrayList<TicketTypeRequest> list = new ArrayList<>();
        list.add( new TicketTypeRequest(TicketTypeRequest.Type.CHILD, 1));
        list.add(new TicketTypeRequest(TicketTypeRequest.Type.INFANT, 2));
        list.add(new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 2));
        assertTrue( ticketRequestValidator.validateHasAdultTicket(list));
    }

    @Test
    public void testValidateSufficientAdultTicketPresentException() {
        ArrayList<TicketTypeRequest> list = new ArrayList<>();
        list.add( new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 2));
        list.add(new TicketTypeRequest(TicketTypeRequest.Type.INFANT, 3));
        exceptionRule.expect(InvalidPurchaseException.class);
        exceptionRule.expectMessage("Only infant ticket requests are processed. Ensure sufficient adult laps are available for the number of infants");
        ticketRequestValidator.validateSufficientAdultTicketPresent(list);
    }

    @Test
    public void testValidateSufficientAdultTicketPresent() {
        ArrayList<TicketTypeRequest> list = new ArrayList<>();
        list.add( new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 2));
        list.add(new TicketTypeRequest(TicketTypeRequest.Type.INFANT, 2));
        assertTrue(ticketRequestValidator.validateSufficientAdultTicketPresent(list));
    }

    @Test
    public void testValidatePurchaseRequestMaxTicketPurchaseQtyException() {
        ArrayList<TicketTypeRequest> list = new ArrayList<>();
        list.add( new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 20));
        list.add(new TicketTypeRequest(TicketTypeRequest.Type.CHILD, 2));
        exceptionRule.expect(InvalidPurchaseException.class);
        exceptionRule.expectMessage("Only a maximum of 20 tickets can be purchased at a time");
        ticketRequestValidator.validatePurchaseRequest(list);
    }

    @Test
    public void testValidatePurchaseRequestHasAdultTicketException() {
        ArrayList<TicketTypeRequest> list = new ArrayList<>();
        list.add( new TicketTypeRequest(TicketTypeRequest.Type.CHILD, 1));
        list.add(new TicketTypeRequest(TicketTypeRequest.Type.INFANT, 2));
        exceptionRule.expect(InvalidPurchaseException.class);
        exceptionRule.expectMessage("child and infant tickets cannot be purchased without purchasing an adult ticket");
        ticketRequestValidator.validatePurchaseRequest(list);
    }

    @Test
    public void testValidatePurchaseRequestSufficientAdultTicketPresentException() {
        ArrayList<TicketTypeRequest> list = new ArrayList<>();
        list.add( new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 2));
        list.add(new TicketTypeRequest(TicketTypeRequest.Type.INFANT, 3));
        exceptionRule.expect(InvalidPurchaseException.class);
        exceptionRule.expectMessage("Only infant ticket requests are processed. Ensure sufficient adult laps are available for the number of infants");
        ticketRequestValidator.validatePurchaseRequest(list);
    }

}
