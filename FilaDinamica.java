public class FilaDinamica { // personalizada para satisfazer a necessidade da classe Pedido
  class Nodo {
    Pedido pedido;
    Nodo proximoNodo;

    public Nodo(Pedido pedido) {
      this.pedido = pedido;
      this.proximoNodo = null;
    }
  }

  private int tamanho;
  private Nodo nodoInicial;
  private Nodo nodoFinal;

  public FilaDinamica() {
    this.tamanho = 0;
    this.nodoInicial = null;
    this.nodoFinal = null;
  }

  public void enfileirar(Pedido item) {
    Nodo novoNodo = new Nodo(item);
    if (this.tamanho == 0) {
      this.nodoInicial = novoNodo;
      this.nodoFinal = novoNodo;
    } else {
      this.nodoFinal.proximoNodo = novoNodo;
      this.nodoFinal = novoNodo;
    }
    this.tamanho++;
  }

  // remove o primeiro item da fila e retorna seu objeto.
  public Pedido desenfileirar() {
    Pedido retorno = null;
    if (this.tamanho > 0) {
      retorno = nodoInicial.pedido;
      this.nodoInicial = nodoInicial.proximoNodo; // Simplesmente avança o nodoInicial
      this.tamanho--;
      if (this.tamanho == 0) {
        this.nodoFinal = null; // Garante que o nodoFinal também é limpo
      }
      return retorno;
    } else {
      System.out.println("[DEBUG] Desenfileirar retornou null");
      return null;
    }
  }

  public Pedido verPrimeiro() {
    if (this.tamanho > 0) {
      Pedido retorno = nodoInicial.pedido;
      return retorno;
    } else {
      System.out.println("[DEBUG] verPrimeiro(): A fila de pedidos está vazia e o primeiro pedido é NULL.");
      return null;
    }

  }

  // verifica se existe o item na fila
  public boolean estaNaFila(String sabor) {
    Nodo nodoAtual = this.nodoInicial;
    while (nodoAtual != null) {
      if (nodoAtual.pedido.getSabor().equals(sabor)) {
        return true;
      }
      nodoAtual = nodoAtual.proximoNodo;
    }
    return false;
  }

  public boolean estaVazia() {
    return this.tamanho == 0;
  }

  public void esvaziar() {
    this.nodoInicial = null;
    this.nodoFinal = null;
    this.tamanho = 0;
  }

  public int getTamanho() {
    return this.tamanho;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    // sb.append("Fila Dinâmica = { ");
    Nodo nodoAux = this.nodoInicial;

    while (nodoAux != null) { // quando nodoAux = this.nodoFinal, a proxima atribuição de nodoAux =
                              // nodoAux.proximoNodo gerará um valor null.
      sb.append(nodoAux.pedido.getNumero());
      if (nodoAux.proximoNodo != null) { // Verifica se não é o último nodo
        sb.append(", "); // Adiciona a vírgula apenas se não for o último
      }
      nodoAux = nodoAux.proximoNodo;
    }

    // sb.append("} ");
    // sb.append("Início: ").append(this.nodoInicial.pedido.getSabor()).append(" ");
    // sb.append("Fim: ").append(this.nodoFinal.pedido.getSabor()).append(" ");
    // sb.append("Tamanho: ").append(this.tamanho).append(" ");

    return sb.toString();
  }
}
