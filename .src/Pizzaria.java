import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.IOException;

public class Pizzaria {
  private int tempoAtual;
  private int totalPedidos; // total de pedidos que chegaram na pizzaria (csv)
  private int totalPedidosAtual; // total de pedidos na fila, no instante atual
  private FilaDinamica filaPedidos; // armazena todos os pedidos que chegam conforme o tempo
  private ArvoreBinariaPesquisa pedidosProntos; // armazena os pedidos que já foram processados (em ordem)
  private Pedido pedidoAtualEmProcessamento; // aux

  public Pizzaria() {
    this.tempoAtual = 0;
    this.totalPedidosAtual = 0;
    this.totalPedidosAtual = 0;
    this.filaPedidos = new FilaDinamica();
    this.pedidosProntos = new ArvoreBinariaPesquisa();
    this.pedidoAtualEmProcessamento = null;
  }

  public void simular() {
    Scanner scanner = new Scanner(System.in); // leitor de input

    // Loop de inputs:
    // Enquanto a quantidade de pedidos prontos (ABP) não for igual a quantidade de
    // pedidos encomendados, a simulação continuarodando.
    while (this.pedidosProntos.getTotal() != this.totalPedidos) {
      System.out.println(
          "Pressione <ENTER> para avançar um ciclo, 'C' para continuar até o final, ou 'S' para encerrar o programa.");
      String input = scanner.nextLine(); // le cada input do usuario

      if (input.equalsIgnoreCase("c")) {
        System.out.println("Continuando o programa até o final...");
        this.executarSimulacaoContinua();
        break;
      } else if (input.equalsIgnoreCase("s")) {
        System.out.println("Saindo do loop...");
        this.gerarResultados();
        break;
      } else if (input.isEmpty()) { // Apertou <ENTER>
        this.avancarTempo();
      } else {
        System.out.println("Comando inválido!");
      }
    }

    this.exportarOrdemCentralCSV();

    System.out.println("O programa está sendo encerrado...");

    scanner.close();
  }

  private void avancarTempo() {
    this.tempoAtual++;
    System.out.println("\nTempo atual: " + this.tempoAtual);

    System.out.println("[DEBUG] Verificando se algum pedido chegou ao estabelecimento...");
    this.carregarDados("pedidos_100.csv", this.tempoAtual);

    this.analisarPedidos();

    this.inserirNoRelatorio();
  }

  // método auxiliar pra saber quantos pedidos contem no csv.
  public void contarLinhas(String filePath) {
    int contadorDeLinhas = 0;
    File arquivo = new File(filePath);

    try (Scanner scanner = new Scanner(arquivo)) {
      if (scanner.hasNextLine()) { // Ignora a primeira linha (cabeçalho)
        scanner.nextLine();
      }

      while (scanner.hasNextLine()) {
        scanner.nextLine();
        contadorDeLinhas++;
      }

    } catch (FileNotFoundException e) {
      System.out.println("Arquivo não encontrado: " + e.getMessage());
    }

    System.out.println("[DEBUG] Total de pedidos no arquivo csv: " + contadorDeLinhas);
    this.totalPedidos = contadorDeLinhas;
  }

  // simula o programa inteiro em uma unica vez
  private void executarSimulacaoContinua() {
    while (this.pedidosProntos.getTotal() < this.totalPedidos) {
      this.avancarTempo();
    }

    // depois de terminar a simulação, gera os resultados
    this.gerarResultados();
    this.exportarOrdemCentralCSV();
  }

  private void analisarPedidos() {

    // debug
    Pedido verPrimeiro = this.filaPedidos.verPrimeiro();
    if (verPrimeiro != null) {
      System.out.println("[DEBUG] Primeiro pedido da fila no instante atual: " + verPrimeiro.toString());
    }

    // Verifica disponibilidade do pizzaiolo
    if (Pizzaiolo.status == StatusPizzaiolo.DISPONIVEL) {
      System.out.println("O pizzaiolo está DISPONÍVEL.");
      // se disponível, analisa fila de pedidos
      this.processarPedido();

      // Decrementa o tempo restante do pedido atual em processamento
    } else if (this.pedidoAtualEmProcessamento != null) {
      if (this.pedidoAtualEmProcessamento.getTempoRestante() > 0) {
        this.pedidoAtualEmProcessamento.atualizarTempoRestante();
      } else { // quando tempo restante do pedido = 0 --> atualizar status do pedido /
               // adicionar na
               // ABP / liberar pizzaiolo
        this.pedidoAtualEmProcessamento.setStatus(StatusPedido.PRONTO);
        this.pedidoAtualEmProcessamento.setTempoFinalizacao(this.tempoAtual);
        this.pedidosProntos.inserir(pedidoAtualEmProcessamento);
        System.out.println("O pedido NRO " + this.pedidoAtualEmProcessamento.getNumero() + " de sabor '"
            + this.pedidoAtualEmProcessamento.getSabor() + "' foi finalizado!");
        Pizzaiolo.status = StatusPizzaiolo.DISPONIVEL;
      }
    }

    // if(this.filaPedidos.verPrimeiro().getInstantePedido() == this.tempoAtual) {

    // }
  }

  // inicia processamento do primeiro pedido da fila --> desenfileira a fila /
  // atualiza status do pedido / atualiza status do pizzaiolo
  private void processarPedido() {
    Pedido pedido = this.filaPedidos.desenfileirar();
    if (pedido != null) {
      this.pedidoAtualEmProcessamento = pedido;
      this.pedidoAtualEmProcessamento.setStatus(StatusPedido.EMPRODUCAO);
      this.pedidoAtualEmProcessamento.atualizarTempoRestante();
      Pizzaiolo.status = StatusPizzaiolo.OCUPADO;
      System.out.println("O pedido NRO " +
          this.pedidoAtualEmProcessamento.getNumero() + " de sabor '"
          + this.pedidoAtualEmProcessamento.getSabor() + "' começará a ser produzido.");
    } else {
      System.out.println("[DEBUG] Não há pedido para ser processado no instante atual.");
    }
  }

  // popula a fila de pedidos a partir de um arquivo csv
  public void carregarDados(String csvPathName, int tempoAtual) {
    File csvFile = new File(csvPathName);
    Scanner scanner;
    try {
      scanner = new Scanner(csvFile);

      // Pular a primeira linha (cabeçalho) da tabela;
      if (scanner.hasNextLine()) {
        scanner.nextLine();
      }

      FilaDinamica filaAux = new FilaDinamica(); // aux para debug

      while (scanner.hasNextLine()) {
        String line = scanner.nextLine();
        // Divide a linha em tokens (palavras/atributos) usando a vírgula como
        // delimitador
        String[] tokens = line.split(",");

        // Verificar se há quatro tokens na linha --> para o formato proposto no trab,
        // sempre terá.
        if (tokens.length == 4) {
          int token1 = Integer.valueOf(tokens[0]); // id (numero)
          String token2 = tokens[1]; // sabor
          int token3 = Integer.valueOf(tokens[2]); // instantePedido
          int token4 = Integer.valueOf(tokens[3]); // tempoPreparo

          // instancia transacao com tokens como atributos;
          Pedido novoPedido = new Pedido(token1, token2, token3, token4);
          novoPedido.setStatus(StatusPedido.NAFILA);

          // inserir pedido na fila;
          if (novoPedido.getInstantePedido() == tempoAtual) {
            System.out.println("O " + novoPedido.toString() + " chegou ao sistema da pizzaria.");
            filaAux.enfileirar(novoPedido);
            this.filaPedidos.enfileirar(novoPedido);
          }

        } else {
          // Tratar caso a linha não tenha quatro tokens
          System.out.println("A linha não possui quatro tokens: " + line);
        }
      }

      this.totalPedidosAtual = this.filaPedidos.getTamanho();

      if (filaAux.getTamanho() == 0) {
        System.out.println("[DEBUG] Nenhum pedido chegou ao estabelecimento no tempo atual.");
      } else {
        System.out.println("[DEBUG] filaAux: Pedidos lidos e inseridos no instante atual: " + filaAux.toString());
      }

      System.out.println("[DEBUG] Tamanho da fila no instante atual: " + this.filaPedidos.getTamanho());
      System.out.println("[DEBUG] Pedidos na fila no instante atual: " + this.filaPedidos.toString());

      scanner.close();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
  }

  // imprime total de pedidos processados (total de pedidos na ABP)
  private void verPedidosProntos() {
    System.out.println("\nTotal de pedidos prontos: " + this.pedidosProntos.getTotal());
    this.pedidosProntos.percorrerEmOrdemNormal(); // Passa o PrintWriter para o método modificado
  }

  // Tempo total de execução do programa
  private void verTempoTotal() {
    System.out.println("Tempo total de execução do programa: " + this.tempoAtual);
  }

  // gera os resultados gerais da simulação
  private void gerarResultados() {
    System.out.println("\n*------------- Resultados gerais da simulação -------------*\n");
    this.verTempoTotal();
    this.verPedidosProntos();
    this.pedidosProntos.verPedidosMaisDemorados();
  }

  // registra a situação dos pedidos a cada instante no relatorio CSV
  private void inserirNoRelatorio() {
    try (PrintWriter relatorio = new PrintWriter(new FileWriter("relatorio_simulacao.csv", true))) {
      if (new File("relatorio_simulacao.csv").length() == 0) {
        relatorio.println("Instante de Tempo t,Fila de pedidos,Em produção,Prontos");
      }

      // Capturando os estados dos pedidos
      String filaDePedidos = filaPedidos.toString();
      String emProducao = (pedidoAtualEmProcessamento != null) ? String.valueOf(pedidoAtualEmProcessamento.getNumero())
          : "";

      StringWriter prontosStringWriter = new StringWriter();
      PrintWriter prontosPrintWriter = new PrintWriter(prontosStringWriter);

      this.pedidosProntos.percorrerEmOrdemRelatorio(prontosPrintWriter);
      prontosPrintWriter.close();

      // Formatação de pedidos prontos para CSV

      String prontos = prontosStringWriter.toString().trim();
      // Remover a última vírgula e quebras de linha
      if (prontos.endsWith(", ")) {
        prontos = prontos.substring(0, prontos.length() - 2);
      }

      prontos = prontos.replaceAll("\\s+", ""); // Remove espaços extras e quebras de linha, se necessário.

      // Imprime a linha no arquivo CSV
      relatorio.printf("%d,\"%s\",\"%s\",\"%s\"\n", tempoAtual, filaDePedidos, emProducao, prontos);
    } catch (IOException e) {
      System.out.println("Erro ao escrever no arquivo de relatório: " + e.getMessage());
    }
  }

  // gera o arquivo csv do caminhamento central da ABP, contendo apenas os CÓDIGOS
  // DOS PEDIDOS (separados por vírgula)
  private void exportarOrdemCentralCSV() {
    String nomeArquivo = "ordem_central.csv";
    try (PrintWriter writer = new PrintWriter(new FileWriter(nomeArquivo, false))) { // false
      this.pedidosProntos.percorrerEmOrdemRelatorio(writer);
    } catch (IOException e) {
      System.out.println("Erro ao escrever no arquivo CSV: " + e.getMessage());
    }
  }
}
