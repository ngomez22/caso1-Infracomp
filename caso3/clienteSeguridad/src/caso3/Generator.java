package caso3;

import uniandes.gload.core.*;

public class Generator {
	
	private LoadGenerator generator;
	
	private int[] timeout = {20, 40, 100};
	private int[] transactions = {400, 200, 80};
	
	public Generator() {
		Task work = createTask();
		for(int i=0; i<timeout.length; i++){
			generator = new LoadGenerator("Prueba", transactions[i], work, timeout[i]);
			generator.generate();
		}
	}

	private Task createTask() {
		return new ClienteTask();
	}

	public static void main(String[] args) {
		@SuppressWarnings("unused")
		Generator g = new Generator();
	}

}
