package spaceships;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Classe encarregada da leitura dos arquivos de entrada.
 * Um potente radar capaz de detectar a estrutura de teletransportes de uma nave
 * inimiga.
 *
 * @author Eduardo Vieira e Sousa
 */
public class Radar {

    private ArrayList<PostoDeCombate> frota; // Grafo de entrada
    private int nrVertices, nrArestas; // Contadores para o número de vértices e arestas

    /**
     * Faz a leitura das tropas contidas no arquivo passado por parâmetro.
     *
     * @param caminho Path para o arquivo de entrada
     */
    public void sondarFrotas(String caminho) {

        int v1, v2; // Para inserção das aretas nas listas de adjacências dos vértices
        int postoSaida, postoChegada; // Para gerar o vetor com as posições dos tripulantes
        PostoDeCombate teleport; // Para a criação dos vértices
        String linha; // Para leitura linha a linha do arquivo
        String[] splitStr; // Para separar os valores de cada linha

        try {

            // Inicia as estruturas para leitura do arquivo
            FileReader entrada = new FileReader(caminho);
            BufferedReader lerArq = new BufferedReader(entrada);

            // Lê a linha inicial e separa os valores em duas strings utilizando o
            // espaçamento como separador
            linha = lerArq.readLine();
            splitStr = linha.trim().split("\\s+");

            // Atribui os valores dos números de vértices e arestas
            nrVertices = Integer.parseInt(splitStr[0]);
            nrArestas = Integer.parseInt(splitStr[1]);

            // Instancia as estruturas de dados
            frota = new ArrayList<PostoDeCombate>();

            // Cria os vértices (PostoDeCombate) do grafo com a numeração correta e adiciona
            // na frota ordenado
            // Vértice i na posição i-1
            for (int i = 0; i < nrVertices; i++) {
                teleport = new PostoDeCombate(i);
                frota.add(teleport);
            }

            // Adiciona as arestas (teleports) do grafo
            for (int i = 0; i < nrArestas; i++) {

                // Lê linha por linha separando em duas strings
                linha = lerArq.readLine();
                splitStr = linha.trim().split("\\s+");

                // Identifica os dois vértices onde a aresta será inserida
                v1 = Integer.parseInt(splitStr[0]);
                v2 = Integer.parseInt(splitStr[1]);

                // Decrementa pois os índices da lista começam a partir do 0
                v1--;
                v2--;

                // Adiciona ambos os vértices nas respectivas listas de adjacência
                frota.get(v1).addTeleport(frota.get(v2));
                frota.get(v2).addTeleport(frota.get(v1));
            }

            // Insere os pontos de combate para a movimentação
            for (int i = 0; i < nrVertices; i++) {

                // Lê linha por linha separado em duas strings
                linha = lerArq.readLine();
                splitStr = linha.trim().split("\\s+");

                // Identifica o posto de partida (primeiro valor) e o posto de chegada (segundo
                // valor) de cada tripulante
                postoSaida = Integer.parseInt(splitStr[0]);
                postoChegada = Integer.parseInt(splitStr[1]);

                // Decrementa pois os índices do vetor começam a partir do 0
                postoSaida--;
                postoChegada--;

                // Insere os valores no vetor
                frota.get(postoSaida).setPosto(frota.get(postoChegada));
            }

            entrada.close();

        } catch (IOException e) {
            System.err.printf("Erro na abertura do arquivo: %s.\n", e.getMessage());
            System.out.println();
        }

    }

    public void fabricaDeNaves(int tipoNave, long nrVertices, long nrMovimentos, String caminho) {

        long nrArestas;

        try {
            FileWriter saida = new FileWriter(caminho);
            PrintWriter gravarArq = new PrintWriter(saida);

            switch (tipoNave) {

                case 0:

                    nrArestas = nrVertices - 1;
                    gravarArq.printf("" + nrVertices + " " + nrArestas + "%n");

                    // Gera as arestas
                    for (long i = 1; i <= nrArestas; i++) {
                        gravarArq.printf("" + i + " " + (i + 1) + "%n");
                    }

                    // Gera os movimentos
                    for (long i = 1; i <= nrVertices / 2; i++) {
                        gravarArq.printf("" + i + " " + (nrVertices + 1 - i) + "%n");
                        gravarArq.printf("" + (nrVertices + 1 - i) + " " + i + "%n");
                    }

                    break;

                case 1:

                    nrArestas = nrVertices - 1;
                    gravarArq.printf("" + nrVertices + " " + nrArestas + "%n");

                    // Gera as arestas
                    for (long i = 1; i <= nrArestas - 1; i++) {
                        gravarArq.printf("" + i + " " + (i + 1) + "%n");
                    }

                    gravarArq.printf("" + nrArestas / 2 + " " + (nrVertices) + "%n");

                    // Gera os movimentos
                    for (long i = 1; i <= nrVertices / 2; i++) {
                        gravarArq.printf("" + i + " " + (nrVertices + 1 - i) + "%n");
                        gravarArq.printf("" + (nrVertices + 1 - i) + " " + i + "%n");
                    }

                    break;

                case 2:

                    nrArestas = (nrVertices / 2) * (nrVertices / 2);
                    gravarArq.printf("" + nrVertices + " " + nrArestas + "%n");

                    // Gera as arestas
                    for (long i = 1; i <= (nrVertices / 2); i++) {
                        for (long j = (long) (nrVertices / 2) + 1; j <= nrVertices; j++) {
                            gravarArq.printf("" + i + " " + ((j) % nrArestas) + "%n");
                        }
                    }

                    // Gera os movimentos
                    // 2 Movimentos
                    if (nrMovimentos == 1) {
                        for (long i = 1; i <= (nrVertices / 2); i++) {
                            if ((i + nrMovimentos) != (nrVertices / 2)) {
                                gravarArq.printf("" + i + " " + ((i + nrMovimentos) % (nrVertices / 2)) + "%n");
                            } else {
                                gravarArq.printf("" + i + " " + ((i % (nrVertices / 2)) + nrMovimentos) + "%n");
                            }
                        }
                        for (long i = (nrVertices / 2) + 1; i <= nrVertices; i++) {
                            if (i != nrVertices) {
                                gravarArq.printf("" + i + " " + ((i % nrVertices) + nrMovimentos) + "%n");
                            } else {
                                gravarArq.printf(
                                        "" + i + " " + ((i % nrVertices) + nrMovimentos + (nrVertices / 2)) + "%n");
                            }
                        }
                    } else { // 1 Movimento
                        for (long i = 1; i <= (nrVertices / 2); i++) {
                            if ((i + (nrVertices / 2)) != nrVertices) {
                                gravarArq.printf("" + i + " " + ((i + (nrVertices / 2)) % nrVertices) + "%n");
                            } else {
                                gravarArq.printf("" + i + " " + ((i + (nrVertices / 2) % nrVertices)) + "%n");
                            }
                        }
                        for (long i = (nrVertices / 2) + 1; i <= nrVertices; i++) {
                            if (i != nrVertices) {
                                gravarArq.printf("" + i + " " + (i % (nrVertices / 2)) + "%n");
                            } else {
                                gravarArq.printf("" + i + " " + ((i + (nrVertices / 2)) % (nrVertices)) + "%n");
                            }
                        }
                    }

                    break;

                case 3:

                    nrArestas = nrVertices;
                    gravarArq.printf("" + nrVertices + " " + nrArestas + "%n");

                    // Gera as arestas
                    for (long i = 1; i <= nrArestas; i++) {
                        if ((i + 1) != nrArestas) {
                            gravarArq.printf("" + i + " " + ((i + 1) % nrArestas) + "%n");
                        } else {
                            gravarArq.printf("" + i + " " + ((i % nrArestas) + 1) + "%n");
                        }
                    }

                    // Gera os movimentos
                    for (long i = 1; i <= nrVertices; i++) {
                        if ((i + nrMovimentos) != nrVertices) {
                            gravarArq.printf("" + i + " " + ((i + nrMovimentos) % nrVertices) + "%n");
                        } else {
                            gravarArq.printf("" + i + " " + ((i % nrVertices) + nrMovimentos) + "%n");
                        }
                    }

                    break;

                default:

                    System.out.println("Erro: tipo de nave desconhecido");
            }

            gravarArq.close();
        } catch (IOException ex) {
            Logger.getLogger(Radar.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * Imprime o grafo.
     * Método de teste!
     */
    public void printaFrota() {
        System.out.print("\n");
        for (int i = 0; i < frota.size(); i++) {
            System.out.print("Vértice " + i + ": ");
            for (int j = 0; j < frota.get(i).getTeleports().size(); j++) {
                System.out.print(frota.get(i).getTeleports().get(j).getNumero() + " ");
            }
            System.out.print("\n");
        }
        System.out.print("\n");
    }

    public ArrayList<PostoDeCombate> getFrota() {
        return frota;
    }

    // Getters & Setters
    public int getNrVertices() {
        return nrVertices;
    }

    public int getNrArestas() {
        return nrArestas;
    }

}
