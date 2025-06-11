package sh.jfm.springbootdemos.restclient;

/// Mirror of [HTTPBin POST `/post`](https://httpbin.org/#/HTTP_Methods/post_post) `json` section;
/// only required field modelled.
public record ExamplePostRequest(String message) {
}
