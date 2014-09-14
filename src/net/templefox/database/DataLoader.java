package net.templefox.database;

import java.util.List;

import com.google.gson.JsonArray;
import com.koushikdutta.async.future.FutureCallback;

import net.templefox.database.data.Entity;

public interface DataLoader {
	/**
	 * The implementation should <b>save</b> the new loaded data into all rest
	 * data, which means assuming the function users will <b>ignore</b> the
	 * return type.
	 * 
	 * @return The new loaded data.
	 */
	List<? extends Entity> loadLocalData(LoadDataListener loadDataListener);

	/**
	 * The implementation should <b>save</b> the new loaded data into all rest
	 * data, which means assuming the function users will <b>ignore</b> the
	 * return type.
	 * 
	 * @return The new loaded data.
	 */
	List<? extends Entity> loadRemoteDate(FutureCallback<JsonArray> listener, String query);
}
