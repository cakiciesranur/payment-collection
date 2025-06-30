package examples.payment;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.NONE)
public class PaymentOperationExamples {
    public static final String CREATE_PAYMENT_REQUEST_EXAMPLE = """
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

    public static final String UPDATE_PAYMENT_REQUEST_EXAMPLE = """
            {
               "customerId": 1,
               "amount": 16000.00,
               "currency": "TRY",
               "paymentDate": "2025-01-15",
               "dueDate": "2025-02-15",
               "paymentType": "BANK_TRANSFER",
               "notes": "Changed payment type"
             }
            """;

    public static final String CREATE_PAYMENT_RESPONSE_EXAMPLE = """
            {
               "responseType": "SUCCESS",
               "errorCode": null,
               "message": "Payment created successfully",
               "data": {
                 "id": 1,
                 "customerId": 1,
                 "customerCode": "C001",
                 "customerCompanyName": "Yilmaz Madeni Yag A.S.",
                 "amount": 15000.5,
                 "currency": "TRY",
                 "paymentDate": "2025-01-15",
                 "dueDate": "2025-02-15",
                 "paymentType": "BANK_TRANSFER",
                 "status": "PENDING",
                 "notes": "Ocak ayi sevkiyat odemesi",
                 "createdDate": "2025-06-29T16:33:15.715",
                 "lastModifiedDate": "2025-06-29T16:33:15.715",
                 "createdBy": "admin",
                 "lastModifiedBy": "admin"
               }
             }
            """;
}
