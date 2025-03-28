package pizzashop.repository;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import pizzashop.model.Payment;
import pizzashop.model.PaymentType;

public class PaymentRepositoryTest {
    private PaymentRepository paymentRepository;
    private static final String TEST_FILENAME = "data/test_payments.txt";

    @BeforeEach
    void setUp() throws IOException {
        Files.write(Paths.get(TEST_FILENAME), new byte[0]);
        paymentRepository = new PaymentRepository(TEST_FILENAME);
    }

    private boolean isPaymentInFile(Payment payment) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(TEST_FILENAME));
        return lines.contains(payment.toString());
    }

    @Test
    @DisplayName("Valid Payment")
    void add_ValidPayment() {
        Payment validPayment = new Payment(8, PaymentType.Card, 0.0);
        paymentRepository.add(validPayment);
        try {
            assertTrue(isPaymentInFile(validPayment));
        } catch (Exception e) {
            fail("Unexpected exception");
        }
    }

    @Test
    @DisplayName("Valid Payment 2")
    void add_ValidPayment_2() {
        Payment validPayment = new Payment(1, PaymentType.Card, 0.0);
        paymentRepository.add(validPayment);
        try {
            assertTrue(isPaymentInFile(validPayment));
        } catch (Exception e) {
            fail("Unexpected exception");
        }
    }

    @Test
    @DisplayName("Invalid Payment - amount")
    void add_InvalidPayment_Amount() {
        Payment invalidPayment = new Payment(8, PaymentType.Card, -1.0);

        assertThrows(IllegalArgumentException.class, () -> paymentRepository.add(invalidPayment));
    }

    @Test
    @DisplayName("Invalid Payment - table number")
    void add_InvalidPayment_TableNumber() {
        Payment invalidPayment = new Payment(9, PaymentType.Card, 0.0);

        assertThrows(IllegalArgumentException.class, () -> paymentRepository.add(invalidPayment));
    }
}
