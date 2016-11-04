package caso3;

import uniandes.gload.core.*;

public class Generator {
	
	private LoadGenerator generator;
	
	private int[] timeout = {20, 40, 100};
	private int[] transactions = {400, 200, 80};
	
	public Generator() {
		for(int i=0; i<timeout.length; i++){
			String file = "./data/datos-"+transactions[i]+"-"+timeout[i]+".csv";
			int fallas = 0;
			Task work = createTask(fallas, file);
			generator = new LoadGenerator("Prueba", transactions[i], work, timeout[i]);
			generator.generate();
		}
	}

	private Task createTask(int fallas, String route) {
		return new ClienteTask(fallas, route);
	}

	public static void main(String[] args) {
		@SuppressWarnings("unused")
		Generator g = new Generator();
	}

}
