/*BEATRIZ FAVINI CHICARONI
O programa utiliza um grafo cujos vértices representam os aeroportos atendidos pela 
companhia e cujas arestas representam os voos existentes.
O objetivo principal é retornar quantas arestas são necessárias para unir todas as 
componentes conexas e transformar o grafo em um grafo conexo. ou seja, determinar 
quantas arestas são necessárias para garantir que existe pelo menos um caminho entre 
todo par de vértices.

ENTRADA:
O programa recebe como entrada um inteiro n que representa a quantidade de vértices 
do grafo (número de aeroportos).
Depois recebe um inteiro m que representa quantas arestas existirão no grafo, ou quantos
voos existem. 
Depois recebe m linhas representando as arestas do grafo, x representa
um extremo e y representa o outro extremo da aresta. 

PROCESSAMENTO:
O programa executa uma busca em largura para cada componente conexa do grafo. 
A cada vez que a fila fica vazia, o programa verifica se ainda existem vértices não 
visitados. Quando existem, conclui que estão em uma componente diferente das que já 
foram visitadas, adiciona 1 ao contador de componentes e executa uma nova busca em 
largura iniciando nesse vértice não visitado.

SAÍDA:
Com o número de componentes conexas, é possível concluir que para existir pelo menos um 
caminho entre todos os pares de vértices do grafo basta ligar uma componente conexa à 
outra, adicionando uma aresta entre elas. Então, como saída, o programa apresenta 
o número de componentes do grafo-1, que representa o número de arestas que devem 
ser adicionadas para tornar o grafo conexo.

*/


import java.util.Scanner;

public class BFS {

	//preenche a matriz passada como parâmetro com 0 em todas as posições
	public static void preenche(int[][] matriz){
		for(int i=0; i<matriz.length; i++) { 
			for(int j=0; j<matriz[i].length; j++)
				matriz[i][j]=0;
		}	
	}
	
	//verifica se a fila está vazia
	public static boolean isEmpty (int total) {
		return total == 0;
	}
	
	//adiciona um elemento à fila
	public static void enfileira (int elemento, int[] fila, int ultimo) {
		fila[ultimo] = elemento;
	}
	
	//retira o elemento que está há mais tempo na fila
	public static int desenfileira (int[] fila, int primeiro) {
		int elemento = fila [primeiro];
		return elemento;
	}
	
	/*verifica se ainda há algum vértice não visitado no grafo passado como parâmetro
	  se houver, retorna o vértice. Caso contrário, retorna -1.
	*/
	public static int visitouTudo (int[] visitou) {
		for (int i = 0; i < visitou.length; i++) {
			if (visitou[i] == 0)
				return i;
		}	
		return -1;
	}
	
	public static void main(String[] args) {
		Scanner leia = new Scanner(System.in);
		int n, m, x, y, i, s, u;
		//n é um inteiro que representa a quantidade de vertices
		n = leia.nextInt();
		/*o programa aqui instancia a matriz que representa o grafo 
		  e coloca 0 em todas as posições da matriz através da função
		  preenche*/ 
		int grafo[][] = new int[n][n];
		preenche(grafo);
		/*m é um inteiro que representa o número de arestas existentes
		  no grafo. O laço for coloca 1 na matriz na posição das arestas
		  que existem*/
		m = leia.nextInt();
		for (i=0; i<m; i++) {
			x = leia.nextInt();
			y = leia.nextInt();
			grafo[x][y] = 1;
			grafo[y][x] = 1;
		}
		
		/*visitado é um vetor booleano que indica, para cada vértice, se ele
		  já foi visitado ou não
		  pai é um vetor de inteiros que indica o predecessor de cada vértice 
		  na árvore gerada pelo algoritmo da busca em largura 
		*/
		int visitado[] = new int [n];
		int pai[] = new int [n];
		/*o laço for coloca 0 em todas as poções do vetor visitado, indicando que 
		  nenhum deles foi visitado ainda e coloca -1 em todas as posições do vetor
		  pai, para indicar que nenhum vértice tem predecessor definido 
		*/
		for (i=0; i<n; i++) {
			visitado[i] = 0;
			pai[i] = -1;
		}
		
		//componentes conta quantas componentes do grafo foram descobertas
		int componentes = 0;
		
		//primeiro, ultimo e total são variáveis que fazem parte da estrutura fila. 
		int fila[] = new int [n];
		int primeiro = 0;
		int ultimo = 0;
		int total = 0;
		
		/*o laço while executa a busca em largura em todos as componentes 
		  conexas do grafo e conta quantas componentes existem
		 */
		while (visitouTudo(visitado)!= -1) {
			//caso ainda existam vértices para serem visitados:
			//adiciona uma componente ao contador
			componentes++;
			//s representa o vértice de onde partirá a busca em largura.
			//será a raiz da árvore gerada
			s = visitouTudo(visitado);
			pai[s]=s;
			visitado[s] = 1;
			//adiciona s à fila e atualiza as variáveis último e total
			enfileira(s, fila, ultimo);
			ultimo = (ultimo+1)%fila.length;
			total++;
			while (isEmpty(total) == false ) {
				//u recebe o vertice que está há mais tempo na fila
				u = desenfileira (fila, primeiro);
				primeiro = (primeiro+1)%fila.length;
				total--;
				/* o laço for percorre, na matriz que representa o grafo, 
				   a linha correspondente aos vizinhos de u
				*/
				for (i = 0; i < n; i++) {
					//verifica se o vertice i é vizinho de u
					if (grafo[u][i] == 1) {
						//para cada vizinho de u, verifica se ele já foi visitado
						if (visitado[i] == 0) {
							//para cada vértice não visitado:
							//marca ele como visitado
							visitado[i] = 1;
							//define u como o pai dele
							pai[i] = u;
							//coloca o vértice na fila e atualiza 
							//as variaveis da estrutura fila
							enfileira(i, fila, ultimo);
							ultimo = (ultimo+1)%fila.length;
							total++;
						}
					}
				}
			}
		}
		System.out.println("# de novos voos: "+ (componentes -1));
		leia.close();
	}
}
