import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FuncionarioDAO {
    private Connection connection;

    FuncionarioDAO(Connection connection){
        this.connection = connection;
    }
    // CREATE - Cadastrar funcionario
    public Integer cadastrar(Funcionario funcionario) {
        String sql = "";

        try (PreparedStatement stmt = prepararStatement(funcionario)) {
            int linhasAfetadas = stmt.executeUpdate();

            if (linhasAfetadas > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int idGerado = generatedKeys.getInt(1);
                        funcionario.setId(idGerado);
                        return idGerado;
                    }
                }
            }

            return null;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao cadastrar funcionário: " + e.getMessage(), e);
        }
    }

    private PreparedStatement prepararStatement(Funcionario funcionario) throws SQLException {
        String sql;
        PreparedStatement stmt;

        if (funcionario instanceof Professor) {
            sql = "INSERT INTO Funcionario (nome, idade, cpf, cargo, vinculo, turno) VALUES (?, ?, ?, ?, ?, ?)";
            stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(6, ((Professor) funcionario).getTurno());
        } else if (funcionario instanceof Coordenador) {
            sql = "INSERT INTO Funcionario (nome, idade, cpf, cargo, vinculo, setor_responsavel) VALUES (?, ?, ?, ?, ?, ?)";
            stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(6, ((Coordenador) funcionario).getSetorResponsavel());
        } else if (funcionario instanceof Cuidador) {
            sql = "INSERT INTO Funcionario (nome, idade, cpf, cargo, vinculo, faixa_etaria_atendida) VALUES (?, ?, ?, ?, ?, ?)";
            stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(6, ((Cuidador) funcionario).getFaixaEtariaAtendida());
        } else {
            throw new SQLException("Tipo de funcionário desconhecido!");
        }

        // Campos comuns
        stmt.setString(1, funcionario.getNome());
        stmt.setInt(2, funcionario.getIdade());
        stmt.setString(3, funcionario.getCpf());
        stmt.setString(4, funcionario.getCargo());
        stmt.setString(5, funcionario.getVinculo());

        return stmt;
    }


    // READ - Listar todos
    public List<Funcionario> listarTodos() {
        List<Funcionario> funcionarios = new ArrayList<>();
        String sql = "SELECT * FROM Funcionario ORDER BY id_funcionario";

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Funcionario funcionario = mapearFuncionario(rs);
                funcionarios.add(funcionario);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar responsáveis: " + e.getMessage(), e);
        }

        return funcionarios;
    }
    public Funcionario buscarPorId(int id) {
        Funcionario funcionario = null;
        String sql = "SELECT * FROM Funcionario WHERE id_funcionario = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                funcionario = mapearFuncionario(rs);
                return funcionario;
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar funcionario por ID: " + e.getMessage(), e);
        }

        return funcionario;
    }
    // READ - Listar funcionários por cargo
    public List<Funcionario> listarPorCargo(String cargo) {
        List<Funcionario> funcionarios = new ArrayList<>();
        String sql = "SELECT * FROM Funcionario WHERE cargo = ? ORDER BY id_funcionario";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, cargo);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Funcionario funcionario = mapearFuncionario(rs);
                    funcionarios.add(funcionario);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar funcionários por cargo: " + e.getMessage(), e);
        }

        return funcionarios;
    }


    // Método auxiliar para mapear ResultSet para objeto Responsavel
    private Funcionario mapearFuncionario(ResultSet rs) throws SQLException {
        String cargo = rs.getString("cargo");

        switch (cargo) {
            case "Professor":
                Professor professor = new Professor(
                        rs.getInt("id_funcionario"),
                        rs.getString("nome"),
                        rs.getInt("idade"),
                        rs.getString("cpf"),
                        rs.getString("vinculo"),
                        rs.getString("turno") // adiciona o campo específico
                );
                return professor;

            case "Coordenador":
                Coordenador coordenador = new Coordenador(
                        rs.getInt("id_funcionario"),
                        rs.getString("nome"),
                        rs.getInt("idade"),
                        rs.getString("cpf"),
                        rs.getString("vinculo"),
                        rs.getString("setor_responsavel")
                );
                return coordenador;

            case "Cuidador":
                Cuidador cuidador = new Cuidador(
                        rs.getInt("id_funcionario"),
                        rs.getString("nome"),
                        rs.getInt("idade"),
                        rs.getString("cpf"),
                        rs.getString("vinculo"),
                        rs.getString("faixa_etaria_atendida")
                );
                return cuidador;

            default:
                return new Funcionario(
                        rs.getInt("id_funcionario"),
                        rs.getString("nome"),
                        rs.getInt("idade"),
                        rs.getString("cpf"),
                        rs.getString("cargo"),
                        rs.getString("vinculo")
                ) { };

        }
    }

}
