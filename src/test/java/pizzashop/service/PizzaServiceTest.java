package pizzashop.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import pizzashop.model.MenuDataModel;
import pizzashop.model.Payment;
import pizzashop.model.PaymentType;
import pizzashop.repository.MenuRepository;
import pizzashop.repository.PaymentRepository;

@ExtendWith(MockitoExtension.class)
public class PizzaServiceTest {
    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private MenuRepository menuRepository;

    @InjectMocks
    private PizzaService pizzaService;

    @Test
    @DisplayName("Empty payment list")
    public void getTotalEmpty() {
        when(paymentRepository.getAll()).thenReturn(Collections.emptyList());

        double result = pizzaService.getTotalAmount(PaymentType.Card);
        assertEquals(0.0, result);

        when(paymentRepository.getAll()).thenReturn(null);

        result = pizzaService.getTotalAmount(PaymentType.Card);
        assertEquals(0.0, result);
    }

    @Test
    @DisplayName("One matching payment")
    public void getTotalOnePayment() {
        List<Payment> payments = List.of(new Payment(1, PaymentType.Card, 50.0));
        when(paymentRepository.getAll()).thenReturn(payments);

        double result = pizzaService.getTotalAmount(PaymentType.Card);
        assertEquals(50.0, result);
    }

    @Test
    @DisplayName("One non-matching payment")
    public void getTotalOneNonMatchingPayment() {
        List<Payment> payments = List.of(new Payment(1, PaymentType.Cash, 100000.0));
        when(paymentRepository.getAll()).thenReturn(payments);

        double result = pizzaService.getTotalAmount(PaymentType.Card);
        assertEquals(0.0, result);
    }

    @Test
    @DisplayName("Two matching payments")
    public void getTotalTwoPayments() {
        List<Payment> payments = List.of(
                new Payment(1, PaymentType.Cash, 40.0),
                new Payment(1, PaymentType.Cash, 50.0));

        when(paymentRepository.getAll()).thenReturn(payments);

        double result = pizzaService.getTotalAmount(PaymentType.Cash);
        assertEquals(90.0, result);
    }

    @Test
    @DisplayName("Two non-matching payments")
    public void getTotalTwoNonMatchingPayments() {
        List<Payment> payments = List.of(
                new Payment(1, PaymentType.Cash, 1000000.0),
                new Payment(1, PaymentType.Cash, 1000000.0));

        when(paymentRepository.getAll()).thenReturn(payments);

        double result = pizzaService.getTotalAmount(PaymentType.Card);
        assertEquals(0.0, result);
    }

    @Test
    @DisplayName("Mixed payments")
    public void getTotalMixedPayments() {
        List<Payment> payments = List.of(
                new Payment(1, PaymentType.Cash, 10000.0),
                new Payment(1, PaymentType.Card, 50.0),
                new Payment(1, PaymentType.Card, 10.0),
                new Payment(1, PaymentType.Cash, 10000.0));

        when(paymentRepository.getAll()).thenReturn(payments);

        double result = pizzaService.getTotalAmount(PaymentType.Card);
        assertEquals(60.0, result);
    }

    @Test
    @DisplayName("Get menu data returns value from repository")
    public void getMenuData_ReturnsFromRepo() {
        List<MenuDataModel> mockMenu = List.of(new MenuDataModel("Pizza", 1, 10.0));
        when(menuRepository.getMenu()).thenReturn(mockMenu);

        List<MenuDataModel> result = pizzaService.getMenuData();
        assertSame(mockMenu, result);
    }

    @Test
    @DisplayName("Get payments returns value from repository")
    public void getPayments_ReturnsFromRepo() {
        List<Payment> mockPayments = List.of(new Payment(1, PaymentType.Cash, 100.0));
        when(paymentRepository.getAll()).thenReturn(mockPayments);

        List<Payment> result = pizzaService.getPayments();
        assertSame(mockPayments, result);
    }

    @Test
    @DisplayName("Add payment delegates to repository")
    public void addPayment_DelegatesToRepo() {
        pizzaService.addPayment(5, PaymentType.Card, 25.0);
        verify(paymentRepository, times(1)).add(
                argThat(p -> p.getTableNumber() == 5 &&
                        p.getType() == PaymentType.Card &&
                        p.getAmount() == 25.0));
    }
}
