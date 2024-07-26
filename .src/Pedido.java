public class Pedido implements Comparable<Pedido> {
  private int numero;
  private String sabor;
  private int instantePedido; // retorna o instante em que o pedido foi feito
  private int tempoPreparo;
  private int tempoRestante;
  private int tempoFinalizacao;
  private int posicao; // por ordem de tempo de preparo
  private StatusPedido status;

  public Pedido(int numero, String sabor, int tempoPedido, int tempoPreparo) {
    this.numero = numero;
    this.sabor = sabor;
    this.instantePedido = tempoPedido;
    this.tempoPreparo = tempoPreparo;
    this.tempoRestante = this.tempoPreparo;
    this.tempoFinalizacao = 0;
    this.posicao = 0;
    this.status = StatusPedido.NAFILA;
  }

  public int getNumero() {
    return this.numero;
  }

  public String getSabor() {
    return this.sabor;
  }

  public int getInstantePedido() {
    return this.instantePedido;
  }

  public int getTempoPreparo() {
    return this.tempoPreparo;
  }

  public int getTempoRestante() {
    return this.tempoRestante;
  }

  public int getTempoFinalizacao() {
    return this.tempoFinalizacao;
  }

  public void setTempoFinalizacao(int to) {
    this.tempoFinalizacao = to;
  }

  public int getPosicao() {
    return this.posicao;
  }

  public void setPosicao(int posicao) {
    this.posicao = posicao;
  }

  // decrementar
  public void atualizarTempoRestante() {
    if (this.tempoRestante > 0) {
      this.tempoRestante--;
    } else {
      System.out.println("Erro! O tempo restante do pedido que você está tentando atualizar já chegou a zero.");
    }
  }

  public StatusPedido getStatus() {
    return this.status;
  }

  public void setStatus(StatusPedido to) {
    this.status = to;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("Pedido de número: ").append(this.numero).append(" | ");
    sb.append("Sabor: ").append(this.sabor).append(" | ");
    sb.append("Instante de chegada: ").append(this.instantePedido);
    return sb.toString();
  }

  // Permite comparar dois objetos Pedido de acordo com seus números
  @Override
  public int compareTo(Pedido outroPedido) {
    if (this.tempoFinalizacao < outroPedido.tempoFinalizacao) {
      return -1; // Retorna -1 se este pedido terminou de ser preparado antes
    } else if (this.tempoFinalizacao > outroPedido.tempoFinalizacao) {
      return 1; // Retorna 1 see este pedido terminou de ser preparado depois
    } else {
      return 0; // Retorna 0 se os pedidos terminaram de ser preparados ao mesmo tempo (não
                // acontece)
    }
  }
}
