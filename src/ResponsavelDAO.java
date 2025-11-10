import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ResponsavelDAO {
    private Connection connection;

    public ResponsavelDAO(Connection connection) {
        this.connection = connection;
    }

    // CREATE - Cadastrar responsável
    public Integer cadastrar(Responsavel responsavel) {
        String sql = "INSERT INTO Responsavel (nome, idade, cpf, telefone, parentesco) VALUES (?, ?, ?, ?, ?)";

        // Usando RETURN_GENERATED_KEYS para pegar o ID auto-incrementado
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1,responsavel.getNome());
            stmt.setInt(2,responsavel.getIdade());
            stmt.setString(3,responsavel.getCpf());
            stmt.setString(4,responsavel.getTelefone());
            stmt.setString(5,responsavel.getParentesco());

            int linhasAfetadas = stmt.executeUpdate();

            // Recuperar o ID gerado automaticamente
            if (linhasAfetadas > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int idGerado = generatedKeys.getInt(1);
                        responsavel.setId(idGerado); // Atualiza o ID no objeto
                        return idGerado;
                    }
                }
            }

            return null;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao cadastrar responsável: " + e.getMessage(), e);
        }
    }

    // READ - Buscar por ID
    public Responsavel buscarPorId(int id) {
        String sql = "SELECT * FROM Responsavel WHERE id_responsavel = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Responsavel responsavel = mapearResponsavel(rs);
                return responsavel;
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar responsável por ID: " + e.getMessage(), e);
        }

        return null;
    }

    // READ - Buscar por CPF
    public Optional<Responsavel> buscarPorCpf(String cpf) {
        String sql = "SELECT * FROM Responsavel WHERE cpf = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, cpf);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Responsavel responsavel = mapearResponsavel(rs);
                return Optional.of(responsavel);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar responsável por CPF: " + e.getMessage(), e);
        }

        return Optional.empty();
    }

    // READ - Listar todos
    public List<Responsavel> listarTodos() {
        List<Responsavel> responsaveis = new ArrayList<>();
        String sql = "SELECT * FROM Responsavel ORDER BY id_responsavel";

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Responsavel responsavel = mapearResponsavel(rs);
                responsaveis.add(responsavel);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar responsáveis: " + e.getMessage(), e);
        }

        return responsaveis;
    }

    // UPDATE - Atualizar responsável
    public void atualizar(Responsavel responsavel) {
        String sql = "UPDATE Responsavel SET nome = ?, idade = ?, cpf = ?, telefone = ?, parentesco = ? WHERE id_responsavel = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, responsavel.getNome());
            stmt.setInt(2, responsavel.getIdade());
            stmt.setString(3, responsavel.getCpf());
            stmt.setString(4, responsavel.getTelefone());
            stmt.setString(5, responsavel.getParentesco());
            stmt.setInt(6, responsavel.getId());

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar responsável: " + e.getMessage(), e);
        }
    }

    // DELETE - Excluir responsável (com verificação de dependência)
    public boolean excluir(int id) {
        // Primeiro verificar se existem alunos dependentes
        String sqlVerificar = "SELECT COUNT(*) FROM Aluno WHERE id_responsavel = ?";
        String sqlExcluir = "DELETE FROM Responsavel WHERE id_responsavel = ?";

        try (PreparedStatement stmtVerificar = connection.prepareStatement(sqlVerificar);
             PreparedStatement stmtExcluir = connection.prepareStatement(sqlExcluir)) {

            // Verificar dependências
            stmtVerificar.setInt(1, id);
            ResultSet rs = stmtVerificar.executeQuery();

            if (rs.next() && rs.getInt(1) > 0) {
                // Existem alunos dependentes - não pode excluir
                return false;
            }

            // Não há dependências - pode excluir
            stmtExcluir.setInt(1, id);
            int linhasAfetadas = stmtExcluir.executeUpdate();

            return linhasAfetadas > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao excluir responsável: " + e.getMessage(), e);
        }
    }

    // Método auxiliar para mapear ResultSet para objeto Responsavel
    private Responsavel mapearResponsavel(ResultSet rs) throws SQLException {
        return new Responsavel(
                rs.getInt("id_responsavel"),
                rs.getString("nome"),
                rs.getInt("idade"),
                rs.getString("cpf"),
                rs.getString("parentesco"),
                rs.getString("telefone")
        );
    }
}