/* 

BEATRIZ FAVINI CHICARONI

Objetivos: O programa recebe as coordenadas dos vértices de um grafo e calcula a
árvore geradora mínima desse grafo a partir do vértice 0. As coordenadas do 
vértice do grafo representam a localização das Ystations. A soma dos tamanhos 
das arestas representa a menor quantidade de fibra ótica necessária para ligar 
todas as Ystations à central. O vértice 0 representa a central.

Entrada: O programa recebe o número n de vértices do grafo e, em seguida, n 
linhas com as coordenadas de cada um deles.

Processamento: O programa utiliza o algoritmo de PRIM para construir a árvore
geradora mínima. 

Saída: O programa retorna a soma dos tamanhos das arestas da árvore geradora 
mínima do grafo. Em seguida, em ordem lexicográfica, retorna os extremos das 
arestas dessa árvore.

Informações adicionais: O programa representa o grafo através de listas de 
adjacências.

*/

#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#include <limits.h>

int *pred;
double *key;

//Estrutura que representa os vértices do grafo.
struct Node {
  int rotulo;
  double distancia;
  struct Node *next;
};
typedef struct Node Node;

//Estrutura que representa os elementos da heap (fila de prioridades) utilizada
//pela função PRIM.
struct ItemHeap {
  int vertice;
  double key;
};
typedef struct ItemHeap ItemHeap;

//Estrutura que representa a fila de prioridades utilizada pela função PRIM.
struct Rip{
  ItemHeap* *fila;
  int tamanho;
};
typedef struct Rip Rip;

//Estrutura que representa um aresta.
struct Aresta {
  int u;
  int v;
};
typedef struct Aresta Aresta;

//Função que aloca espaço de memória para uma estrutura do tipo Heap.
void constroiHeap(Rip *rip, int tamanhoHeap){
  rip->fila = (ItemHeap**) malloc(sizeof(ItemHeap*)*(tamanhoHeap+1));
  rip->tamanho = 0;
}

//Função que, a partir de um índice da Heap, retorna o indíce do seu "pai".
int pai(int idx) {
  return floor((idx-1) / 2.0);
}

//Função que, a partir de um índice da Heap, retorna o indíce do seu "filho da 
//esquerda".
int f_esq (int idx) {
  return 2 * idx + 1;
}

//Função que, a partir de um índice da Heap, retorna o indíce do seu "filho da 
//direita".
int f_dir (int idx) {
  return 2 * idx + 2;
}

//Função que corrige/atualiza a heap a partir de um índice. Serve para garantir 
//as propriedades da heap. 
void corrige_subindo (Rip rip, int i) {
  ItemHeap* temp;
  int p = pai(i);
  //Enquanto o pai for maior do que o elemento troca o pai com o elemento.
  while ((i>0) && (rip.fila[p]->key > rip.fila[i]->key)){
    temp = rip.fila[i];
    rip.fila[i] = rip.fila[p];
    rip.fila[p] = temp;       
    i = p;
    p = pai(i);
  }
}

//Função que corrige/atualiza a heap a partir de um índice. Serve para garantir 
//as propriedades da heap. 
void corrige_descendo (Rip rip, int i) {
  int e = f_esq(i);
  int d = f_dir(i);
  int menor;
  ItemHeap* temp;
  //Identifica, dentre o elemento e seus dois filhos, qual é o menor.
  if ((e <= rip.tamanho) && (rip.fila[e] -> key < rip.fila[i] -> key))
    menor = e;
  else
    menor = i;
  if ((d <= rip.tamanho) && (rip.fila[d] -> key < rip.fila[menor] -> key))
    menor = d;
  //Caso o menor deles seja um dos filhos, troca o elemento com o menor filho.
  if (menor != i) {
    temp = rip.fila[i];
    rip.fila[i] = rip.fila[menor];
    rip.fila[menor] = temp;
    corrige_descendo(rip, menor);
  }
}

//Insere um novo elemento na fila de prioridades e chama a função corrige_subindo
//para garantir as propriedades de heap.
void insereNaHeap (Rip *rip, int rotulo, double dist) {
  rip->tamanho = rip->tamanho+1;

  ItemHeap* novo = (ItemHeap*) malloc (sizeof(ItemHeap));

  (*novo).vertice = rotulo;
  (*novo).key = dist;

  int indice = (rip->tamanho)-1;
  rip->fila[indice] = novo;
  corrige_subindo(*rip, indice);
}

//Remove o primeiro elemento da fila de prioridades e chama a função 
//corrige_descendo para garantir as propriedades de heap.
ItemHeap* extract_min (Rip *rip) {
  ItemHeap* min;
  min = rip->fila[0];
  rip->fila[0] = rip->fila[rip->tamanho - 1];
  rip->tamanho = rip->tamanho - 1;
  corrige_descendo(*rip, 0);
  return min;
}

//Atualiza o campo key de um elemento da Heap. Como o key representa a prioridade
//de um elemento, chama a função corrige_subindo para atualizar a fila de 
//prioridades e garantir as propriedades de heap. 
void decrease_key (Rip rip, int v, double dist) {
  int i = 0;
  while(i < rip.tamanho && rip.fila[i]->vertice != v) {
    i = i+1;
  }
  rip.fila[i]->key = dist;
  corrige_subindo(rip, i);
}

//Adiciona um novo nó à vizinhança de um vértice na lista de adjacências
void adicionarVizinho(int vertice, double distancia, Node* *primeiro) {
  Node *novo = (Node*) malloc (sizeof(Node));

  (*novo).rotulo = vertice;
  (*novo).distancia = distancia;

  (*novo).next = *primeiro;
  *primeiro = novo;
}

//Para cada par de vértices, a partir das coordenadas no plano cartesiano de 
//cada um, calcula a distância euclidiana entre eles.
void calculaDistancia (Node* *G, int *Xcoord, int *Ycoord, int vertices) {
  int i, j;
  double difX, difY, dist;
  for (i=0; i<vertices; i++) {
    for (j=i+1; j<vertices; j++) {
      if (i!=j) {
        difX = Xcoord[i] - Xcoord[j];
        difY = Ycoord[i] - Ycoord[j];
        dist = sqrt((pow(difX, 2)) + (pow(difY, 2)));
        adicionarVizinho(j, dist, &(G[i]));
        adicionarVizinho(i, dist, &(G[j]));
      }
    }
  }
}

//Aloca espaço de memória para representr o grafo através de listas de adjacências.
//Cria dois vetores de inteiros que serão utilizados pela função calculaDistancia.
//Esses vetores de inteiros Xcoord e Ycoord representam, respectivamente, as 
//coordenadas x e y de cada um dos vértices.
void constroiGrafo (Node* *G, int vertices) {
  int Xcoord [vertices];
  int Ycoord [vertices];
  for (int i=0; i<vertices; i++) {
    (G[i]) = NULL;
    scanf ("%d %d", &Xcoord[i], &Ycoord[i]);
  }
  calculaDistancia(G, Xcoord, Ycoord, vertices);
}

//Compara duas arestas e decide qual delas é menor. Função fornecida à rotina 
//de ordenação qsort.
int compara (const void *e1, const void *e2) {
  if (((struct Aresta *)e1)->u < ((struct Aresta *)e2)->u)
    return -1;
  else if (((struct Aresta *)e1)->u == ((struct Aresta *)e2)->u) {
    if (((struct Aresta *)e1)->v < ((struct Aresta *)e2)->v)
      return -1;
    else if (((struct Aresta *)e1)->v == ((struct Aresta *)e2)->v)
      return 0;
    else
      return 1;
  }
  else
    return 1;
}

/*
Constroi o vetor de predecessores que representa a árvore geradora mínima do 
grafo. Para isso mantém um subgrafo H que, no início de cada iteração está 
contido em uma árvore geradora mínima. A cada iteração a função encontra uma 
aresta uv que se adicionada ao grafo H, preservará a propriedade de H estar 
contido em uma árvore geradora mínima. Essa aresta uv é denominada key, que
representa a menor aresta que liga o vétice v ao subgrafo H.
*/
void PRIM (Node* *G, int vertices) {
  int i, v;
  ItemHeap* u;
  //Vetor booleano que verific se um vértice ainda está na fila de prioridades.
  int taNaFila [vertices];
  
  //Inicializa os vetores predecessor, key e taNaFila.
  for (i=0; i<vertices; i++) {
    pred[i] = -1;
    key[i] = INT_MAX;
    taNaFila[i] = 0; 
  }
  
  pred[0] = 0;
  key[0] = 0;
  
  //Declara e aloca espaço de memória para uma heap chamada prioridade.
  Rip prioridade;
  constroiHeap(&prioridade, vertices);
  
  //Adiciona todos os vértices à heap.
  for (i=0; i<vertices; i++) {
    insereNaHeap(&prioridade, i, key[i]); 
    taNaFila[i] = 1;
  }
  
  //Enquanto a fila não está vazia:
  while(prioridade.tamanho > 0) {
    //Atribui a u o menor elemento da heap.
    u = extract_min(&prioridade);
    //Marca no vetor taNaFila que o elemento não está mais na fila de prioridades.
    taNaFila[u->vertice] = 0;
    Node *atual = (G[u->vertice]);
    //Para cada vizinho v de u:
    while (atual != NULL){
      v = atual->rotulo;
      //Se o vizinho v estiver na fila e a distância entre ele e u for menor que
      //o key de v atualiza o key de v .
      if ((taNaFila[v] == 1) && atual->distancia < key[v]) {
        pred[v] = u->vertice;
        key[v] = atual->distancia;
        decrease_key(prioridade, v, atual->distancia);
      }
      //Passa para o próximo vizinho de u.
      atual = atual->next;
    }  
  }
}

int main(void) {
  //vertices representa o números de vértices do grafo e i é um contador.
  int vertices, i;
  
  double dist;
  //Recebe o número de vertices do usuário.
  scanf("%d", &vertices);
  
  //Aloca espaço de memória para o grafo G e para os vetores pred e key.
  Node* G [vertices];
  constroiGrafo(G, vertices);
  pred = malloc(vertices*(sizeof(int)));
  key = malloc(vertices*(sizeof(double)));

    
  double soma=0;
  PRIM(G, vertices);
 
  Aresta arestas[vertices];

  //A partir do vetor pred retornado pela função PRIM, cria um vetor com as 
  //arestas da árvore geradora mínima. Além disso, armazena na variável soma,
  //a soma dos tamanhos das arestas dessa árvore.
  for(i=0; i<vertices; i++) {
    soma+=key[i];
    if (i < pred[i]) {
      arestas[i].u = i;
      arestas[i].v = pred[i];
    }
    else {
      arestas[i].u = pred[i];
      arestas[i].v = i;
    }
  }
  //Ordena o vetor de arestas
  qsort(arestas, vertices, sizeof(Aresta), compara);
  
  //Imprime na tela a soma dos tamanhos das arestas da árvore geradora mínima do
  //grafo.
  printf("comprimento de cabeamento minimo: %.4lf\n", soma);

  //Em seguida, em ordem lexicográfica, imprime os extremos das arestas dessa
  //árvore.
  for(i=1; i<vertices; i++) {
    printf("%d %d\n", arestas[i].u, arestas[i].v);
  }

  return 0;
}

/*

6
2 13
1 19
19 1
4 17
9 5
8 1

*/