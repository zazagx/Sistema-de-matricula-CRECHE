import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FuncionarioDAO {
    private Connection connection;

    FuncionarioDAO(Connection connection){
        this.connection = connection;
    }

    // CREATE - Cadastrar responsável
    public Integer cadastrar(Funcionario funcionario) {
        String sql = "INSERT INTO Funcionario (id_funcionario, nome, idade, cpf, cargo, vinculo, turno, setor_responsavel, faixa_etaria_atendida) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        // Usando RETURN_GENERATED_KEYS para pegar o ID auto-incrementado
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1,funcionario.getId());
            stmt.setString(2,funcionario.getNome());
            stmt.setInt(3,funcionario.getIdade());
            stmt.setString(4,funcionario.getCpf());
            stmt.setString(5,funcionario.getCargo());
            stmt.setString(6,funcionario.getVinculo());
            if (funcionario instanceof Professor){
                stmt.setString(7,((Professor) funcionario).getTurno());
            }else {
                stmt.setString(7,null);
            }
            if (funcionario instanceof Coordenador){
                stmt.setString(8,((Coordenador)funcionario).getSetorResponsavel());
            }else {
                stmt.setString(8,null);
            }
            if (funcionario instanceof Cuidador){
                stmt.setString(9,((Cuidador)funcionario).getFaixaEtariaAtendida());
            }else {
                stmt.setString(9,null);
            }

            int linhasAfetadas = stmt.executeUpdate();

            // Recuperar o ID gerado automaticamente
            if (linhasAfetadas > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int idGerado = generatedKeys.getInt(1);
                        funcionario.setId(idGerado); // Atualiza o ID no objeto
                        return idGerado;
                    }
                }
            }

            return null;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao cadastrar responsável: " + e.getMessage(), e);
        }
    }

    // READ - Listar todos
    public List<Funcionario> listarTodos() {
        List<Funcionario> funcionarios = new ArrayList<>();
        String sql = "SELECT * FROM Funcionarios ORDER BY id_funcionario";

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


    // Método auxiliar para mapear ResultSet para objeto Responsavel
    private Funcionario mapearFuncionario(ResultSet rs) throws SQLException {
        return new Funcionario(
                rs.getInt("id_funcionario"),
                rs.getString("nome"),
                rs.getInt("idade"),
                rs.getString("cpf"),
                rs.getString("cargo"),
                rs.getString("vinculo")
        ) {
        };
    }

}
