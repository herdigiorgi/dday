package dday.assets;

public class AssetException extends RuntimeException {

    public AssetException(String error, Exception ex) {
        super(error, ex);
    }
    public AssetException(String error) {
        super(error);
    }


}
