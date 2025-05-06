package pizzashop.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PaymentTest {

    private Payment payment;

    @BeforeEach
    void setUp() {
        payment = new Payment(5, PaymentType.Card, 20.0);
    }

    @Test
    void constructorAndGetters() {
        assertEquals(5, payment.getTableNumber());
        assertEquals(PaymentType.Card, payment.getType());
        assertEquals(20.0, payment.getAmount(), 0.001);
    }

    @Test
    void setTableNumber() {
        payment.setTableNumber(10);
        assertEquals(10, payment.getTableNumber());
    }

    @Test
    void setType() {
        payment.setType(PaymentType.Cash);
        assertEquals(PaymentType.Cash, payment.getType());
    }

    @Test
    void setAmount() {
        payment.setAmount(35.5);
        assertEquals(35.5, payment.getAmount(), 0.001);
    }

    @Test
    void testToString() {
        String expected = "5,Card,20.0";
        assertEquals(expected, payment.toString());
    }
}
