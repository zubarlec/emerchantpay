# emerchantpay
Emerchantpay task

## Overview

Demo application for managing merchants and payment transactions.
There are two roles: merchant and admin.

Merchants have many payment transactions of different types

Transactions can be related:
- Authorize Transaction -> Charge Transaction -> Refund Transaction
- Authorize Transaction -> Reversal Transaction
  * Only approved or refunded transactions can be referenced, otherwise the status of the submitted transaction will be created with error status

Transaction types:   
- **Authorize transaction** - has amount and used to hold customer's amount
   
- **Charge transaction** - has amount and used to confirm the amount is taken from the customer's account and transferred to the merchant
    - The merchant's total transactions amount has to be the sum of the **approved Charge** transactions
   
- **Refund transaction** - has amount and used to reverse a specific amount (whole amount) of the Charge Transaction and return it to the customer
    - Transitions the **Charge** transaction to status **refunded**
    - The **approved Refund** transactions will decrease the merchant's total transaction amount
   
- **Reversal transaction** - has no amount, used to invalidate the Authorize Transaction
    - Transitions the **Authorize** transaction to status **reversed**


## Setup DB

### Create schema:

    CREATE SCHEMA emerchantpay COLLATE utf8mb4_0900_ai_ci;

### Create user:

    CREATE USER 'emerchantpay'@'%' IDENTIFIED BY 'emerchantpay';
    GRANT ALL ON emerchantpay.* TO 'emerchantpay'@'%';

To import initial accounts, set environment property 'emerchantpay.import.csv.file' to contain absolute path to the import csv file.
Example file can be found in 'backend/import-accounts.csv'

    emerchantpay.import.csv.file=absolute-path/backend/import-accounts.csv

If this is set and no other accounts exist in the DB, the service will try to import the accounts listed in the CSV file at startup.

## Run

### Backend

#### Build

    cd backend
    mvn clean package

#### Start
If not using IDE, you can run the backend directly with java:

    java -Dserver.port=8080 -Dspring.profiles.active=main -jar [path-to]/backend-[version].jar

Make sure to replace [path-to] and [version] to the actual values.
If Using IDE, make sure to set the active spring profile to 'main'.

### Frontend

    ng serve

## Login
Use one of the accounts from '/backend/import-accounts.csv' or from your own csv file used for the first run.

### Admin account:
 * view all transactions
 * view all merchants
 * create, edit, delete merchants

### Merchant account:
 * view only own transactions
 * submit new transaction

## Docker

First build the backend and frontend applications:

#### Backend

    cd backend
    mvn clean package

#### Client

    cd client
    ng build

### Run docker composer

    docker-compose up

To import the '/backend/import-accounts.csv' file, add the environment variable when running docker-compose

    IMPORT_FILE=import-accounts.csv docker-compose up

## API endpoints

### /auth/login
Logs in a user with the given username and password.

- Example request:

      POST /auth/login?username=mock_username&password=mock_password

   Replace my_username and my_password with actual values

- Successful response:

      HTTP/1.1 200 OK
      Content-Type: application/json
      
      {
         "value": "abc123"
      }
  The response body contains a JWT token in the `value` field.
   
  Use this token for authenticated endpoints by providing it as `Authorization` header with contents

     Bearer jwtToken

  Where `jwtToken` is the token described above.

### /auth/account
Retrieves the account details of the currently authenticated user.

- Example request:
   
      GET /auth/account
      Authorization: Bearer <your_token_here>

- Example response for Admin account:

      HTTP/1.1 200 OK
      Content-Type: application/json

      {
        "id": 123456789,
        "email": "admin@test.com",
        "role": "ROLE_ADMIN"
      }
- Example response for Merchant account:
      
      HTTP/1.1 200 OK
      Content-Type: application/json

      {
        "id": 123456789,
        "email": "merchant@test.com",
        "role": "ROLE_MERCHANT",
        "name": "Merchant Name",
        "description": "Merchant description",
        "status": "MERCHANT_ACTIVE",
        "totalTransactionSum": 12.34,
      }

### GET /merchants/
Retrieves all the merchants.

- Example request:
        
        GET /merchants/
        Authorization: Bearer <your_token_here>

- Example response:

        HTTP/1.1 200 OK
        Content-Type: application/json
        
        {
          "value": [
            {
              "id": 1,
              "email": "merchant1@example.com",
              "role": "ROLE_MERCHANT",
              "name": "Merchant 1",
              "description": "Description 1",
              "status": "MERCHANT_ACTIVE",
              "totalTransactionSum": 990.50
            },
            {
              "id": 2,
              "email": "merchant2@example.com",
              "role": "ROLE_MERCHANT",
              "name": "Merchant 2",
              "description": "Description 2",
              "status": "MERCHANT_INACTIVE",
              "totalTransactionSum": 300.00
            }
            //... more MerchantDTO objects
          ]
        }

### GET /merchants/{id}
Retrieve a specific merchant by ID.

- Example request:
        
        GET /merchants/1
        Authorization: Bearer <your_token_here>

- Example response:

        HTTP/1.1 200 OK
        Content-Type: application/json

        {
          "id": 1,
          "email": "merchant1@example.com",
          "role": "ROLE_MERCHANT",
          "name": "Merchant 1",
          "description": "Description 1",
          "status": "MERCHANT_ACTIVE",
          "totalTransactionSum": 990.50
       }

### POST /merchants/
Create a new merchant.
- Note that `id` must not be provided for this request.

- Example request:
        
        POST /merchants/
        Content-Type: application/json
        Authorization: Bearer <your_token_here>
        Body: {
          "merchant": {
            "email": "newmerchant@example.com",
            "name": "New Merchant",
            "description": "New Description",
            "status": "MERCHANT_ACTIVE"
          },
          "password": "password123"
        }

- Example response:

        HTTP/1.1 200 OK
        Content-Type: application/json
        
        {
          "id": 3,
          "email": "newmerchant@example.com",
          "role": "ROLE_MERCHANT",
          "name": "New Merchant",
          "description": "New Description",
          "status": "MERCHANT_ACTIVE"
        }

### PATCH /merchants/
Update an existing merchant.
 - Note that `id` must be provided for this request.
 - Only the fields that are provided will be updated.
 - Example request:

        PATCH /merchants/
        Content-Type: application/json
        Authorization: Bearer <your_token_here>
        Body: {
          "merchant": {
            "id": 1,
            "email": "updatedmerchant@example.com",
            "name": "Updated Merchant",
            "description": "Updated Description",
            "status": "MERCHANT_INACTIVE"
          },
          "password": "updatedPassword123"
        }

 - Example response:

         HTTP/1.1 200 OK
         Content-Type: application/json
        
         {
           "id": 1,
           "email": "updatedmerchant@example.com",
           "role": "ROLE_MERCHANT",
           "name": "Updated Merchant",
           "description": "Updated Description",
           "status": "MERCHANT_ACTIVE",
           "totalTransactionSum": 1000.0
         }

### DELETE /merchants/{id}
Delete a merchant by ID.
- Merchant with related transactions cannot be deleted.
- Example request:

        DELETE /merchants/1
        Authorization: Bearer <your_token_here>

- Successful response is always:
        
        HTTP/1.1 200 OK
        Content-Type: application/json
        
        {
          "value": true
        }

### GET /transaction/
Retrieve all transactions for the authenticated account.
- If the authenticated user is admin, all transactions are returned.
- If the authenticated user is a merchant, all transactions that belong to that merchant are returned.
- The resulting transactions are sorted by timestamp in descending order for merchants.

- Example request:

        GET /transaction/
        Authorization: Bearer <your_token_here>

- Example response:

        HTTP/1.1 200 OK
        Content-Type: application/json
        
        {
            "value": [
                {
                    "id": 2,
                    "uuid": "123e4567-e89b-12d3-a456-426614174001",
                    "status": "TRANSACTION_REFUNDED",
                    "timestamp": 1645474148000,
                    "customerEmail": "customer2@example.com",
                    "customerPhone": "0987654321",
                    "referenceId": 1,
                    "amount": 400.50,
                    "type": "TRANSACTION_CHARGE",
                    "merchant": {
                        "id": 1,
                        "email": "merchant1@example.com",
                        "role": "ROLE_MERCHANT",
                        "name": "Merchant 1",
                        "description": "Description 1",
                        "status": "MERCHANT_ACTIVE",
                        "totalTransactionSum": 990.50
                    }
                },
                {
                    "id": 1,
                    "uuid": "123e4567-e89b-12d3-a456-426614174000",
                    "status": "TRANSACTION_APPROVED",
                    "timestamp": 1645473138000,
                    "customerEmail": "customer1@example.com",
                    "customerPhone": "1234567890",
                    "amount": 400.50,
                    "type": "TRANSACTION_AUTHORIZE",
                    "merchant": {
                        "id": 1,
                        "email": "merchant1@example.com",
                        "role": "ROLE_MERCHANT",
                        "name": "Merchant 1",
                        "description": "Description 1",
                        "status": "MERCHANT_ACTIVE",
                        "totalTransactionSum": 990.50
                    }
                },
                
                //... more TransactionDTO objects
            ]
        }

### POST /transaction/
Submit a transaction for processing.
- If the authenticated user is admin, a {@code merchant.id} is required to be provided.
- If the authenticated user is a merchant, the transaction is created for the merchant.

- Example request:

        POST /transaction/
        Content-Type: application/json
        Authorization: Bearer <your_token_here>
        Body: {
          "customerEmail": "customer1@example.com",
          "customerPhone": "1234567890",
          "referenceId": 1,
          "amount": 100.00,
          "type": "TRANSACTION_CHARGE",
          "merchant": {
              "id": 1,
          }
        }

- Example response:

        HTTP/1.1 200 OK
        Content-Type: application/json
        
        {
            "id": 3,
            "uuid": "123e4567-e89b-12d3-a456-426614174000",
            "status": "TRANSACTION_APPROVED",
            "timestamp": 1645473138000,
            "customerEmail": "customer1@example.com",
            "customerPhone": "1234567890",
            "referenceId": 1,
            "amount": 100.00,
            "type": "TRANSACTION_AUTHORIZE",
            "merchant": {
                "id": 1,
                "email": "merchant1@example.com",
                "role": "ROLE_MERCHANT",
                "name": "Merchant 1",
                "description": "Description 1",
                "status": "MERCHANT_ACTIVE",
                "totalTransactionSum": 1090.50
            }
        }

