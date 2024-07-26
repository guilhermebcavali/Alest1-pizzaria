public enum StatusPizzaiolo {
  DISPONIVEL("Disponível"),
  OCUPADO("Ocupado");

  private String descricao;

  StatusPizzaiolo(String descricao) {
    this.descricao = descricao;
  }

  public String getDescricao() {
    return descricao;
  }
}
