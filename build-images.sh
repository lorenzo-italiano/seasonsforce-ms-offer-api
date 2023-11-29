mvn clean install

mv target/seasonsforce-ms-offer-api-1.0-SNAPSHOT.jar api-image/seasonsforce-ms-offer-api-1.0-SNAPSHOT.jar

cd api-image

docker build -t offer-api .

cd ../postgres-image

docker build -t offer-db .