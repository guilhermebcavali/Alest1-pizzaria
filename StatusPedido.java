public enum StatusPedido {
  NAFILA("Na fila"),
  AVALIACAO("Em avaliação"),
  EMPRODUCAO("Em produção"),
  PRONTO("Pronto");

  private String descricao;

  StatusPedido(String descricao) {
    this.descricao = descricao;
  }

  public String getDescricao() {
    return descricao;
  }
}