import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;

public class TESTE {
    public static void main(String[] args) {
        // Configurações do banco de dados
        String url = "jdbc:mysql://localhost:3306/Creche"; // substitui "seu_banco" pelo nome do teu banco
        String usuario = "root";
        String senha = "isaac1662006";

        try (Connection connection = DriverManager.getConnection(url, usuario, senha)) {
            System.out.println("✅ Conexão bem-sucedida!");

            // Cria o DAO e chama o método listarTodos()
            ResponsavelDAO dao = new ResponsavelDAO(connection);
            List<Responsavel> lista = dao.listarTodos();


            if (lista.isEmpty()) {
                System.out.println("Nenhum responsável encontrado.");
            } else {
                System.out.println("Lista de responsáveis:");
                for (Responsavel r : lista) {
                    System.out.println("ID: " + r.getId() +
                            " | Nome: " + r.getNome() +
                            " | Idade: " + r.getIdade() +
                            " | CPF: " + r.getCpf() +
                            " | Telefone: " + r.getTelefone() +
                            " | Parentesco: " + r.getParentesco());
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

