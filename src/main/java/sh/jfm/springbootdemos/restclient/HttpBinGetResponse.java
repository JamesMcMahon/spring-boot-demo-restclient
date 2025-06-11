package sh.jfm.springbootdemos.restclient;

/// Response body of [HTTPBin GET `/get`](https://httpbin.org/#/HTTP_Methods/get_get)
/// – only `.args` part modelled.
public record HttpBinGetResponse(HttpBinGetArgs args) {
    public record HttpBinGetArgs(String name) {
    }
}
