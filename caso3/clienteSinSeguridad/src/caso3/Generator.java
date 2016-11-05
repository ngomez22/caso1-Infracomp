package caso3;

import uniandes.gload.core.*;

public class Generator {
	
	private LoadGenerator generator;
	
	private int[] timeout = {20, 40, 100};
	private int[] transactions = {400, 200, 80};
	
	private static int fallas;
	
	public Generator() {
		fallas = 0;
		for(int i=0; i<timeout.length; i++){
			String file = "./data/datos-"+transactions[i]+"-"+timeout[i]+".csv";
			Task work = createTask(file);
			generator = new LoadGenerator("Prueba", transactions[i], work, timeout[i]);
			generator.generate();
			fallas = 0;
		}
	}

	private Task createTask(String route) {
		return new ClienteTask(route);
	}

	public static void registrarFalla(){
		fallas++;
	}
	public static void main(String[] args) {
		@SuppressWarnings("unused")
		Generator g = new Generator();
	}

}
