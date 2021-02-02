/*BEATRIZ FAVINI CHICARONI
  O programa utiliza um grafo para representar os amigos de Marvin e a relação entre eles.
  
  ENTRADA:
  O programa recebe como entrada um inteiro n que representa a quantidade de amigos
  de Marvin (n é a quantidade de vértices do grafo). Depois recebe um inteiro n que 
  representa quantas arestas existirão no grafo, ou quantas relações de amizades existem 
  entre os amigos. Depois recebe m linhas representando as arestas do grafo, x representa
  um extremo e y representa o outro extremo da aresta. Com o grafo construído, (este programa
  representa o grafo através de uma matriz) é possível analisar quais listas formam um 
  churrasco ÉPICO. Para isso, o programa recebe um inteiro k que representa o número de 
  listas feitas. Nas próximas k linhas, o primeiro inteiro (l) representa a quantidade de 
  amigos incluída naquela lista, e os outros inteiros representam os amigos incluídos na lista.
  
  EXECUÇÃO:
  O programa interpreta cada amigo da lista como um vértice do grafo e analisa, par a par, se
  os amigos se conhecem. 
  
  SAÍDA:
  Caso todos os amigos de uma lista se conheçam, o programa retorna sim, indicando que aquela é
  uma lista de amigos que dá um churrasco ÉPICO. 
  */


import java.util.Scanner;

public class Main {

	//preenche a matriz passada como parâmetro com 0 em todas as posições
	public static void preenche(int[][] matriz){
		for(int i=0; i<matriz.length; i++) { 
			for(int j=0; j<matriz[i].length; j++)
				matriz[i][j]=0;
		}	
	}
	
	/*verifica é uma função booleana que recebe uma lista de amigos e o 
	  grafo que representa quais amigos são amigos entre si. 
	  a função verifica se todos os amigos da lista são amigos uns dos outros.*/
	public static boolean verifica (int[] lista, int[][] amigos) {
		int i, j, amigo1, amigo2; 
		//i e j são contadores
		for (i=0; i<lista.length - 1; i++) {
			/*amigo1 é um inteiro que representa um dos amigos do par 
			  que está sendo analisado*/
			amigo1 = lista[i];
			for (j=i+1; j<lista.length; j++) {
				/*amigo2 é um inteiro que representa o outro amigo do par 
				  que está sendo analisado*/
				amigo2 = lista[j];
				/*caso exista um par de amigos na lista que não são amigos 
				  entre si a função retorna falso imediatamente, sem verificar
				  se os outros amigos se conhecem*/
				if (amigos[amigo1][amigo2] == 0) {
					return false;
				}
			}
		}
		return true;
	}
	
	public static void main(String[] args) {
		Scanner leia = new Scanner(System.in);
		int n, m, x, y, k, l, i, j;
		//n é um inteiro que representa a quantidade de amigos
		n = leia.nextInt();
		/*o programa aqui instancia a matriz que representa o grafo 
		  e coloca 0 em todas as posições da matriz através da função
		  preenche*/ 
		int grafo[][] = new int[n][n];
		preenche(grafo);
		
		/*m é um inteiro que representa o número de arestas existentes
		  no grafo. o laço for coloca 1 na matriz na posição das arestas
		  que existem (amigos que se conhecem)*/
		m = leia.nextInt();
		for (i=0; i<m; i++) {
			x = leia.nextInt();
			y = leia.nextInt();
			grafo[x][y] = 1;
			grafo[y][x] = 1;
		}
		
		//k é um inteiro que representa o número de listas de amigos criadas
		k = leia.nextInt();
		for (i=0; i<k; i++) {
			//l é um inteiro que representa a quantidade de amigos presentes em uma lista
			l = leia.nextInt();
			
			//lista é um vetor de inteiros que representa uma lista de amigos
			int lista[] = new int [l];
			/*este laço for pega os numeros que representam os amigos fornecidos
			  na entrada e os coloca no vetor lista*/
			for (j=0; j<l; j++) {
				lista[j] = leia.nextInt();
			}
			
			/*a função verifica é responsável por analisar se todos os amigos
			  de uma lista fornecida se conhecem.*/ 
			if (verifica(lista, grafo)==true) {
				System.out.println("SIM");
			}
			else {
				System.out.println("NAO");
			}
		}
		leia.close();
	}

}