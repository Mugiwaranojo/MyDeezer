package mydevmind.com.mydeezer.model.fetcher;

/**
 * Created by Fitec on 27/06/2014.
 */
public interface ConnectionManager {

    public void  performUrlRequest(String url, OnConnectionResultListener listener);
}
