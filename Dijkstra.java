/* 

BEATRIZ FAVINI CHICARONI

Objetivos: O programa recebe os arcos de um digrafo e seus respectivos pesos 
e calcula a menor distância entre um vértice origem e um vértice destino.
Os arcos representam os corredores que o robô pode percorrer, o vértice 
origem representa a posição atual do vetor e o vértice destino é onde ele 
precisa chegar

Entrada: O programa recebe o número n de vértices do grafo e o número m
de arestas/arcos. Em seguida, recebe m linhas com a cauda, a cabeça e o peso 
de cada arco. Por último recebe o vértice origem e o vértice destino.

Processamento: O programa utiliza o algoritmo de Dijkstra e uma fila de 
prioridades para calcular a menor distância entre o vértice origem e todos 
os outros do grafo.

Saída: O programa retorna a menor distância entre o vértice origem e o 
vértice destino.

Informações adicionais: O programa representa o grafo através de listas de 
adjacências.

*/

import java.util.LinkedList;
import java.util.Scanner;
import java.util.Locale; 
import java.lang.Math;

//Classe que representa os arcos de um digrafo
class Arco {
	
	//atributo cauda
	private final int cauda;
	public int getCauda() {
		return cauda;
	}
	
	//atributo cabeça
	private final int cabeça;
	public int getCabeça() {
		return cabeça;
	}

	//atributo peso
	private final double peso;
	public double getPeso() {
		return peso;
	}
	
	//construtor da classe
	public Arco (int cauda, int cabeça, double peso) {
		this.cauda = cauda;
		this.cabeça = cabeça;
		this.peso = peso;
	}
	
	@Override
	public String toString() {
		return String.format("%d -> %d (%.2f, arg1)", cauda, cabeça, peso);
	}
}

//Classe que representa um digrafo 
class DigrafoComPeso {
	
	//Numero de vértices do digrafo
	private final int vertices;
	
	//Vetor de listas ligadas que representa o digrafo
	private LinkedList<Arco>[] adj;

	//método get que retorna o numero de vertices
	public int ordem() {
		return vertices;
	}
	
	//método get que retorna a lista
	//ligada de vizinhos de um vértice
	public LinkedList<Arco> adj(int i) {
		return adj[i];
	}
	
	//Construtor da classe
	public DigrafoComPeso (int vertices, int A) {
		this.vertices = vertices;
		adj = new LinkedList[vertices];
		for (int i = 0; i < vertices; i++) {
			adj[i] = new LinkedList<>();
		}
	}
	
	//método que adiciona um arco
	//à estrutura do digrafo
	public void adicArco (Arco e) { 
		adj[e.getCauda()].add(e);
	}
}


//Classe que representa os elementos 
//da lista de prioridade
class HeapElem {
	//Representa o rótulo do vertice
	private int vertice;
	
	//Representa a distância do vértice
	//ao vértice raiz passado para o Dijkstra
	private double prioridade;
	
	//métodos getters e setters
	public int getVertice() {
		return vertice;
	}
	public void setVertice(int vertice) {
		this.vertice = vertice;
	}

	public double getPrioridade() {
		return prioridade;
	}
	public void setPrioridade(double prioridade) {
		this.prioridade = prioridade;
	}

	//Construtor da classe
	public HeapElem (int V, double P) {
		this.vertice = V;
		this.prioridade = P;
	}
}

//Implementação da fila de prioridades
class MinHeap { 
	
	//Heap é o vetor que representa a fila de prioridades
	private HeapElem[] Heap;
	
	//Indices é um vetor que armazena a posição de cada vértice na heap
	private int[] indices;
	
	//Size é o tamanho atual da heap 
	private int size; 
	
	//Maxsize é o tamanho máximo da heap
	private int maxsize; 
	
	private static final int FRONT = 0; 
	
	//Construtor da classe inicializa a fila de prioridades
	public MinHeap(int maxsize) { 
		this.maxsize = maxsize; 
	    this.size = 0; 
	    Heap = new HeapElem[this.maxsize]; 
	    indices = new int[this.maxsize];
	    
	    for (int i = 0; i < maxsize; i++) {
	    	Heap[i] = new HeapElem(i, Double.POSITIVE_INFINITY);
	    	indices[i] = i;
	    }     
	} 
	
	//Método que recebe a posição de um vértice na heap
	//e retorna a posição do pai dele
	private int parent(int pos) { 
		return (int) Math.floor((pos-1)/ 2);//(pos-1) / 2; 
	} 
	
	//Método que recebe a posição de um vértice na heap
	//e retorna a posição do filho esquerdo dele
	private int filho_esquerdo(int pos) { 
		return (2 * pos) + 1; 
	} 
	
	//Método que recebe a posição de um vértice na heap
	//e retorna a posição do filho esquerdo dele
	private int filho_direito(int pos) { 
		return (2 * pos) + 2; 
	} 
	
	//Método que inverte a posição de dois elementos da heap
	//e atualiza o vetor de índices para acompanhar a inversão
	private void inverte(int fpos, int spos) { 
		HeapElem tmp; 
		indices[Heap[fpos].getVertice()] = spos;
		indices[Heap[spos].getVertice()] = fpos;
		tmp = Heap[fpos]; 
		Heap[fpos] = Heap[spos]; 
		Heap[spos] = tmp; 
	} 
    
    //Método que corrige a heap a partir de um índice 
    //passado como parâmetro
	private void corrige_descendo(int pos) { 
		int e = filho_esquerdo(pos);
		int d = filho_direito(pos);
		int menor;
		if ((e <= size) && (Heap[pos].getPrioridade() > Heap[e].getPrioridade())) {
			menor = e;
		}
		else {
			menor = pos;
		}
		if ((d <= size) && (Heap[menor].getPrioridade() > Heap[d].getPrioridade())) {
			menor = d;
		}
		if (menor != pos) {
			inverte(pos, menor); 
			corrige_descendo(menor); 
		} 
	}
	
	
	//Método que corrige a heap a partir de um índice 
    //passado como parâmetro
	public void corrige_subindo (int pos) {
		int current = pos;
		int pai = parent(pos);
		
		while ((current > 0) && (Heap[current].getPrioridade() < 
				Heap[pai].getPrioridade())) { 
			inverte(current, pai); 
			current = pai;
			pai = parent(current);
		}
	}

	//Método que insere um elemento na heap  
	public void insere(int rotulo, double prioridade) { 
		if (size > maxsize - 1) { 
			return; 
		} 
		Heap[size].setVertice(rotulo);
		Heap[size].setPrioridade(prioridade); 
		size++;
		int current = size - 1; 
		corrige_subindo(current);
	} 
	
	//Método que retorna o primeiro elemento da heap
	public HeapElem remove() { 
		HeapElem popped = Heap[FRONT]; 
		Heap[FRONT] = Heap[size - 1];
		size --;
		indices[Heap[FRONT].getVertice()] = 0;
		corrige_descendo(FRONT); 
		return popped; 
	}
	
	//Método que verifica se a heap está vazia
	public boolean isEmpty () {
		if (size == 0)
			return true;
		return false;
	}

	//Método que altera a prioridade de um elemento da heap
	public void decrease_key (int vertice, double p) {
		int i = indices[vertice];
		Heap[i].setPrioridade(p);		
		corrige_subindo(i);
	}
}


//Classe que representa o algoritmo de Dijkstra
class Dijkstra {
	
	//Vetor de predecessores
	private int pred[];
	
	//Vetor de custos que representa a distância de 
	//cada vértice ao vértice raiz
	private double cost[];
	
	//Fila de prioridades
	MinHeap pq;
	
	//retorna o valor armazenado em um índice 
	public double cost(int v) {
		return cost[v];
	}
	
	//Recebe o digrafo e o vertice s que será a raiz da busca
	public Dijkstra (DigrafoComPeso digrafo, int s) {
		
		int t = digrafo.ordem();
		
		pred = new int [t];
		cost = new double[t];
		pq = new MinHeap(t);
		
		//Inicializa o vetor cost e a lista de prioridades
		for (int i = 0; i < t; i++) {
			cost[i] = Double.POSITIVE_INFINITY;
			pq.insere(i, Double.POSITIVE_INFINITY);
		}
		
		//Atribui 0 ao custo da raiz e coloca ela 
		//como sua própria predecessora;
		cost[s] = 0.0;
		pred[s] = s;
		//Troca a prioridade da raiz na heap para 0
		pq.decrease_key(s, 0);
		
		//Se a heap não estiver vazia
		while (!pq.isEmpty()) {
				
			//Pega o primeiro elemento da heap
			HeapElem u = pq.remove();
			int v = u.getVertice();
			
			//Para cada vizinho de saída do vértice
			for(int i = 0; i < digrafo.adj(v).size(); i++) {
				Arco e = digrafo.adj(v).get(i);
				//Faz a relaxação do arco
				relax(e);
			}
		}
	}
	
	//Método de relaxação dos arcos
	private void relax (Arco a) {
		int u = a.getCauda();
		int v = a.getCabeça();
		
		if (cost[v] > cost[u] + a.getPeso()) {
			cost[v] = cost[u] + a.getPeso();
			pred[v] = u;
			pq.decrease_key(v, cost[v]);
		}
	}
	
	
	//Verifica se um vértice tem um caminho até o vértice raiz
	public boolean hasPathTo (int v) {
		return cost[v] < Double.POSITIVE_INFINITY;
	}
}


public class problema7 {

	public static void main(String[] args) {
		
		Scanner leia = new Scanner(System.in);
		leia.useLocale(Locale.ENGLISH);
		
		int V, E, o, d;
		//Número de vértices
		V = leia.nextInt();
		
		//Número de arcos
		E = leia.nextInt();
		
		//Inicializa o digrafo
		DigrafoComPeso G = new DigrafoComPeso(V, E);
		
		int i, head, tail;
		double weight;
		Arco a;
		
		//Adiciona todos os arcos da entrada à estrutura
		for(i = 0; i < E; i++) {
			head = leia.nextInt();
			tail = leia.nextInt();
			weight = leia.nextDouble();
			a = new Arco(head, tail, weight);
			G.adicArco(a);
		}
		
		//Recebe o vértice de origem
		o = leia.nextInt();
		
		//Executa o algoritmo de Dijkstra a partir do vértice de origem
		Dijkstra D = new Dijkstra(G, o);
		
		//Recebe o vértice destino
		d = leia.nextInt();
		
		//Imprime a distância entre o vértice de origem 
		//e o vértice de destino. Imprime uma mensagem 
		//de erro caso não exista um caminho. 
		if (!D.hasPathTo(d)) {
			System.out.println("ERRO: 3.1415");
		}
		else {
			System.out.println(D.cost(d));
		}
		
	    leia.close();
	}

}

/*

CASOS DE TESTE

TESTE 1 
5
9
0 3 9.0690
0 4 29.0608
1 2 0.5465
2 1 75.4759
2 4 0.2178
3 1 58.2781
3 2 0.5346
3 4 987.8134
4 0 0.4844
2 1

Saída esperada : 68.0493

TESTE 2
5
9
0 3 0.5429
0 4 16.2579
2 0 2.3740
2 3 0.9571
3 1 428.1844
3 4 0.3195
4 0 5.8892
4 1 0.2138
4 3 448.0224
2 0

Saída esperada : 2.3740

TESTE 3
9
62
0 1 427.6478
0 2 8.3846
0 3 4.3070
0 4 96.7177
0 5 49.5153
0 6 1.7213
1 0 0.8482
1 3 8.1712
1 4 82.2437
1 6 0.0820
1 7 20.5712
2 0 30.6055
2 1 101.1314
2 3 67.9755
2 4 564.9132
2 5 283.9452
2 6 0.3244
2 7 39.7945
2 8 0.2607
3 0 0.1575
3 1 0.2018
3 4 0.0563
3 5 30.0608
3 6 28.3389
3 7 8.1227
3 8 0.5955
4 0 6.5300
4 1 0.1364
4 2 518.2453
4 3 620.7192
4 5 392.7908
4 6 58.1165
4 7 51.6789
5 0 18.6703
5 1 996.7683
5 2 784.5864
5 3 0.2593
5 4 64.8225
5 6 73.4428
5 7 943.1348
5 8 0.7991
6 0 3.1275
6 1 7.8191
6 2 586.3445
6 3 303.1854
6 4 40.1322
6 5 625.5920
6 7 45.5905
6 8 13.3516
7 2 0.2730
7 4 9.6821
7 5 6.0782
7 6 802.6282
7 8 710.7930
8 0 0.7365
8 1 93.7445
8 2 556.3397
8 3 562.6996
8 4 665.5454
8 5 47.5825
8 6 0.2502
8 7 0.5446
4 7

*/