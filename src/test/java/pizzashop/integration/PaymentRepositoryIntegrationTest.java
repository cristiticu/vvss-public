package pizzashop.integration;

import org.junit.jupiter.api.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;

import pizzashop.model.Payment;
import pizzashop.model.PaymentType;
import pizzashop.repository.PaymentRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PaymentRepositoryIntegrationTest {

    private final String filename = "data/integration_test_payments.txt";
    private PaymentRepository repo;

    @BeforeEach
    void setup() throws IOException {
        Files.write(Paths.get(filename), new byte[0]); // clear file
        repo = new PaymentRepository(filename);
    }

    @Test
    @DisplayName("Add valid mocked payment and verify file content")
    void testAddValidMockedPayment() throws IOException {
        Payment mockPayment = mock(Payment.class);
        when(mockPayment.getAmount()).thenReturn(20.0);
        when(mockPayment.getTableNumber()).thenReturn(2);
        when(mockPayment.getType()).thenReturn(PaymentType.Cash);
        when(mockPayment.toString()).thenReturn("2,Cash,20.0");

        repo.add(mockPayment);

        List<String> lines = Files.readAllLines(Paths.get(filename));
        assertTrue(lines.contains("2,Cash,20.0"));
    }

    @Test
    @DisplayName("Reject negative payment amount")
    void testAddInvalidPaymentAmount() {
        Payment payment = new Payment(1, PaymentType.Card, -10.0);
        assertThrows(IllegalArgumentException.class, () -> repo.add(payment));
    }

    @Test
    @DisplayName("Get all payments after adding")
    void testGetAllPayments() {
        Payment payment = new Payment(1, PaymentType.Card, 5.0);
        repo.add(payment);
        List<Payment> all = repo.getAll();
        assertEquals(1, all.size());
        assertEquals(1, all.get(0).getTableNumber());
    }
}
