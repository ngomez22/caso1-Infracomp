package caso3;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.Writer;

import uniandes.gload.core.*;

public class Generator {

	private LoadGenerator generator;

	private int[] timeout = {20, 40, 100};
	private int[] transactions = {400, 200, 80};

	private static int fallas;
	private static int terminados;

	public Generator() {
		terminados = 0;
		int i=2;
		fallas = 0;
		String path = "./data/"+ 1 + "datos-" + transactions[i] + "-" + timeout[i] + ".csv";
		File file = new File(path);
		try(BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
			file.createNewFile();
			bw.write("tAutorizacion(ms), tConsulta(ms)\n");
			Task work = createTask(bw);
			generator = new LoadGenerator("Prueba", transactions[i], work, timeout[i]);
			generator.generate();
			while(terminados<transactions[i]){
			}
			bw.write(""+fallas);
			fallas = 0;
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private Task createTask(BufferedWriter f) {
		return new ClienteTask(f);
	}
	
	public static synchronized void contar() {
		terminados++;
	}

	public static void registrarFalla(){
		fallas++;
	}

	public static void main(String[] args) {
		@SuppressWarnings("unused")
		Generator g = new Generator();
	}
}
