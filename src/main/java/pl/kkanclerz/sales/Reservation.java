package pl.kkanclerz.sales;

import pl.kkanclerz.sales.cart.CartItem;
import pl.kkanclerz.sales.offerting.Offer;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class Reservation {
    private final String id;
    private final BigDecimal total;
    private final CustomerDetails customerDetails;
    private final List<ReservationLine> lines;
    private PaymentDetails paymentDetails;
    private Instant paidAt;

    public Reservation(String id, BigDecimal total, CustomerDetails customerDetails, List<ReservationLine> lines) {
        this.id = id;
        this.total = total;
        this.customerDetails = customerDetails;
        this.lines = lines;
    }

    public static Reservation of(Offer currentOffer, List<CartItem> items, CustomerData customerData) {
        return new Reservation(
                UUID.randomUUID().toString(),
                currentOffer.getTotal(),
                CustomerDetails.of(
                        customerData.getEmail(),
                        customerData.getFirstname(),
                        customerData.getLastname()
                ),
                items.stream()
                        .map(cartItem -> new ReservationLine(cartItem.getProductId(), cartItem.getQuantity()))
                        .collect(Collectors.toList())
        );
    }

    public boolean isPending() {
        return paidAt == null;
    }

    public String getCustomerEmail() {
        return customerDetails.getEmail();
    }

    public String getCustomerLastname() {
        return customerDetails.getLastname();
    }

    public String getId() {
        return id;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void registerPayment(PaymentGateway paymentGateway) {
        paymentDetails = paymentGateway.registerPayment(
                getId(),
                total,
                customerDetails.getEmail(),
                customerDetails.getLastname()
        );
    }

    public String getPaymentId() {
        return paymentDetails.getId();
    }

    public String getPaymentUrl() {
        return paymentDetails.getUrl();
    }
}
