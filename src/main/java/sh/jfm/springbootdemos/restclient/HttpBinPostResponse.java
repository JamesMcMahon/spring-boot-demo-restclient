package sh.jfm.springbootdemos.restclient;

/// Response for [HTTPBin POST `/post`](https://httpbin.org/#/HTTP_Methods/post_post)
/// – echoes incoming JSON under `.json`.
public record HttpBinPostResponse(ExamplePostRequest json) {
}
