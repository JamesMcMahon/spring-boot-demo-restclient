package sh.jfm.springbootdemos.restclient;

public record HttpBinGetResponse(HttpBinGetArgs args) {
    public record HttpBinGetArgs(String name) {
    }
}
