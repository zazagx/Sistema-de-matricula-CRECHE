import java.sql.*;

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
}
