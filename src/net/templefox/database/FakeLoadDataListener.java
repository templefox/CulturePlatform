package net.templefox.database;

public abstract class FakeLoadDataListener {
	private static LoadDataListener listener = new LoadDataListener() {
		@Override
		public void onDone() {
		}
	};

	public static LoadDataListener getFake() {
		return listener;
	}
}
