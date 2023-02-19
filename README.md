# Start the application
```mvn clean package && docker compose up -d```

Then you will find the app running on 8080, if available of course :)

# Integration Tests

``` mvn clean test```

I'm using test containers to start the database 
to test the graphql endpoints

once the app is running you can access graphiql by ```http://host:port/graphiql```
to try the graphql endpoints