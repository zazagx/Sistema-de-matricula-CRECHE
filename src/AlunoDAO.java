import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AlunoDAO {
    private Connection connection;

    public AlunoDAO(Connection connection) {
        this.connection = connection;
    }

    // CREATE - Cadastrar aluno (AGORA COM ID AUTOMÁTICO)
    public Integer cadastrar(Aluno aluno) {
        String sql = "INSERT INTO Aluno (id_responsavel, nome, idade, cpf, necessidade_especial) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

           stmt.setInt(1, aluno.getResponsavel().getId()); // ID do responsável já deve existir!
           stmt.setString(2,aluno.nome);
           stmt.setInt(3, aluno.getIdade());
           stmt.setString(4,aluno.getCpf());
           stmt.setString(5, aluno.getNecessidadeEspecial());

            int linhasAfetadas = stmt.executeUpdate();

            // Recuperar ID gerado
            if (linhasAfetadas > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int idGerado = generatedKeys.getInt(1);
                        aluno.setId(idGerado);
                        return idGerado;
                    }
                }
            }

            return null;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao cadastrar aluno: " + e.getMessage(), e);
        }
    }

    // READ - Buscar aluno com seus dados completos (incluindo responsável)
    public Aluno buscarPorId(int id) {
        String sql = "SELECT a.*, r.nome as nome_responsavel, r.idade as idade_responsavel, " +
                "r.cpf as cpf_responsavel, r.telefone, r.parentesco " +
                "FROM Aluno a " +
                "INNER JOIN Responsavel r ON a.id_responsavel = r.id_responsavel " +
                "WHERE a.id_aluno = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                // Primeiro criar o responsável
                Responsavel responsavel = new Responsavel(
                        rs.getInt("id_responsavel"),
                        rs.getString("nome_responsavel"),
                        rs.getInt("idade_responsavel"),
                        rs.getString("cpf_responsavel"),
                        rs.getString("parentesco"),
                        rs.getString("telefone")
                );

                // Depois criar o aluno com o responsável
                Aluno aluno = new Aluno(
                        rs.getInt("id_aluno"),
                        rs.getString("nome"),
                        rs.getInt("idade"),
                        rs.getString("cpf"),
                        responsavel,
                        rs.getString("necessidade_especial")
                );
                // Carregar irmãos
                //carregarIrmaos(aluno, connection);

                return aluno;
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar aluno: " + e.getMessage(), e);
        }

        return null;
    }

    // READ - Listar todos os alunos
    public List<Aluno> listarTodos() {
        List<Aluno> alunos = new ArrayList<>();
        String sql = "SELECT a.*, r.nome as nome_responsavel, r.telefone, r.parentesco, r.idade as idade_responsavel, r.cpf as cpf_responsavel " +
                "FROM Aluno a " +
                "INNER JOIN Responsavel r ON a.id_responsavel = r.id_responsavel";

        try(PreparedStatement stmt = connection.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery()){

            while (rs.next()){
                Responsavel responsavel = new Responsavel(
                        rs.getInt("id_responsavel"),
                        rs.getString("nome_responsavel"),
                        rs.getInt("idade_responsavel"),
                        rs.getString("cpf_responsavel"),
                        rs.getString("parentesco"),
                        rs.getString("telefone")
                );

                Aluno aluno = new Aluno(
                        rs.getInt("id_aluno"),
                        rs.getString("nome"),
                        rs.getInt("idade"),
                        rs.getString("cpf"),
                        responsavel,
                        rs.getString("necessidade_especial")
                );
                // CARREGAR IRMAOS CADA ALUNO
                //carregarIrmaos(aluno, connection);

                alunos.add(aluno);
            }
        } catch (SQLException e){
            throw new RuntimeException("Erro ao listar alunos: "+ e.getMessage(),e);
        }

        return alunos;
    }

    // UPDATE - Atualizar aluno
    public void atualizar(Aluno aluno) {
        String sql = "UPDATE Aluno SET nome = ?, idade = ?, cpf = ?, necessidade_especial = ?, id_responsavel = ? WHERE id_aluno = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, aluno.getNome());
            stmt.setInt(2, aluno.getIdade());
            stmt.setString(3, aluno.getCpf());
            stmt.setString(4, aluno.getNecessidadeEspecial());
            stmt.setInt(5, aluno.getResponsavel().getId());
            stmt.setInt(6, aluno.getId());

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar aluno: " + e.getMessage(), e);
        }
    }

    // DELETE - Excluir aluno
    public void excluir(int id) {
        String sql = "DELETE FROM Aluno WHERE id_aluno = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao excluir aluno: " + e.getMessage(), e);
        }
    }
    // No AlunoDAO, adicione este método para carregar irmãos
    public void carregarIrmaos(Aluno aluno, Connection connection) {
        IrmaosDAO irmaosDAO = new IrmaosDAO(connection);
        List<Integer> idsIrmaos = irmaosDAO.buscarIrmaosPorId(aluno.getId());

        for (int idIrmao : idsIrmaos) {
            Aluno irmao = buscarPorId(idIrmao);
            if (irmao != null && !aluno.getIrmaos().contains(irmao)) {
                aluno.getIrmaos().add(irmao);
            }
        }
    }
}
