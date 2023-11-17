# emerchantpay
Emerchantpay task

## Setup DB

Create schema:

    create schema emerchantpay collate utf8mb4_0900_ai_ci;

To import initial accounts, set environment property 'emerchantpay.import.csv.file' to contain absolute path to the import csv file.
Example file can be found in 'backend/import-accounts.csv'

    emerchantpay.import.csv.file=absolute-path/backend/import-accounts.csv

If this is set and no other accounts exist in the DB, the service will try to import the accounts listed in the CSV file at startup.

## Run

If not using IDE run directly with java:

    java -Dserver.port=8080 -Dspring.profiles.active=main -jar backend.jar

If Using IDE, make sure to set the active spring profile to 'main'.