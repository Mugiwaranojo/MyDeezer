package mydevmind.com.mydeezer.model.fetcher;

/**
 * Created by Joan on 27/06/2014.
 * Interface request url and return listener
 */
public interface ConnectionManager {

    public void  performUrlRequest(String url, OnConnectionResultListener listener);
    public void  cancelAll();
    public void  stop();
}
