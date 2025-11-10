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
    // READ - Listar todas as turmas (com dados do professor)
    public List<Turma> listarTodos() {
        List<Turma> turmas = new ArrayList<>();

        String sql = "SELECT t.*, f.id_funcionario, f.nome AS nome_funcionario, f.idade, f.cpf, f.cargo, f.vinculo, f.turno " +
                "FROM Turma t " +
                "LEFT JOIN Funcionario f ON t.id_professor = f.id_funcionario";

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {

                // Cria o professor associado (se existir)
                Professor professor = null;
                if (rs.getInt("id_professor") != 0) {
                    professor = new Professor(
                            rs.getInt("id_funcionario"),
                            rs.getString("nome_funcionario"),
                            rs.getInt("idade"),
                            rs.getString("cpf"),
                            rs.getString("vinculo"),
                            rs.getString("turno")
                    );
                }

                // Agora cria a turma conforme o tipo
                Turma turma;
                switch (rs.getString("tipo_turma")) {
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
                        throw new SQLException("Tipo de turma desconhecido!");
                }

                turmas.add(turma);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar turmas: " + e.getMessage(), e);
        }

        return turmas;
    }
    public Turma buscarPorId(int id) {
        String sql = "SELECT * FROM Turma WHERE id_turma = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Turma turma = new Turma() {
                    @Override
                    public boolean verificarIdadeAluno(int idade) {
                        return false;
                    }
                };
                turma.setId(rs.getInt("id_turma"));
                turma.setNome(rs.getString("nome"));
                turma.setTurno(rs.getString("turno"));
                // adicione outros campos se existirem
                return turma;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar turma por ID: " + e.getMessage(), e);
        }
        return null;
    }


}
