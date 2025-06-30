package examples.customer;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.NONE)
public class CustomerOperationExamples {
    public static final String GET_CUSTOMER_BY_ID_RESPONSE_EXAMPLE = """
            {
                "responseType": "SUCCESS",
                "errorCode": null,
                "message": "Customer retrieved successfully",
                "data": {
                  "id": 1,
                  "customerCode": "C001",
                  "companyName": "Yılmaz Madeni Yağ A.Ş.",
                  "contactPerson": "Mehmet Yılmaz",
                  "email": "mehmet@yilmazyag.com",
                  "phone": "0212 555 1234",
                  "address": "Hadımköy Organize Sanayi Bölgesi, İstanbul",
                  "taxNumber": "1234567890",
                  "status": "ACTIVE"
                  "createdDate": "2025-06-29T16:18:56.791",
                  "lastModifiedDate": "2025-06-29T16:18:56.791",
                  "createdBy": "admin",
                  "lastModifiedBy": "admin"
                }
              }
            """;

    public static final String CREATE_CUSTOMER_REQUEST_EXAMPLE = """
            {
               "customerCode": "C001",
               "companyName": "Yılmaz Madeni Yağ A.Ş.",
               "contactPerson": "Mehmet Yılmaz",
               "email": "mehmet@yilmazyag.com",
               "phone": "0212 555 1234",
               "address": "Hadımköy Organize Sanayi Bölgesi, İstanbul",
               "taxNumber": "1234567890",
               "status": "ACTIVE"
             }
            """;

    public static final String CREATE_CUSTOMER_RESPONSE_EXAMPLE = """
            {
                "responseType": "SUCCESS",
                "errorCode": null,
                "message": "Customer created successfully",
                "data": {
                  "id": 1,
                  "customerCode": "C001",
                  "companyName": "Yilmaz Madeni Yag A.S.",
                  "contactPerson": "Mehmet Yilmaz",
                  "email": "mehmet@yilmazyag.com",
                  "phone": "0212 555 1234",
                  "address": "Hadimkoy Organize Sanayi Bölgesi, Istanbul",
                  "taxNumber": "1234567890",
                  "status": "ACTIVE",
                  "createdDate": "2025-06-29T16:18:56.791",
                  "lastModifiedDate": "2025-06-29T16:18:56.791",
                  "createdBy": "admin",
                  "lastModifiedBy": "admin"
                }
              }
            """;
    public static final String CREATE_GET_ALL_CUSTOMERS_RESPONSE_EXAMPLE = """
            {
                 "responseType": "SUCCESS",
                 "errorCode": null,
                 "message": "Customers retrieved successfully",
                 "data": {
                   "content": [
                    {
                      "id": 1,
                      "customerCode": "C001",
                      "companyName": "Yilmaz Madeni Yag A.S.",
                      "contactPerson": "Mehmet Yilmaz",
                      "email": "mehmet@yilmazyag.com",
                      "phone": "0212 555 1234",
                      "address": "Hadimkoy Organize Sanayi Bölgesi, Istanbul",
                      "taxNumber": "1234567890",
                      "status": "ACTIVE",
                      "createdDate": "2025-06-29T16:18:56.791",
                      "lastModifiedDate": "2025-06-29T16:18:56.791",
                      "createdBy": "admin",
                      "lastModifiedBy": "admin"
                    }
                   ],
                   "pageable": {
                     "pageNumber": 0,
                     "pageSize": 10,
                     "sort": {
                       "empty": false,
                       "unsorted": false,
                       "sorted": true
                     },
                     "offset": 0,
                     "paged": true,
                     "unpaged": false
                   },
                   "last": true,
                   "totalElements": 1,
                   "totalPages": 1,
                   "first": true,
                   "size": 10,
                   "number": 0,
                   "sort": {
                     "empty": false,
                     "unsorted": false,
                     "sorted": true
                   },
                   "numberOfElements": 1,
                   "empty": false
                 }
               }
            """;
}
