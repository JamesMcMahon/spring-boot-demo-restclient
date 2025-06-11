package sh.jfm.springbootdemos.restclient;

/// Response for [HTTPBin GET `/bearer`](https://httpbin.org/#/Auth/get_bearer)
/// – echoes authentication outcome and token.
public record HttpBinBearerResponse(boolean authenticated, String token) {
}
