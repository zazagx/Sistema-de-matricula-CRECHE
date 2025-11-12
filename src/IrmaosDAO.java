import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class IrmaosDAO {
    private Connection connection;

    public IrmaosDAO(Connection connection) {
        this.connection = connection;
    }

    // Adicionar relação de irmãos (bidirecional)
    public void adicionarIrmaos(int idAluno1, int idAluno2) {
        // Garante que a relação seja salva apenas uma vez (id menor primeiro)
        int menorId = Math.min(idAluno1, idAluno2);
        int maiorId = Math.max(idAluno1, idAluno2);

        String sql = "INSERT INTO Irmaos (id_irmao1, id_irmao2) VALUES (?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, menorId);
            stmt.setInt(2, maiorId);
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao adicionar relação de irmãos: " + e.getMessage(), e);
        }
    }

    // Remover relação de irmãos
    public void removerIrmaos(int idAluno1, int idAluno2) {
        int menorId = Math.min(idAluno1, idAluno2);
        int maiorId = Math.max(idAluno1, idAluno2);

        String sql = "DELETE FROM Irmaos WHERE id_irmao1 = ? AND id_irmao2 = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, menorId);
            stmt.setInt(2, maiorId);
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao remover relação de irmãos: " + e.getMessage(), e);
        }
    }

    // Buscar todos os irmãos de um aluno
    public List<Integer> buscarIrmaosPorId(int idAluno) {
        List<Integer> idsIrmaos = new ArrayList<>();

        String sql = "SELECT id_irmao1, id_irmao2 FROM Irmaos " +
                "WHERE id_irmao1 = ? OR id_irmao2 = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idAluno);
            stmt.setInt(2, idAluno);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int id1 = rs.getInt("id_irmao1");
                int id2 = rs.getInt("id_irmao2");

                // Adiciona o ID que não é o aluno atual
                if (id1 == idAluno) {
                    idsIrmaos.add(id2);
                } else {
                    idsIrmaos.add(id1);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar irmãos: " + e.getMessage(), e);
        }

        return idsIrmaos;
    }

    // Verificar se dois alunos são irmãos
    public boolean saoIrmaos(int idAluno1, int idAluno2) {
        int menorId = Math.min(idAluno1, idAluno2);
        int maiorId = Math.max(idAluno1, idAluno2);

        String sql = "SELECT COUNT(*) FROM Irmaos WHERE id_irmao1 = ? AND id_irmao2 = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, menorId);
            stmt.setInt(2, maiorId);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao verificar relação de irmãos: " + e.getMessage(), e);
        }

        return false;
    }
}