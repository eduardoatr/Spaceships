package spaceships;

import java.util.ArrayList;

/**
 * Classe que representa os vértices do grafo.
 * Os postos de combate são ligados por teletransportes que permitem uma rápida
 * movimentação
 * dos tripulantes dentro da nave... embora sejam bastante apertados e
 * desconfortáveis.
 *
 * @author Eduardo Vieira e Sousa
 */
public class PostoDeCombate {

    private final int numero; // Número do vértice
    private int nivel; // Nível onde o vértice se localiza na árvore
    private int particao; // Número da particao (para o cálculo do LCA)

    private int corBFS; // Para execução do BFS (0 = branco, 1 = cinza, 2 = preto)
    private int corDFS; // Para execução do DFS (0 = branco, 1 = cinza, 2 = preto)
    private int distanciaBFS; // Valor da distância utilizando o BFS
    private int distanciaDFS; // Valor da distância utilizando o DFS

    private PostoDeCombate posto; // Posto de combate ao qual ele deve se deslocar
    private PostoDeCombate pai; // Número do vértice pai (para o cálculo do LCA)

    private ArrayList<PostoDeCombate> teleports; // Lista de adjacência

    // Construtor
    public PostoDeCombate(int numero) {
        this.numero = numero;
        teleports = new ArrayList<PostoDeCombate>();
    }

    // Getters & Setters
    public int getNumero() {
        return numero;
    }

    public PostoDeCombate getPosto() {
        return posto;
    }

    public void setPosto(PostoDeCombate posto) {
        this.posto = posto;
    }

    public PostoDeCombate getPai() {
        return pai;
    }

    public void setPai(PostoDeCombate pai) {
        this.pai = pai;
    }

    public int getNivel() {
        return nivel;
    }

    public void setNivel(int nível) {
        this.nivel = nível;
    }

    public int getParticao() {
        return particao;
    }

    public void setParticao(int particao) {
        this.particao = particao;
    }

    public int getCorBFS() {
        return corBFS;
    }

    public void setCorBFS(int corBFS) {
        this.corBFS = corBFS;
    }

    public int getCorDFS() {
        return corDFS;
    }

    public void setCorDFS(int corDFS) {
        this.corDFS = corDFS;
    }

    public int getDistanciaBFS() {
        return distanciaBFS;
    }

    public void setDistanciaBFS(int distanciaBFS) {
        this.distanciaBFS = distanciaBFS;
    }

    public int getDistanciaDFS() {
        return distanciaDFS;
    }

    public void setDistanciaDFS(int distanciaDFS) {
        this.distanciaDFS = distanciaDFS;
    }

    public void addTeleport(PostoDeCombate posto) {
        this.teleports.add(posto);
    }

    public ArrayList<PostoDeCombate> getTeleports() {
        return teleports;
    }

}
