# sample-webapi-authcode-java

This sample application uses JavaFX to display a browser window where a user can log in with their Tradestation credentials. After a successful login, an authorization code is returned. This authorization code is then exchanged for an access token which will be used for subsequent WebAPI calls.

## Configuration
Modify the following with your appropriate values:

    final String APIKey = "your key goes here";
    final String APISecret = "your secret goes here";
    final String RedirectUri = "your redirect uri here";
    
## Build Instructions
* Download and Extract the zip or clone this repo
* Open Eclipse and Import this project as an "Existing Maven Project" (requires [m2e](http://www.eclipse.org/m2e/) for Eclipse)
* Browse for the root directory where the project was extracted and select the project "/pom.xml com.tradestation.com:samplejfxauthcode:0.0.1-SNAPSHOT:jar"
* Finish the Import
* Build and Run

## Troubleshooting
If you run into any access restrictions with any imported packages, then remove JRE Sstem Library from your Eclipse Java Build path and then re-add it. Be sure that your JRE System Librar is pointing to the latest JDK instead of JRE. If there are futher problems, open an [issue](https://github.com/tradestation/sample-webapi-authcode-java/issues) and we'll take a look!