# emerchantpay
Emerchantpay task

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

