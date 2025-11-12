import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TurmaDAO {
    private Connection connection;
    TurmaDAO(Connection connection){
        this.connection = connection;
    }
    // CREATE - Cadastrar turma

    public Integer cadastrar(Turma turma) {
        String sql = "";

        try (PreparedStatement stmt = prepararStatement(turma)) {
            int linhasAfetadas = stmt.executeUpdate();

            if (linhasAfetadas > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int idGerado = generatedKeys.getInt(1);
                        turma.setId(idGerado);
                        return idGerado;
                    }
                }
            }

            return null;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao cadastrar turma: " + e.getMessage(), e);
        }
    }

    private PreparedStatement prepararStatement(Turma turma) throws SQLException {
        String sql;
        PreparedStatement stmt;

        if (turma instanceof TurmaCreche) {
            sql = "INSERT INTO Turma (nome, faixa_etaria, turno, tipo_turma, hora_cochilo, id_professor) VALUES (?, ?, ?, ?, ?, ?)";
            stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(4,"Creche");
            stmt.setString(5, ((TurmaCreche) turma).getHoraCochilo());
        } else if (turma instanceof TurmaInfantil) {
            sql = "INSERT INTO Turma (nome, faixa_etaria, turno, tipo_turma, aulas_psicomotricidade, id_professor) VALUES (?, ?, ?, ?, ?, ?)";
            stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(4,"Infantil");
            stmt.setString(5, ((TurmaInfantil) turma).getAulasPsicomotricidade());
        } else if (turma instanceof TurmaPre) {
            sql = "INSERT INTO Turma (nome, faixa_etaria, turno, tipo_turma, aulas_alfabetizacao, id_professor) VALUES (?, ?, ?, ?, ?, ?)";
            stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(4,"Pre");
            stmt.setString(5, ((TurmaPre) turma).getAulasAlfabetizacao());
        } else {
            throw new SQLException("Tipo de funcionário desconhecido!");
        }

        // Campos comuns
        stmt.setString(1, turma.getNome());
        stmt.setString(2, turma.getFaixaEtaria());
        stmt.setString(3, turma.turno);
        stmt.setInt(6,turma.getProfessor().getId());

        return stmt;
    }
    // READ - Buscar turma por ID
    public Turma buscarPorId(int idTurma) {
        String sql = "SELECT * FROM Turma WHERE id_turma = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idTurma);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearTurma(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar turma por ID: " + e.getMessage(), e);
        }
        return null;
    }

    // READ - Listar todas as turmas
    public List<Turma> listarTodos() {
        List<Turma> turmas = new ArrayList<>();
        String sql = "SELECT * FROM Turma ORDER BY id_turma";

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Turma turma = mapearTurma(rs);
                turmas.add(turma);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar turmas: " + e.getMessage(), e);
        }
        return turmas;
    }
    // Método auxiliar para reconstruir o objeto Turma completo
    private Turma mapearTurma(ResultSet rs) throws SQLException {
        String tipo = rs.getString("tipo_turma");
        int idProfessor = rs.getInt("id_professor");

        // Busca o professor antes de instanciar a turma
        FuncionarioDAO funcionarioDAO = new FuncionarioDAO(connection);
        Professor professor = (Professor) funcionarioDAO.buscarPorId(idProfessor);

        Turma turma;

        switch (tipo) {
            case "Creche":
                turma = new TurmaCreche(
                        rs.getInt("id_turma"),
                        rs.getString("nome"),
                        rs.getString("faixa_etaria"),
                        rs.getString("turno"),
                        rs.getString("hora_cochilo"),
                        professor
                );
                break;

            case "Infantil":
                turma = new TurmaInfantil(
                        rs.getInt("id_turma"),
                        rs.getString("nome"),
                        rs.getString("faixa_etaria"),
                        rs.getString("turno"),
                        rs.getString("aulas_psicomotricidade"),
                        professor
                );
                break;

            case "Pre":
                turma = new TurmaPre(
                        rs.getInt("id_turma"),
                        rs.getString("nome"),
                        rs.getString("faixa_etaria"),
                        rs.getString("turno"),
                        rs.getString("aulas_alfabetizacao"),
                        professor
                );
                break;

            default:
                throw new SQLException("Tipo de turma desconhecido: " + tipo);
        }

        return turma;
    }
        // Buscar o professor vinculado

}
