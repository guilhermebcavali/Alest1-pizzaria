import java.io.IOException;

public class App {
    public static void main(String[] args) throws IOException {
        Pizzaria pizzaria = new Pizzaria();

        // ATENÇÃO: para cada arquivo simulado, é necessário excluir o arquivo do
        // relatório atual.
        // O arquivo a ser lido deve ser modificado no método "carregarDadosCSV()"
        // dentro do método "avançarTempo()" da classe Pizzaria.

        // contabiliza quantos pedidos serão lidos do arquivo CSV atual
        pizzaria.contarLinhas("pedidos_100.csv");

        pizzaria.simular();
    }
}
