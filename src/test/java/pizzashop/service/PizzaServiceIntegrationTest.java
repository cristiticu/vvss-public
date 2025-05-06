package pizzashop.service;

import org.junit.jupiter.api.*;
import pizzashop.model.Payment;
import pizzashop.model.PaymentType;
import pizzashop.repository.PaymentRepository;
import pizzashop.service.PizzaService;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PizzaServiceIntegrationTest {

    private final String filename = "data/full_integration_test_payments.txt";
    private PizzaService service;

    @BeforeEach
    void setUp() throws IOException {
        Files.write(Paths.get(filename), new byte[0]); // reset file
        PaymentRepository repo = new PaymentRepository(filename);
        service = new PizzaService(null, repo); // no menuRepo needed here
    }

    @Test
    @DisplayName("Add payment via service and verify persistence and retrieval")
    void testAddAndRetrievePayment() throws IOException {
        service.addPayment(3, PaymentType.Card, 25.0);

        List<Payment> payments = service.getPayments();
        assertEquals(1, payments.size());
        assertEquals(3, payments.get(0).getTableNumber());
        assertEquals(PaymentType.Card, payments.get(0).getType());

        List<String> fileLines = Files.readAllLines(Paths.get(filename));
        assertTrue(fileLines.contains("3,Card,25.0"));
    }

    @Test
    @DisplayName("Calculate total amount for specific type")
    void testGetTotalAmount() {
        service.addPayment(1, PaymentType.Card, 10.0);
        service.addPayment(2, PaymentType.Cash, 20.0);
        service.addPayment(3, PaymentType.Card, 30.0);

        double totalCard = service.getTotalAmount(PaymentType.Card);
        double totalCash = service.getTotalAmount(PaymentType.Cash);

        assertEquals(40.0, totalCard);
        assertEquals(20.0, totalCash);
    }

    @Test
    @DisplayName("Handle no matching payment types")
    void testGetTotalAmountNoMatches() {
        service.addPayment(1, PaymentType.Cash, 99.0);
        double total = service.getTotalAmount(PaymentType.Card);
        assertEquals(0.0, total);
    }
}
