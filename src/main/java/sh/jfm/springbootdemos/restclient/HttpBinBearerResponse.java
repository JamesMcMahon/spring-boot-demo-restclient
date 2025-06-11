package sh.jfm.springbootdemos.restclient;

/**
 * Response payload for httpbin / bearer endpoint.
 * <a href="https://httpbin.org/#/Auth/get_bearer">HttpBin docs</a>
 */
public record HttpBinBearerResponse(boolean authenticated, String token) {
}
