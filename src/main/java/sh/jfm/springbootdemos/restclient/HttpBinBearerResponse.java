package sh.jfm.springbootdemos.restclient;

/// Response payload for httpbin / bearer endpoint.
///
/// [HttpBin docs](https://httpbin.org/#/Auth/get_bearer)
public record HttpBinBearerResponse(boolean authenticated, String token) {
}
