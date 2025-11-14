import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MatriculaDAO {
    private Connection connection;

    MatriculaDAO(Connection connection){
        this.connection = connection;
    }

    // CREATE - Cadastrar aluno (AGORA COM ID AUTOMÁTICO)
    public Integer cadastrar(Matricula matricula) {
        try {
            PreparedStatement stmt;
            if (matricula.isPreMatricula()) {
                String sql = "INSERT INTO Matricula (pre_matricula, id_aluno, observacoes, endereco, data_matricula) VALUES (?, ?, ?, ?, ?)";
                stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            } else {
                String sql = "INSERT INTO Matricula (pre_matricula, id_aluno, observacoes, endereco, data_matricula, id_funcionario, id_turma) VALUES (?, ?, ?, ?, ?, ?, ?)";
                stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                stmt.setInt(6, matricula.getFuncionario().getId());
                stmt.setInt(7, matricula.getTurma().getId());
            }

            stmt.setBoolean(1, matricula.isPreMatricula());
            stmt.setInt(2, matricula.getAluno().getId());
            stmt.setString(3, matricula.getObservacoes());
            stmt.setString(4, matricula.getEndereco());
            stmt.setDate(5, Date.valueOf(matricula.getData()));

            int linhasAfetadas = stmt.executeUpdate();

            if (linhasAfetadas > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int idGerado = generatedKeys.getInt(1);
                        matricula.setNumeroMatricula(idGerado);

                        // Insere responsáveis sem duplicar
                        inserirResponsaveis(matricula);
                        return idGerado;
                    }
                }
            }

            return null;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao cadastrar matrícula: " + e.getMessage(), e);
        }
    }

    private void inserirResponsaveis(Matricula matricula) throws SQLException {
        String checkSql = "SELECT COUNT(*) FROM Matricula_Responsavel WHERE id_matricula = ? AND id_responsavel = ?";
        String insertSql = "INSERT INTO Matricula_Responsavel (id_matricula, id_responsavel) VALUES (?, ?)";

        try (
                PreparedStatement checkStmt = connection.prepareStatement(checkSql);
                PreparedStatement insertStmt = connection.prepareStatement(insertSql)
        ) {
            for (Responsavel resp : matricula.getResponsaveis()) {
                checkStmt.setInt(1, matricula.getNumeroMatricula());
                checkStmt.setInt(2, resp.getId());

                try (ResultSet rs = checkStmt.executeQuery()) {
                    rs.next();
                    if (rs.getInt(1) == 0) {
                        // Só insere se ainda não existir
                        insertStmt.setInt(1, matricula.getNumeroMatricula());
                        insertStmt.setInt(2, resp.getId());
                        insertStmt.addBatch();
                    }
                }
            }
            insertStmt.executeBatch();
        }
    }
    private List<Responsavel> buscarResponsaveisPorMatricula(int idMatricula) throws SQLException {
        List<Responsavel> responsaveis = new ArrayList<>();
        String sql = "SELECT r.* FROM Responsavel r " +
                "JOIN Matricula_Responsavel mr ON r.id_responsavel = mr.id_responsavel " +
                "WHERE mr.id_matricula = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idMatricula);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Responsavel resp = new Responsavel(
                            rs.getInt("id_responsavel"),
                            rs.getString("nome"),
                            rs.getInt("idade"),
                            rs.getString("cpf"),
                            rs.getString("parentesco"),
                            rs.getString("telefone")
                    );
                    responsaveis.add(resp);
                }
            }
        }
        return responsaveis;
    }
    // READ - Listar todas as matrículas
    public List<Matricula> listarTodos() {
        List<Matricula> matriculas = new ArrayList<>();
        String sql = "SELECT m.*, a.nome as nome_aluno " +
                "FROM Matricula m " +
                "INNER JOIN Aluno a ON a.id_aluno = m.id_aluno";

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                // --- Dados básicos ---
                int numeroMatricula = rs.getInt("id_matricula");
                boolean preMatricula = rs.getBoolean("pre_matricula");
                String observacoes = rs.getString("observacoes");
                String endereco = rs.getString("endereco");
                LocalDate date = rs.getDate("data_matricula").toLocalDate();

                // --- Aluno ---
                AlunoDAO alunoDAO = new AlunoDAO(connection);
                Aluno aluno = alunoDAO.buscarPorId(rs.getInt("id_aluno"));

                // --- Funcionário e Turma (APENAS se não for pré-matrícula) ---
                Funcionario funcionario = null;
                Turma turma = null;

                if (!preMatricula) {
                    int idFuncionario = rs.getInt("id_funcionario");
                    int idTurma = rs.getInt("id_turma");

                    if (idFuncionario > 0) {
                        FuncionarioDAO funcionarioDAO = new FuncionarioDAO(connection);
                        funcionario = funcionarioDAO.buscarPorId(idFuncionario);
                    }
                    if (idTurma > 0) {
                        TurmaDAO turmaDAO = new TurmaDAO(connection);
                        turma = turmaDAO.buscarPorId(idTurma);
                    }
                }

                // --- Responsáveis ---
                List<Responsavel> responsaveis = buscarResponsaveisPorMatricula(numeroMatricula);

                // --- Situação (ajustar conforme sua tabela) ---
                SituacaoMatricula situacao = SituacaoMatricula.ATIVA; // ou buscar do RS se existir

                // --- CRIAR E ADICIONAR MATRÍCULA ---
                Matricula matricula = new Matricula(
                        numeroMatricula, date, situacao, observacoes, endereco,
                        aluno, responsaveis, funcionario, turma, preMatricula
                );

                matriculas.add(matricula); // ← LINHA QUE ESTAVA FALTANDO!
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar matrículas: " + e.getMessage(), e);
        }

        return matriculas;
    }

    // Mapeia cada linha do ResultSet para um objeto Matricula
    private Matricula mapearMatricula(ResultSet rs) throws SQLException {

        // Busca aluno vinculado
        Matricula matricula = new Matricula();
        AlunoDAO alunoDAO = new AlunoDAO(connection);
        Aluno aluno = alunoDAO.buscarPorId(rs.getInt("id_aluno"));
        matricula.setAluno(aluno);

        matricula.setNumeroMatricula(rs.getInt("id_matricula"));
        matricula.setPreMatricula(rs.getBoolean("pre_matricula"));
        matricula.setObservacoes(rs.getString("observacoes"));
        matricula.setEndereco(rs.getString("endereco"));
        matricula.setData(rs.getDate("data_matricula").toLocalDate());




        // Se não for pré-matrícula, busca também funcionário e turma
        if (!matricula.isPreMatricula()) {
            int idFuncionario = rs.getInt("id_funcionario");
            int idTurma = rs.getInt("id_turma");

            FuncionarioDAO funcionarioDAO = new FuncionarioDAO(connection);
            TurmaDAO turmaDAO = new TurmaDAO(connection);

            Funcionario funcionario = funcionarioDAO.buscarPorId(idFuncionario);
            Turma turma = turmaDAO.buscarPorId(idTurma);

            matricula.setFuncionario(funcionario);
            matricula.setTurma(turma);
        }


        // Buscar os responsáveis associados
        List<Responsavel> responsaveis = buscarResponsaveisPorMatricula(matricula.getNumeroMatricula());
        matricula.setResponsaveis(responsaveis);

        return matricula;
    }
    // READ - Buscar matrícula por ID
    public Matricula buscarPorId(int idMatricula) {
        String sql = "SELECT * FROM Matricula WHERE id_matricula = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idMatricula);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    // Reutiliza o método mapearMatricula para manter a consistência
                    return mapearMatricula(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar matrícula por ID: " + e.getMessage(), e);
        }

        return null; // Retorna null se não encontrar nenhuma matrícula com o ID informado
    }
    public void atualizar(Matricula matricula) {
        String sql = "UPDATE Matricula SET pre_matricula = ?, situacao = ?, id_funcionario = ?, id_turma = ? WHERE id_matricula = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setBoolean(1, matricula.isPreMatricula());
            stmt.setString(2, matricula.getSituacao().name());
            stmt.setInt(3, matricula.getFuncionario().getId());
            stmt.setInt(4, matricula.getTurma().getId());
            stmt.setInt(5, matricula.getNumeroMatricula());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar matrícula: " + e.getMessage(), e);
        }
    }
    // READ - Listar pré-matrículas (somente as que têm pre_matricula = 1)
    public List<Matricula> listarPreMatriculas() {
        List<Matricula> preMatriculas = new ArrayList<>();

        String sql = """
        SELECT m.id_matricula,m.pre_matricula,m.id_aluno, 
               m.observacoes, m.id_funcionario, m.id_turma, m.endereco, m.data_matricula,
               a.nome AS nome_aluno
        FROM Matricula m
        JOIN Aluno a ON m.id_aluno = a.id_aluno
        WHERE m.pre_matricula = true
    """;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Matricula matricula = new Matricula();
                matricula.setNumeroMatricula(rs.getInt("id_matricula"));
                matricula.setPreMatricula(rs.getBoolean("pre_matricula"));
                matricula.setSituacao(SituacaoMatricula.valueOf(rs.getString(SituacaoMatricula.ATIVA.ordinal())));

                // Criar o objeto Aluno dentro da matrícula
                AlunoDAO alunoDAO = new AlunoDAO(connection);
                Aluno aluno = alunoDAO.buscarPorId(rs.getInt("id_aluno"));
                //aluno.setId(rs.getInt("id_aluno"));
                //aluno.setNome(rs.getString("nome_aluno"));
                matricula.setAluno(aluno);

                // (Opcional) se quiser carregar também o funcionário e turma
                matricula.setFuncionario(null);
                matricula.setTurma(null);

                preMatriculas.add(matricula);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar pré-matrículas: " + e.getMessage(), e);
        }

        return preMatriculas;
    }


}

