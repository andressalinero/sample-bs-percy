# Pending

## Notes
* You can export the environment variables for the Username and Access Key of your BrowserStack account
* You can export the environment variable for your Percy Project token

  ```
  export BROWSERSTACK_USERNAME=<browserstack-username> &&
  export BROWSERSTACK_ACCESS_KEY=<browserstack-access-key> &&
  export PERCY_TOKEN=<percy-token>
  ```
### Running your tests

- To run functional tests on Browserstack Automate run: <br>`mvn test clean`
- To run functional + visual tests on Browserstack Automate and Percy run: <br>`npx percy exec -- mvn test clean`
