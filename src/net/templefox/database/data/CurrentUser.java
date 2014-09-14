package net.templefox.database.data;

import net.templefox.misc.OnUserChangedListener;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.EBean.Scope;

@EBean(scope= Scope.Singleton)
public class CurrentUser extends User {
	private static final long serialVersionUID = -4554986716912397784L;
	OnUserChangedListener _listener;
	@Override
	public void setId(Integer id) {
		super.setId(id);
		if (_listener!=null) {
			_listener.onUserChanged(id);			
		}
	}
	
	public void setOnUserChangedListener(OnUserChangedListener listener){
		this._listener = listener;
	}
}
