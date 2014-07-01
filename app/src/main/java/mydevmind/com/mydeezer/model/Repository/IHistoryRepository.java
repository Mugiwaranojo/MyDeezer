package mydevmind.com.mydeezer.model.Repository;

import java.util.ArrayList;

/**
 * Created by Fitec on 30/06/2014.
 */
public interface IHistoryRepository {

    public boolean addToHistory(String request);

    public ArrayList<String> getRequestMatchingPredicate(String predicate);
}
