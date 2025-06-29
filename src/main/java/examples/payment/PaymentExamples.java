package examples.payment;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.NONE)
public class PaymentExamples {
    public static final String CREATE_PAYMENT_EXAMPLE = """
            {
              "customerId": 1,
              "amount": 1500.00,
              "currency": "USD",
              "paymentDate": "2025-07-01",
              "dueDate": "2025-07-15",
              "paymentType": "CASH",
              "notes": "First payment"
            }
            """;
}
