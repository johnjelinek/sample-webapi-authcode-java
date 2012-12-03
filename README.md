# sample-webapi-authcode-java

This sample application uses JavaFX to display a browser window where a user can log in with their Tradestation credentials. After a successful login, an authorization code is returned. This authorization code is then exchanged for an access token which will be used for subsequent WebAPI calls.

## Configuration
Modify the following with your appropriate values:

    final String APIKey = "your key goes here";
    final String APISecret = "your secret goes here";
    final String RedirectUri = "your redirect uri here";
    
Then build an execute the sample.