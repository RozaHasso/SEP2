package table;

public class TableSingleton {

	private static TableSingleton instance;

	private String ip;
	private int port, tableNo;

	private TableSingleton() {

	}

	public static TableSingleton getInstance() {
		if (instance == null)
			instance = new TableSingleton();
		return instance;
	}

	public void prepareData(String args[]) {
		ip = args[0];
		port = Integer.parseInt(args[1]);
		tableNo = Integer.parseInt(args[2]);
	}

	public String getIP() {
		return ip;
	}

	public int getPort() {
		return port;
	}

	public int getTableNo() {
		return tableNo;
	}

}
