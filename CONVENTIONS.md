- The goal of this codebase is to be a demo of the differences between RestClient and RestTemplate. Focus on
  straightforward, well-documented code. Avoid complexity not related to the main demo topic.
- Focus on RestClient and RestTemplate best practices, search for reference documentation if needed.
- Prefer terse comments that focus on the why of what we are doing. Focus on the Spring ecosystem and how it works. When
  appropriate, link to external documentation.
- Use English for comments
- When possible, follow conventions already established in the codebase.
- Use httpbin (https://httpbin.org/) for simulating an external HTTP service that RestClient and RestTemplate talk to.
- Always test new lines of code, any conditional should be covered under test. For this codebase, focus on high-level
  tests.
- Using TDD conventions, write and run tests first to ensure that newly created tests can fail.
- Using TDD conventions, only write one test at a time and then write only the code needed for that test to pass. Do not
  write more code than needed to pass a test.
- Using TDD conventions, after a newly created test passes, revisit the code writing to make it pass for clarity and
  simplicity. Delete any lines not needed to make it pass. When finished re-run the tests to make sure everything still
  passes.
- Use modern Java features - ie, Records over Classes for data classes.
