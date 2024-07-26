import java.util.ArrayList;
import java.util.List;
import java.io.PrintWriter;

public class ArvoreBinariaPesquisa { // Personalizada para suprir a necessidade do programa
  class Nodo {
    Pedido chave; // comparações entre nodos: número do pedido
    Nodo esquerda, direita, pai;

    public Nodo(Pedido pedido) {
      this.chave = pedido;
      esquerda = direita = pai = null;
    }
  }

  private Nodo raiz;
  private int tamanho; // total de nodos da arvore
  private List<Pedido> pedidosMaisDemorados; // armazena o(os) pedidos mais demorados

  ArvoreBinariaPesquisa() {
    this.raiz = null;
    this.tamanho = 0;
    this.pedidosMaisDemorados = new ArrayList<>();
  }

  // inserir nova chave (Pedido) na ABP
  public void inserir(Pedido novaChave) {
    this.raiz = inserirRecursivamente(this.raiz, novaChave, null);
    this.tamanho++; // Incrementa o tamanho a cada inserção
    this.atualizarPedidoMaisDemorado(novaChave); // Verifica e atualiza o pedido mais demorado
  }

  // Método auxiliar de inserção recursiva com nodo pai
  private Nodo inserirRecursivamente(Nodo atual, Pedido novaChave, Nodo pai) {
    if (atual == null) {
      Nodo novoNodo = new Nodo(novaChave);
      novoNodo.pai = pai; // Define o pai do novo nodo
      return novoNodo;
    }

    if (novaChave.getTempoFinalizacao() < atual.chave.getTempoFinalizacao()) { // compara os números
      atual.esquerda = inserirRecursivamente(atual.esquerda, novaChave, atual);
    } else if (novaChave.getTempoFinalizacao() > atual.chave.getTempoFinalizacao()) {
      atual.direita = inserirRecursivamente(atual.direita, novaChave, atual);
    }

    return atual;
  }

  private void atualizarPedidoMaisDemorado(Pedido novoPedido) {
    if (pedidosMaisDemorados.isEmpty()
        || novoPedido.getTempoPreparo() > pedidosMaisDemorados.get(0).getTempoPreparo()) {
      pedidosMaisDemorados.clear();
      pedidosMaisDemorados.add(novoPedido);
    } else if (novoPedido.getTempoPreparo() == pedidosMaisDemorados.get(0).getTempoPreparo()) {
      pedidosMaisDemorados.add(novoPedido);
    }
  }

  // percorrer a ABP em caminhamento central para printar no relatorio
  public void percorrerEmOrdemRelatorio(PrintWriter relatorio) {
    this.percorrerEmOrdemRecursivo(this.raiz, relatorio);
  }

  // Método auxiliar para percorrer em ordem
  private void percorrerEmOrdemRecursivo(Nodo atual, PrintWriter relatorio) {
    if (atual != null) {
      percorrerEmOrdemRecursivo(atual.esquerda, relatorio);
      relatorio.print(atual.chave.getNumero() + ", ");
      percorrerEmOrdemRecursivo(atual.direita, relatorio);
    }
  }

  // percorrer a ABP em caminhamento central para resultados gerais
  public void percorrerEmOrdemNormal() {
    int[] contador = new int[1]; // Array de um elemento para manter o contador
    this.percorrerEmOrdemRecursivo(this.raiz, contador);
  }

  // Método auxiliar para percorrer em ordem
  private void percorrerEmOrdemRecursivo(Nodo atual, int[] contador) {
    if (atual != null) {
      percorrerEmOrdemRecursivo(atual.esquerda, contador);
      contador[0]++;
      atual.chave.setPosicao(contador[0]);
      System.out
          .println("[" + contador[0] + "]" + " Pedido: " + atual.chave.getSabor() + " | Nr: " + atual.chave.getNumero()
              + " | Tempo Finalização: " + atual.chave.getTempoFinalizacao());
      percorrerEmOrdemRecursivo(atual.direita, contador);
    }
  }

  public int getTotal() {
    return this.tamanho;
  }

  public void verPedidosMaisDemorados() {
    if (!this.pedidosMaisDemorados.isEmpty()) {
      System.out.println("\nListando os pedido(s) mais demorado(s):");
      for (Pedido pedido : this.pedidosMaisDemorados) {
        System.out.println(
            "[" + pedido.getPosicao() + "]" + " Pedido de NRO: " + pedido.getNumero() + " | Sabor: " + pedido.getSabor()
                + " | Tempo de preparo: " + pedido.getTempoPreparo());
      }
    } else {
      System.out.println("\n[DEBUG] Pedidos Prontos: Nenhum pedido foi processado até o momento");
    }
    System.out.println();
  }
}