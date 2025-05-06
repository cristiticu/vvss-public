package pizzashop.repository;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.Spy;

import pizzashop.model.Payment;
import pizzashop.model.PaymentType;

@ExtendWith(MockitoExtension.class)
public class PaymentRepositoryTest {
    private final String filename = "data/test_payments.txt";

    @Spy
    private List<Payment> mockList = new java.util.ArrayList<>(List.of(
            new Payment(1, PaymentType.Cash, 10.0),
            new Payment(2, PaymentType.Card, 15.5)));

    private PaymentRepository paymentRepository;

    @BeforeEach
    void setUp() throws Exception {
        Files.write(Paths.get(filename), new byte[0]);

        paymentRepository = new PaymentRepository(filename);

        Field field = PaymentRepository.class.getDeclaredField("paymentList");
        field.setAccessible(true);
        field.set(paymentRepository, mockList);
    }

    private boolean isPaymentInFile(Payment payment) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(filename));
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

    @Test
    @DisplayName("Get All returns current list")
    void getAll_ReturnsPaymentList() {
        List<Payment> payments = paymentRepository.getAll();

        assertTrue(payments.containsAll(mockList));
    }

    @Test
    @DisplayName("WriteAll writes to file correctly")
    void writeAll_WritesToFileCorrectly() throws IOException {
        paymentRepository.writeAll();
        for (Payment p : mockList) {
            assertTrue(isPaymentInFile(p));
        }
    }

    @Test
    @DisplayName("ReadPayments loads data from file")
    void readPayments_LoadsData() throws Exception {
        String fileContent = "3,Cash,20.0\n4,Card,25.5\n";
        Files.write(Paths.get(filename), fileContent.getBytes());

        PaymentRepository repo = new PaymentRepository(filename);
        List<Payment> payments = repo.getAll();

        assertTrue(payments.stream()
                .anyMatch(p -> p.getTableNumber() == 3 && p.getType() == PaymentType.Cash && p.getAmount() == 20.0));
        assertTrue(payments.stream()
                .anyMatch(p -> p.getTableNumber() == 4 && p.getType() == PaymentType.Card && p.getAmount() == 25.5));
    }
}
