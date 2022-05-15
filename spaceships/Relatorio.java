package spaceships;

import java.util.ArrayList;
import java.util.Stack;

/**
 * Analisa os dados obtidos pelo Radar, gerando um relatório com os tipos de
 * naves e tempo de vantagem.
 * O desejo do quartel general é uma ordem.
 *
 * @author Eduardo Vieira e Sousa
 */
public class Relatorio {

    private int[] tipos; // Vetor para contabilizar a quantia de cada tipo de nave (0: Reconhecimento, 1:
                         // Frigata, 2: Bombardeiro, 3: Trasportador)
    private long tempoMin; // Guarda o tempo de vantagem mínimo
    private ArrayList<PostoDeCombate> frota; // Ponteiro para a frota de combate

    /**
     * Identifica a quantidade e os tipos de naves em cada frota utilizando o DFS,
     * depois faz o cálculo do tempo de vantagem.
     * O quartel general vai ficar orgulhoso!(?)
     *
     * @param frota Lista contendo os vértices (PostoDeCombate) do grafo de entrada
     */
    public void buscar(ArrayList<PostoDeCombate> frota) {

        int nrVertices, nrArestas, maiorGrau; // Contadores para a quantidade de vértices, arestas e maior grau de cada
                                              // nave
        int tipoAtual; // Guarda o tipo da nave atual
        PostoDeCombate postoAnterior, postoAtual; // Ponteiros para iterar nos postos da nave
        Stack<PostoDeCombate> pilha = new Stack<PostoDeCombate>(); // Pilha para execução do DFS
        ArrayList<PostoDeCombate> naveAtual = new ArrayList<PostoDeCombate>(); // Lista que guarda os postos da nave
                                                                               // para o cálculo do tempo de vantagem

        // Estruturas para o cálculo do LCA
        int altura; // Guarda a altura da árvore

        // Inicia as estruturas para uma nova busca
        tipos = new int[4];
        tempoMin = -1;
        this.frota = frota;

        // Reseta a cor para a busca em profundidade
        for (int i = 0; i < frota.size(); i++) {
            frota.get(i).setCorDFS(0);
        }

        // Aplica o DFS na frota
        for (int i = 0; i < frota.size(); i++) {

            // Se o vértice ainda não foi visitado, temos uma nova componente(nave)
            if (frota.get(i).getCorDFS() == 0) {

                // Reseta as variáveis para identificação de uma nova nave
                nrVertices = 0;
                nrArestas = 0;
                maiorGrau = -1;
                naveAtual.clear();

                // Adiciona o vértice inicial na pilha e arruma distância
                postoAtual = frota.get(i);
                pilha.push(postoAtual);
                postoAtual.setDistanciaDFS(-1);

                // Ajusta as estruturas do LCA
                postoAtual.setPai(null);
                postoAtual.setNivel(0);
                altura = 0;

                // Explora os vizinhos do posto atual
                while (!pilha.isEmpty()) {

                    // Atualiza ponteiro
                    postoAnterior = postoAtual;

                    // Remove o primeiro da pilha e adiciona na nave
                    postoAtual = pilha.pop();
                    naveAtual.add(postoAtual);

                    // Seta cor cinza e distância
                    postoAtual.setCorDFS(1);
                    postoAtual.setDistanciaDFS(postoAnterior.getDistanciaDFS() + 1);

                    // Seta o pai do vértice, caso ele não seja o raiz
                    if (postoAtual.getPai() != null)
                        postoAtual.setNivel(postoAtual.getPai().getNivel() + 1);

                    // Atualiza a altura da árvore
                    if (postoAtual.getNivel() > altura)
                        altura = postoAtual.getNivel();

                    // Explora a lista de adjacência
                    for (int j = 0; j < postoAtual.getTeleports().size(); j++) {

                        // Incrementa o contador de arestas
                        nrArestas++;

                        // Se o vértice ainda não tiver sido explorado
                        if (postoAtual.getTeleports().get(j).getCorDFS() == 0) {

                            // Seta cor cinza
                            postoAtual.getTeleports().get(j).setCorDFS(1);

                            // Adiciona à pilha
                            pilha.push(postoAtual.getTeleports().get(j));

                            // Seta o pai do vértice
                            postoAtual.getTeleports().get(j).setPai(postoAtual);
                        }
                    }

                    // Seta cor preta e incrementa o contador de vértices
                    postoAtual.setCorDFS(2);
                    nrVertices++;

                    // Se a nave possuir algum vértice com grau maior que 2, temos uma frigata
                    if (postoAtual.getTeleports().size() > maiorGrau) {
                        maiorGrau = postoAtual.getTeleports().size();
                    }
                }

                // Cada aresta é contada 2 vezes pois está presente em 2 listas de adjacência,
                // então dividimos a contagem por 2
                nrArestas = nrArestas / 2;

                // Ajusta a altura pois no nível é contado à partir do zero
                altura++;

                // Identifica o tipo da nave e atualiza o contador
                tipoAtual = identificaNave(nrVertices, nrArestas, maiorGrau);
                tipos[tipoAtual] = tipos[tipoAtual] + 1;

                // Calcula o tempo de vantagem da naveAtual utilizando o BFS
                tempoMin = tempoDeVantagem(naveAtual, tipoAtual, altura, tempoMin);
            }
        }

    }

    /**
     * Identifica uma nave através do número de vértices, arestas e maior grau de
     * seus vértices.
     *
     * @param vertices Número de vértices da nave
     * @param arestas  Número de arestas da nave
     * @param grau     Maior grau dentre os vértices
     *
     * @return Tipo da nave (0: Reconhecimento, 1: Frigata, 2: Bombardeiro, 3:
     *         Trasportador)
     */
    private int identificaNave(int vertices, int arestas, int grau) {

        // Se o número de vértices for igual ao número de arestas, temos um
        // transportador
        if (vertices == arestas) {
            return 3;
        } else {
            // Se o número de arestas for maior que o número de vértices, temos um
            // bombardeiro
            if (arestas > vertices) {
                return 2;
            } else {
                // Se grau máximo for maior que 2, temos uma frigata
                if (grau > 2) {
                    return 1;
                } else {
                    // Caso contrário, temos uma nave de reconhecimento
                    return 0;
                }
            }
        }
    }

    /**
     * Recebe uma nave e vetor contendo as posições inimigas e retorna o tempo de
     * vantagem para a nave de entrada.
     *
     * @param nave     Conjunto de vértices conectados que representam uma única
     *                 nave
     * @param tipo     Número indicando o tipo de nave recebida
     * @param altura   Altura da árvore
     * @param tempoMin Menor tempo de vantagem calculado até o momento
     *
     * @return Tempo de vantagem para a nave de entrada
     */
    private long tempoDeVantagem(ArrayList<PostoDeCombate> nave, int tipo, int altura, long tempoMin) {

        // Antes de tudo, se um mínimo de 0 já foi encontrado, retorna
        if (tempoMin == 0) {
            return 0;
        }

        PostoDeCombate postoInicial, postoAtual; // Ponteiros para fazer as buscas na nave
        long somaDistancias, distancia; // Para o cálculo das distâncias do tempo de vantagem

        // Reseta o valor das distâncias
        somaDistancias = 0;
        int cont = 0;

        // Inicia as estruturas para o cálculo do LCA
        int[] particao = new int[frota.size()]; // Vetor contendo as partições onde cada vértice se localiza
        int nrVertice, LCA; // Guardam o índice do nodo e do LCA
        int vertice1, vertice2;

        // Para cada tipo de nave, existe um método mais eficiente de busca
        switch (tipo) {

            case 0: // Para as naves de reconhecimento, basta encontrar um nodo das extremidades,
                    // fazer um BFS obtendo as distâncias de uma raiz qualquer
                    // A distância entre 2 vértices é o valor absoluto da distância até primeiro
                    // menos distância até o segundo

                // Procura um nodo da ponta da nave(vértice de grau 1)
                do {
                    postoInicial = nave.get(cont);
                    cont++;
                } while (postoInicial.getTeleports().size() > 1);

                // Faz o BFS
                BFS(postoInicial);

                // Faz o cálculo da distância para todos os vértices
                for (int i = 0; i < nave.size(); i++) {

                    postoAtual = nave.get(i);

                    // Se o vértice não estiver no lugar correto
                    if (postoAtual.getNumero() != postoAtual.getPosto().getNumero()) {

                        // Faz o cálculo
                        somaDistancias += Math
                                .abs(postoAtual.getDistanciaBFS() - postoAtual.getPosto().getDistanciaBFS());

                        // Se o tempo calculado já for maior ou igual que o mínimo, retorna a mínimo
                        if ((somaDistancias / 2) >= tempoMin && tempoMin >= 0) {
                            return tempoMin;
                        }
                    }
                }

                break;

            case 1: // Para frigatas, basta calcular o LCA, em seguida fazer um BFS obtendo as
                    // distâncias de uma raiz qualquer
                    // A distância entre 2 vértices é a soma da distância até os dois vértices,
                    // menos 2 vezes a distância até o LCA

                // Calcula a raiz quadrada da altura para obter o tamanho da partição
                altura = (int) Math.sqrt(altura);

                // Calcula a partição de cada vértice
                for (int i = 0; i < nave.size(); i++) {

                    nrVertice = nave.get(i).getNumero();

                    // Se seu nível for menor do que a raiz da altura, ele fica na primeira partição
                    if (frota.get(nrVertice).getNivel() < altura) {
                        particao[nrVertice] = 1;
                    } else {
                        // Caso contrário, a sua partição é o seu pai
                        if ((frota.get(nrVertice).getNivel() % altura) == 0) {
                            particao[nrVertice] = frota.get(nrVertice).getPai().getNumero();
                        } else {
                            // Caso contrário, a sua partição é a mesma de seu pai
                            particao[nrVertice] = particao[frota.get(nrVertice).getPai().getNumero()];
                        }
                    }
                }

                // Recupera um nodo qualquer
                postoInicial = nave.get(0);

                // Faz o BFS
                BFS(postoInicial);

                // Calcula as distâncias utilizando o LCA
                for (int i = 0; i < nave.size(); i++) {

                    postoAtual = nave.get(i);

                    // Recupera os números dos dos dois vértices
                    vertice1 = postoAtual.getNumero();
                    vertice2 = postoAtual.getPosto().getNumero();

                    // Se o vértice já não estiver no lugar correto
                    if (vertice1 != vertice2) {

                        // Soma as distancias
                        distancia = frota.get(vertice1).getDistanciaBFS() + frota.get(vertice2).getDistanciaBFS();

                        // Procura o LCA
                        while (particao[vertice1] != particao[vertice2]) {
                            if (frota.get(vertice1).getNivel() > frota.get(vertice2).getNivel()) {
                                vertice1 = particao[vertice1];
                            } else {
                                vertice2 = particao[vertice2];
                            }
                        }

                        // Procura o LCA
                        while (vertice1 != vertice2) {
                            if (frota.get(vertice1).getNivel() > frota.get(vertice2).getNivel()) {
                                vertice1 = frota.get(vertice1).getPai().getNumero();
                            } else {
                                vertice2 = frota.get(vertice2).getPai().getNumero();
                            }
                        }

                        LCA = vertice1;

                        // Subtrai o caminho até o LCA duas vezes, pois ele foi somado duas vezes
                        distancia = distancia - (2 * frota.get(LCA).getDistanciaBFS());

                        // Faz a soma das distâncias
                        somaDistancias += distancia;

                        // Se o tempo calculado já for maior ou igual que o mínimo, retorna a mínimo
                        if (((somaDistancias / 2) >= tempoMin) && (tempoMin >= 0)) {
                            return tempoMin;
                        }
                    }
                }

                break;

            case 2: // Para bombardeiros, se o nodo não for vizinho, então a distância é 2
                    // Deste modo basta olhar na primeira lista de adjacência, se o segundo nodo não
                    // estiver nela, então é da outra partição

                postoAtual = nave.get(0);

                // Define as partições: mesma partição que o nodo inicial = 0, outra partição =
                // 1
                for (int i = 0; i < postoAtual.getTeleports().size(); i++) {
                    particao[postoAtual.getTeleports().get(i).getNumero()] = 1;
                }

                // Faz o cálculo para todos os vértices
                for (int i = 0; i < nave.size(); i++) {

                    postoAtual = nave.get(i);

                    // Se o vértice já não estiver no lugar correto
                    if (postoAtual.getNumero() != postoAtual.getPosto().getNumero()) {
                        if (particao[postoAtual.getNumero()] == particao[postoAtual.getPosto().getNumero()]) { // Se a
                                                                                                               // partição
                                                                                                               // for a
                                                                                                               // mesma,
                                                                                                               // a
                                                                                                               // distância
                                                                                                               // é 2
                            somaDistancias += 2;
                        } else { // Caso contrário, é 1
                            somaDistancias += 1;
                        }

                        // Se o tempo calculado já for maior ou igual que o mínimo, retorna a mínimo
                        if ((somaDistancias / 2) >= tempoMin && tempoMin >= 0) {
                            return tempoMin;
                        }
                    }
                }

                break;
            // OK
            case 3: // Para os transportadores, basta fazer um BFS e fazer a diferença das
                    // distâncias
                    // Caso a distância seja maior que a metade do total de vértices, o caminho
                    // correto é o na direção oposto

                // Faz o cálculo da distância para todos os vértices
                for (int i = 0; i < nave.size(); i++) {

                    postoAtual = nave.get(i);

                    // Se o vértice já não estiver no lugar correto
                    if (postoAtual.getNumero() != postoAtual.getPosto().getNumero()) {

                        // Faz o cálculo
                        distancia = Math.abs(postoAtual.getDistanciaDFS() - postoAtual.getPosto().getDistanciaDFS());

                        // Se a distância do caminho for maior que a quantidade de vértices dividida por
                        // 2
                        if (distancia > nave.size() / 2) {
                            distancia = nave.size() - distancia; // O caminho correto são as arestas que sobram
                        }

                        // Faz o cálculo
                        somaDistancias += distancia;

                        // Se o tempo calculado já for maior ou igual que o mínimo, retorna a mínimo
                        if ((somaDistancias / 2) >= tempoMin && tempoMin >= 0) {
                            return tempoMin;
                        }
                    }
                }

                break;
            default:

                System.out.println("Erro: tipo de nave desconhecido");
        }

        // Calcula o tempo de vantagem e retorna
        return somaDistancias / 2;
    }

    /**
     * Faz a busca em largura à partir de uma raiz.
     *
     * @param vertice Vértice raiz de onde será inicia a busca
     */
    public void BFS(PostoDeCombate vertice) {

        ArrayList<PostoDeCombate> fila = new ArrayList<PostoDeCombate>(); // Fila para a verificação de nodos do BFS
        PostoDeCombate nodoAtual, vizinhoAtual; // Ponteiros para fazer a busca

        // Reseta a fila
        fila.clear();

        // Inicia o nodo de onde será feita a busca
        vertice.setCorBFS(1);
        vertice.setDistanciaBFS(0);

        // Adiciona na lista
        fila.add(vertice);

        // Faz a busca em largura na nave
        while ((!fila.isEmpty())) {

            // Remove o primeiro nodo da fila (FIFO)
            nodoAtual = fila.remove(0);

            // Verifica a lista de adjacência do nodo
            for (int v = 0; v < nodoAtual.getTeleports().size(); v++) {

                vizinhoAtual = nodoAtual.getTeleports().get(v);

                // Se o vizinho ainda não foi visitado
                if (vizinhoAtual.getCorBFS() == 0) {

                    // Seta como visitado e calcula distância
                    vizinhoAtual.setCorBFS(1);
                    vizinhoAtual.setDistanciaBFS(nodoAtual.getDistanciaBFS() + 1);

                    // Adiciona na fila
                    fila.add(vizinhoAtual);
                }
            }

            // Seta o nodo como fechado
            nodoAtual.setCorBFS(2);
        }
    }

    // Getters & Setters
    public int[] getTipos() {
        return tipos;
    }

    public long getTempoMin() {
        return tempoMin;
    }

}
