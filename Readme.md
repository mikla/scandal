Postgresql:

```shell
docker run -p5432:5432  --name scandal-db -e POSTGRES_PASSWORD=mysecretpassword -d postgres
```

Create DB
```shell
docker exec -i scandal-db  createdb -h localhost -p 5432 -U postgres scandal
```

Import DB schema

```shell
docker exec -i scandal-db psql -U postgres -d scandal < schema.sql
```

App:

```shell
sbt "runMain io.scandal.Main"
```

```shell
./simulate.sh
```

Note on behaviour
In the end I've abandoned idea of having `clientId`, so I make very stupid thing, basically, for each ad I calculate ration of 
clicks / impressions. This ration is stored in `view` called `ctr`. View automatically re-buils each time we receive click or impressions.
`get` is just returning an AD with the highest ctr.

What to keep mind
- `LocalDateTime` used to simplicity, in real word we'd better take care of timezones as well.
- `Cask` is sync, which means a lot of http calls may degrade system performance. Proper http server like `akka-http` or
  `http4s` or `zio-http` are preferable as we can configure thread pools for blocking operations more carefully.

Next improvements

- Enrich `Advertisement` with more meta data, e.g. `keywords`, `budget`, `targetAudience`, `targetDevice` and more.
- Fix `get` for return not just highest CTR ad.
- Provide tests
- Introduce separate http controller (now everything is in Main)
- Do not create `service` instance on each http request.