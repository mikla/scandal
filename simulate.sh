#!/usr/bin/env sh

echo "Creating ads"

for i in {1..7}; do
  arr[i - 1]="id-$i"
  curl --location --request POST 'localhost:8080/api/v1/ad/create' \
    --header 'Content-Type: application/json' \
    --data-raw '{
     "id": "id-'"$i"'",
     "name": "id-'"$i"'-name",
     "adText": "ia-'"$i"'-text"
  }'
done

echo "Registering impressions"

for i in {1..1000}; do
  adId=${arr[$RANDOM % 7]}

  curl --location --request POST 'localhost:8080/api/v1/impression/register' \
    --header 'Content-Type: application/json' \
    --data-raw '{
   "adId": "'"$adId"'"
  }'
done


echo "Registering clicks"

for i in {1..100}; do
  adId=${arr[$RANDOM % 7]}

  curl --location --request POST 'localhost:8080/api/v1/click/register' \
    --header 'Content-Type: application/json' \
    --data-raw '{
   "adId": "'"$adId"'"
  }'
done
