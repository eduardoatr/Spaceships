package spaceships;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Eduardo Vieira e Sousa
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        FileWriter saida = null;
        try {

            String in; // Caminho do arquivo de entrada
            String out; // Caminho do arquivo de saída

            // Trata erro nas entradas
            if (args.length != 2) {
                System.out.println("ERRO: Numero incorreto de parâmetros");
                System.exit(1);
            }

            // Atribui os caminhos de entrada
            in = args[0];
            out = args[1];

            // Instancia as classes
            Radar radar = new Radar();
            Relatorio identificador = new Relatorio();

            // Inicia a identificação
            radar.sondarFrotas(in);

            // Calcula os tempos de vantagem
            identificador.buscar(radar.getFrota());

            saida = new FileWriter(out);
            PrintWriter gravarArq = new PrintWriter(saida);

            gravarArq.printf(identificador.getTipos()[0] + " ");
            gravarArq.printf(identificador.getTipos()[1] + " ");
            gravarArq.printf(identificador.getTipos()[2] + " ");
            gravarArq.printf(identificador.getTipos()[3] + "%n");
            gravarArq.printf(identificador.getTempoMin() + "");

            gravarArq.close();

        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                saida.close();
            } catch (IOException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
