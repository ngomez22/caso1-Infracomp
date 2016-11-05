package caso3;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;

import uniandes.gload.core.*;

public class Generator {

	private LoadGenerator generator;

	private int[] timeout = {20, 40, 100};
	private int[] transactions = {400, 200, 80};

	private static int fallas;

	public Generator() {
		fallas = 0;
		for (int j=1; j<11; j++){
			for(int i=0; i<timeout.length; i++){
				String file = "./data/"+ j + "datos-" + transactions[i] + "-" + timeout[i] + ".csv";
				try {
					Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "utf-8"));
					writer.write("tAutorizacion, tConsulta");
					Task work = createTask(writer);
					generator = new LoadGenerator("Prueba", transactions[i], work, timeout[i]);
					generator.generate();
					writer.write(fallas);
					fallas = 0;
				} catch (IOException e) {
					e.printStackTrace();
				}
				
			}
		}
	}

	private Task createTask(Writer w) {
		return new ClienteTask(w);
	}

	public static void registrarFalla(){
		fallas++;
	}
	public static void main(String[] args) {
		@SuppressWarnings("unused")
		Generator g = new Generator();
	}

}
