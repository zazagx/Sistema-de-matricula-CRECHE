import java.util.*;
import java.time.LocalDate;

enum SituacaoMatricula {
    ATIVA, INATIVA, PENDENTE, CONCLUIDA
}

abstract class Pessoa {
    protected int id;
    protected String nome;
    protected int idade;
    protected String cpf;

    public Pessoa(int id, String nome, int idade, String cpf) {
        this.id = id;
        this.nome = nome;
        this.idade = idade;
        this.cpf = cpf;
    }

    public int getId() { return id; }
    public String getNome() { return nome; }
    public int getIdade() { return idade; }
    public String getCpf() { return cpf; }

    public void setNome(String nome) { this.nome = nome; }
    public void setIdade(int idade) { this.idade = idade; }
    public void setCpf(String cpf) { this.cpf = cpf; }

    @Override
    public String toString() {
        return "ID: " + id + ", Nome: " + nome + ", Idade: " + idade + ", CPF: " + cpf;
    }
}



class Aluno extends Pessoa {
    private Responsavel responsavel;
    private Turma turma;
    private String necessidadeEspecial;
    private List<Aluno> irmaos = new ArrayList<>();

    public Aluno(int id, String nome, int idade, String cpf, Responsavel responsavel, String necessidadeEspecial) {
        super(id, nome, idade, cpf);
        this.responsavel = responsavel;
        this.necessidadeEspecial = necessidadeEspecial;
    }
    public List<Aluno> getIrmaos(){
        return irmaos;
    }
    public void adicionarIrmao(Aluno irmao){
        if(irmao != null && !irmaos.contains(irmao) && !irmao.equals(this)){
            irmaos.add(irmao);
            irmao.getIrmaos().add(this);
        }
    } //Adiciona um irmao de forma automatica para todos os alunos que possuem irmandade



    public Responsavel getResponsavel() { return responsavel; }
    public Turma getTurma() { return turma; }
    public String getNecessidadeEspecial() { return necessidadeEspecial; }

    public void setResponsavel(Responsavel responsavel) { this.responsavel = responsavel; }
    public void setTurma(Turma turma) { this.turma = turma; }
    public void setNecessidadeEspecial(String necessidadeEspecial) { this.necessidadeEspecial = necessidadeEspecial; }

    @Override
    public String toString() {
        String nomesIrmaos = irmaos.isEmpty() ? "Nenhum" :
                irmaos.stream().map(Aluno::getNome).reduce((a,b) -> a + ", "+ b).orElse("");
        return super.toString() + ", Responsável: " + responsavel.getNome() +
                ", Necessidade Especial: " + (necessidadeEspecial.isEmpty() ? "Nenhuma" : necessidadeEspecial) +
                ", Irmãos: " + nomesIrmaos;
    }
}


abstract class Funcionario extends Pessoa {
    protected String cargo;
    protected String vinculo;

    public Funcionario(int id, String nome, int idade, String cpf, String cargo, String vinculo) {
        super(id, nome, idade, cpf);
        this.cargo = cargo;
        this.vinculo = vinculo;
    }

    // Getters e Setters
    public String getCargo() { return cargo; }
    public String getVinculo() { return vinculo; }

    public void setCargo(String cargo) { this.cargo = cargo; }
    public void setVinculo(String vinculo) { this.vinculo = vinculo; }

    @Override
    public String toString() {
        return super.toString() + ", Cargo: " + cargo + ", Vínculo: " + vinculo;
    }
}


class Professor extends Funcionario {
    private Turma turma;
    private String turno;

    public Professor(int id, String nome, int idade, String cpf, String vinculo, String turno) {
        super(id, nome, idade, cpf, "Professor", vinculo);
        this.turno = turno;
    }

    public Turma getTurma() { return turma; }
    public String getTurno() { return turno; }

    public void setTurma(Turma turma) { this.turma = turma; }
    public void setTurno(String turno) { this.turno = turno; }

    @Override
    public String toString() {
        return super.toString() + ", Turno: " + turno;
    }
}


class Cuidador extends Funcionario {
    private String faixaEtariaAtendida;

    public Cuidador(int id, String nome, int idade, String cpf, String vinculo, String faixaEtariaAtendida) {
        super(id, nome, idade, cpf, "Cuidador", vinculo);
        this.faixaEtariaAtendida = faixaEtariaAtendida;
    }


    public String getFaixaEtariaAtendida() { return faixaEtariaAtendida; }
    public void setFaixaEtariaAtendida(String faixaEtariaAtendida) { this.faixaEtariaAtendida = faixaEtariaAtendida; }

    @Override
    public String toString() {
        return super.toString() + ", Faixa Etária Atendida: " + faixaEtariaAtendida;
    }
}


class Coordenador extends Funcionario {
    private String setorResponsavel;

    public Coordenador(int id, String nome, int idade, String cpf, String vinculo, String setorResponsavel) {
        super(id, nome, idade, cpf, "Coordenador", vinculo);
        this.setorResponsavel = setorResponsavel;
    }


    public String getSetorResponsavel() { return setorResponsavel; }
    public void setSetorResponsavel(String setorResponsavel) { this.setorResponsavel = setorResponsavel; }

    @Override
    public String toString() {
        return super.toString() + ", Setor Responsável: " + setorResponsavel;
    }
}


class Responsavel extends Pessoa {
    private String parentesco;
    private String telefone;

    public Responsavel(int id, String nome, int idade, String cpf, String parentesco, String telefone) {
        super(id, nome, idade, cpf);
        this.parentesco = parentesco;
        this.telefone = telefone;
    }

    public String getParentesco() { return parentesco; }
    public String getTelefone() { return telefone; }

    public void setParentesco(String parentesco) { this.parentesco = parentesco; }
    public void setTelefone(String telefone) { this.telefone = telefone; }

    @Override
    public String toString() {
        return super.toString() + ", Parentesco: " + parentesco + ", Telefone: " + telefone;
    }
}


abstract class Turma {
    protected int id;
    protected String nome;
    protected String faixaEtaria;
    protected String turno;
    protected List<Aluno> alunos;
    protected Professor professor;

    public Turma(int id, String nome, String faixaEtaria, String turno) {
        this.id = id;
        this.nome = nome;
        this.faixaEtaria = faixaEtaria;
        this.turno = turno;
        this.alunos = new ArrayList<>();
    }

    // Método para adicionar aluno verificando a idade
    public boolean adicionarAluno(Aluno aluno) {
        if (verificarIdadeAluno(aluno.getIdade())) {
            alunos.add(aluno);
            aluno.setTurma(this);
            return true;
        }
        return false;
    }

    // Método abstrato para verificar idade do aluno
    public abstract boolean verificarIdadeAluno(int idade);

    // Getters e Setters
    public int getId() { return id; }
    public String getNome() { return nome; }
    public String getFaixaEtaria() { return faixaEtaria; }
    public String getTurno() { return turno; }
    public List<Aluno> getAlunos() { return alunos; }
    public Professor getProfessor() { return professor; }

    public void setNome(String nome) { this.nome = nome; }
    public void setTurno(String turno) { this.turno = turno; }
    public void setProfessor(Professor professor) { this.professor = professor; }

    @Override
    public String toString() {
        return "ID: " + id + ", Nome: " + nome + ", Faixa Etária: " + faixaEtaria +
                ", Turno: " + turno + ", Professor: " + (professor != null ? professor.getNome() : "Não atribuído") +
                ", Número de Alunos: " + alunos.size();
    }
}

// Classe Turma Creche (2-3 anos)
class TurmaCreche extends Turma {
    private String horaCochilo;

    public TurmaCreche(int id, String nome, String turno, String horaCochilo) {
        super(id, nome, "2-3 anos", turno);
        this.horaCochilo = horaCochilo;
    }

    @Override
    public boolean verificarIdadeAluno(int idade) {
        return idade >= 2 && idade <= 3;
    }

    // Getters e Setters
    public String getHoraCochilo() { return horaCochilo; }
    public void setHoraCochilo(String horaCochilo) { this.horaCochilo = horaCochilo; }

    @Override
    public String toString() {
        return super.toString() + ", Hora do Cochilo: " + horaCochilo;
    }
}

// Classe Turma Infantil (4-5 anos)
class TurmaInfantil extends Turma {
    private String aulasPsicomotricidade;

    public TurmaInfantil(int id, String nome, String turno, String aulasPsicomotricidade) {
        super(id, nome, "4-5 anos", turno);
        this.aulasPsicomotricidade = aulasPsicomotricidade;
    }

    @Override
    public boolean verificarIdadeAluno(int idade) {
        return idade >= 4 && idade <= 5;
    }

    // Getters e Setters
    public String getAulasPsicomotricidade() { return aulasPsicomotricidade; }
    public void setAulasPsicomotricidade(String aulasPsicomotricidade) { this.aulasPsicomotricidade = aulasPsicomotricidade; }

    @Override
    public String toString() {
        return super.toString() + ", Aulas de Psicomotricidade: " + aulasPsicomotricidade;
    }
}

// Classe Turma Pré (6 anos)
class TurmaPre extends Turma {
    private String aulasAlfabetizacao;

    public TurmaPre(int id, String nome, String turno, String aulasAlfabetizacao) {
        super(id, nome, "6 anos", turno);
        this.aulasAlfabetizacao = aulasAlfabetizacao;
    }

    @Override
    public boolean verificarIdadeAluno(int idade) {
        return idade == 6;
    }

    // Getters e Setters
    public String getAulasAlfabetizacao() { return aulasAlfabetizacao; }
    public void setAulasAlfabetizacao(String aulasAlfabetizacao) { this.aulasAlfabetizacao = aulasAlfabetizacao; }

    @Override
    public String toString() {
        return super.toString() + ", Aulas de Alfabetização: " + aulasAlfabetizacao;
    }
}

// Classe Matrícula
class Matricula {
    private int numeroMatricula;
    private LocalDate data;
    private SituacaoMatricula situacao;
    private String observacoes;
    private String endereco;
    private Aluno aluno;
    private List<Responsavel> responsaveis;
    private Funcionario funcionario;
    private Turma turma;
    private boolean preMatricula;

    //  Construtor 1 (sem boolean)
    public Matricula(int numeroMatricula, LocalDate data, SituacaoMatricula situacao,
                     String observacoes, String endereco, Aluno aluno,
                     List<Responsavel> responsaveis, Funcionario funcionario, Turma turma) {
        this(numeroMatricula, data, situacao, observacoes, endereco, aluno,
                responsaveis, funcionario, turma, false);
    }

    // Construtor 2 (com boolean)


    public Matricula(int numeroMatricula, LocalDate data, SituacaoMatricula situacao,
                     String observacoes, String endereco, Aluno aluno,
                     List<Responsavel> responsaveis, Funcionario funcionario, Turma turma, boolean preMatricula) {
        this.numeroMatricula = numeroMatricula;
        this.data = data;
        this.situacao = situacao;
        this.observacoes = observacoes;
        this.endereco = endereco;
        this.aluno = aluno;
        this.responsaveis = responsaveis;
        this.funcionario = funcionario;
        this.turma = turma;
        this.preMatricula = preMatricula;
    }


    // Getters e Setters
    public int getNumeroMatricula() { return numeroMatricula; }
    public LocalDate getData() { return data; }
    public SituacaoMatricula getSituacao() { return situacao; }
    public String getObservacoes() { return observacoes; }
    public String getEndereco() { return endereco; }
    public Aluno getAluno() { return aluno; }
    public List<Responsavel> getResponsaveis() { return responsaveis; }
    public Funcionario getFuncionario() { return funcionario; }
    public Turma getTurma() { return turma; }
    public boolean isPreMatricula(){ return preMatricula; }
    public void setPreMatricula(boolean preMatricula){ this.preMatricula = preMatricula; }

    public void setSituacao(SituacaoMatricula situacao) { this.situacao = situacao; }
    public void setObservacoes(String observacoes) { this.observacoes = observacoes; }
    public void setEndereco(String endereco) { this.endereco = endereco; }

    public void setFuncionario(Funcionario funcionario) {
        this.funcionario = funcionario;
    }

    public void setTurma(Turma turma) {
        this.turma = turma;
    }

    @Override
    public String toString() {
        String tipo = preMatricula ? "[PRÉ-MATRÍCULA]" : "[MATŔICULA DEFINITIVA]";
        return tipo + "Número: " + numeroMatricula + ", Data: " + data + ", Situação: " + situacao +
                ", Aluno: " + aluno.getNome() + ", Turma: " + (turma != null ? turma.getNome() : "Não atribuída") +
                ", Responsáveis: " + responsaveis.size() + ", Funcionário: " + (funcionario != null ? funcionario.getNome() : "Não definido");
    }
}

// Classe principal Aplicativo
public class Aplicativo {
    private static List<Aluno> alunos = new ArrayList<>();
    private static List<Funcionario> funcionarios = new ArrayList<>();
    private static List<Responsavel> responsaveis = new ArrayList<>();
    private static List<Matricula> matriculas = new ArrayList<>();
    private static List<Turma> turmas = new ArrayList<>();
    private static Scanner scanner = new Scanner(System.in);
    private static int nextAlunoId = 1;
    private static int nextFuncionarioId = 1;
    private static int nextResponsavelId = 1;
    private static int nextMatriculaId = 1;
    private static int nextTurmaId = 1;

    public static void main(String[] args) {
        // Cadastro inicial de dados conforme solicitado
        cadastrarDadosIniciais();

        String comando;
        do {
            System.out.println("\n=== SISTEMA DE GERENCIAMENTO DE MATRÍCULAS ===");
            System.out.println("Comandos disponíveis:");
            System.out.println("cadastrar aluno, cadastrar funcionario, cadastrar responsavel");
            System.out.println("cadastrar pré-matrícula, converter matrícula, cadastrar matricula");
            System.out.println("adicionar irmãos, cadastrar turma");
            System.out.println("exibir alunos, exibir funcionarios, exibir responsaveis");
            System.out.println("exibir matriculas, exibir irmãos, exibir turmas, exibir pré-matriculados");
            System.out.println("finalizar");
            System.out.print("Digite um comando: ");

            comando = scanner.nextLine().toLowerCase();

            switch (comando) {
                case "cadastrar aluno":
                    cadastrarAluno();
                    break;
                case "cadastrar funcionario":
                    cadastrarFuncionario();
                    break;
                case "cadastrar responsavel":
                    cadastrarResponsavel();
                    break;
                case "cadastrar pré-matrícula":
                    cadastrarPreMatricula();
                    break;
                case "converter matrícula":
                    converterMatricula();
                    break;
                case "cadastrar matricula":
                    cadastrarMatricula();
                    break;
                case "adicionar irmãos":
                    adicionarIrmaos();
                    break;
                case "cadastrar turma":
                    cadastrarTurma();
                    break;
                case "exibir alunos":
                    exibirAlunos();
                    break;
                case "exibir funcionarios":
                    exibirFuncionarios();
                    break;
                case "exibir responsaveis":
                    exibirResponsaveis();
                    break;
                case "exibir matriculas":
                    exibirMatriculas();
                    break;
                case"exibir irmãos":
                    exibirIrmaos();
                    break;
                case "exibir turmas":
                    exibirTurmas();
                    break;
                case "exibir pré-matriculados":
                    exibirPreMatriculas();
                    break;
                case "finalizar":
                    System.out.println("Finalizando o sistema...");
                    break;
                default:
                    System.out.println("Comando não reconhecido. Tente novamente.");
            }
        } while (!comando.equals("finalizar"));

        scanner.close();
    }

    private static void cadastrarDadosIniciais() {
        // Cadastrar responsáveis
        Responsavel resp1 = new Responsavel(nextResponsavelId++, "Maria Silva", 35, "111.222.333-44", "Mãe", "(11) 99999-1111");
        Responsavel resp2 = new Responsavel(nextResponsavelId++, "João Santos", 40, "222.333.444-55", "Pai", "(11) 99999-2222");
        Responsavel resp3 = new Responsavel(nextResponsavelId++, "Ana Oliveira", 28, "333.444.555-66", "Mãe", "(11) 99999-3333");
        Responsavel resp4 = new Responsavel(nextResponsavelId++, "Pedro Costa", 32, "444.555.666-77", "Pai", "(11) 99999-4444");
        Responsavel resp5 = new Responsavel(nextResponsavelId++, "Carla Mendes", 38, "555.666.777-88", "Mãe", "(11) 99999-5555");
        Responsavel resp6 = new Responsavel(nextResponsavelId++, "Marcos Lima", 42, "666.777.888-99", "Pai", "(11) 99999-6666");

        responsaveis.addAll(Arrays.asList(resp1, resp2, resp3, resp4, resp5, resp6));

        // Cadastrar alunos
        Aluno aluno1 = new Aluno(nextAlunoId++, "Lucas Silva", 2, "123.456.789-00", resp1, "");
        Aluno aluno2 = new Aluno(nextAlunoId++, "Julia Santos", 3, "234.567.890-11", resp2, "");
        Aluno aluno3 = new Aluno(nextAlunoId++, "Miguel Oliveira", 4, "345.678.901-22", resp3, "");
        Aluno aluno4 = new Aluno(nextAlunoId++, "Sophia Costa", 5, "456.789.012-33", resp4, "");
        Aluno aluno5 = new Aluno(nextAlunoId++, "Arthur Mendes", 6, "567.890.123-44", resp5, "TDAH");
        Aluno aluno6 = new Aluno(nextAlunoId++, "Isabella Lima", 6, "678.901.234-55", resp6, "");

        alunos.addAll(Arrays.asList(aluno1, aluno2, aluno3, aluno4, aluno5, aluno6));

        // Cadastrar funcionários
        Professor prof1 = new Professor(nextFuncionarioId++, "Amanda Rocha", 28, "777.888.999-00", "CLT", "Manhã");
        Professor prof2 = new Professor(nextFuncionarioId++, "Bruno Almeida", 32, "888.999.000-11", "CLT", "Tarde");
        Professor prof3 = new Professor(nextFuncionarioId++, "Camila Nunes", 30, "999.000.111-22", "CLT", "Integral");

        Cuidador cuidador = new Cuidador(nextFuncionarioId++, "Daniel Souza", 25, "000.111.222-33", "CLT", "2-3 anos");

        Coordenador coord = new Coordenador(nextFuncionarioId++, "Elena Martins", 45, "111.222.333-44", "CLT", "Pedagógico");

        funcionarios.addAll(Arrays.asList(prof1, prof2, prof3, cuidador, coord));

        // Cadastrar turmas
        TurmaCreche turmaCreche = new TurmaCreche(nextTurmaId++, "Maternal", "Manhã", "13:00-15:00");
        TurmaInfantil turmaInfantil = new TurmaInfantil(nextTurmaId++, "Infantil I", "Tarde", "Segunda e Quarta");
        TurmaPre turmaPre = new TurmaPre(nextTurmaId++, "Pré-escola", "Integral", "Diárias");

        turmaCreche.setProfessor(prof1);
        turmaInfantil.setProfessor(prof2);
        turmaPre.setProfessor(prof3);

        // Adicionar alunos às turmas
        turmaCreche.adicionarAluno(aluno1);
        turmaCreche.adicionarAluno(aluno2);
        turmaInfantil.adicionarAluno(aluno3);
        turmaInfantil.adicionarAluno(aluno4);
        turmaPre.adicionarAluno(aluno5);
        turmaPre.adicionarAluno(aluno6);

        turmas.addAll(Arrays.asList(turmaCreche, turmaInfantil, turmaPre));

        // Cadastrar matrículas
        List<Responsavel> responsaveisMat1 = Arrays.asList(resp1, resp2);
        Matricula mat1 = new Matricula(nextMatriculaId++, LocalDate.now(), SituacaoMatricula.ATIVA,
                "Aluno com adaptação", "Rua A, 123", aluno1,
                responsaveisMat1, prof1, turmaCreche);

        List<Responsavel> responsaveisMat2 = Arrays.asList(resp3, resp4);
        Matricula mat2 = new Matricula(nextMatriculaId++, LocalDate.now(), SituacaoMatricula.ATIVA,
                "", "Rua B, 456", aluno3,
                responsaveisMat2, prof2, turmaInfantil);

        List<Responsavel> responsaveisMat3 = Arrays.asList(resp5, resp6);
        Matricula mat3 = new Matricula(nextMatriculaId++, LocalDate.now(), SituacaoMatricula.ATIVA,
                "Necessidade especial: TDAH", "Rua C, 789", aluno5,
                responsaveisMat3, prof3, turmaPre);

        matriculas.addAll(Arrays.asList(mat1, mat2, mat3));

        System.out.println("Dados iniciais cadastrados com sucesso!");
    }

    private static void cadastrarAluno() {
        System.out.println("\n--- Cadastro de Aluno ---");

        System.out.print("Nome: ");
        String nome = scanner.nextLine();

        System.out.print("Idade: ");
        int idade = Integer.parseInt(scanner.nextLine());

        System.out.print("CPF: ");
        String cpf = scanner.nextLine();

        System.out.print("Necessidade especial (deixe em branco se não houver): ");
        String necessidadeEspecial = scanner.nextLine();

        // Listar responsáveis para seleção
        System.out.println("Responsáveis disponíveis:");
        for (int i = 0; i < responsaveis.size(); i++) {
            System.out.println((i + 1) + ". " + responsaveis.get(i).getNome());
        }

        System.out.print("Selecione o número do responsável: ");
        int respIndex = Integer.parseInt(scanner.nextLine()) - 1;

        if (respIndex < 0 || respIndex >= responsaveis.size()) {
            System.out.println("Índice de responsável inválido!");
            return;
        }

        Responsavel responsavel = responsaveis.get(respIndex);

        Aluno aluno = new Aluno(nextAlunoId++, nome, idade, cpf, responsavel, necessidadeEspecial);
        alunos.add(aluno);

        System.out.println("Aluno cadastrado com sucesso! ID: " + aluno.getId());
    }

    private static void cadastrarFuncionario() {
        System.out.println("\n--- Cadastro de Funcionário ---");
        System.out.println("Tipos: 1. Professor, 2. Cuidador, 3. Coordenador");
        System.out.print("Selecione o tipo: ");
        int tipo = Integer.parseInt(scanner.nextLine());

        System.out.print("Nome: ");
        String nome = scanner.nextLine();

        System.out.print("Idade: ");
        int idade = Integer.parseInt(scanner.nextLine());

        System.out.print("CPF: ");
        String cpf = scanner.nextLine();

        System.out.print("Vínculo: ");
        String vinculo = scanner.nextLine();

        Funcionario funcionario = null;

        switch (tipo) {
            case 1: // Professor
                System.out.print("Turno: ");
                String turno = scanner.nextLine();
                funcionario = new Professor(nextFuncionarioId++, nome, idade, cpf, vinculo, turno);
                break;
            case 2: // Cuidador
                System.out.print("Faixa etária atendida: ");
                String faixaEtaria = scanner.nextLine();
                funcionario = new Cuidador(nextFuncionarioId++, nome, idade, cpf, vinculo, faixaEtaria);
                break;
            case 3: // Coordenador
                System.out.print("Setor responsável: ");
                String setor = scanner.nextLine();
                funcionario = new Coordenador(nextFuncionarioId++, nome, idade, cpf, vinculo, setor);
                break;
            default:
                System.out.println("Tipo inválido!");
                return;
        }

        funcionarios.add(funcionario);
        System.out.println("Funcionário cadastrado com sucesso! ID: " + funcionario.getId());
    }

    private static void cadastrarResponsavel() {
        System.out.println("\n--- Cadastro de Responsável ---");

        System.out.print("Nome: ");
        String nome = scanner.nextLine();

        System.out.print("Idade: ");
        int idade = Integer.parseInt(scanner.nextLine());

        System.out.print("CPF: ");
        String cpf = scanner.nextLine();

        System.out.print("Parentesco: ");
        String parentesco = scanner.nextLine();

        System.out.print("Telefone: ");
        String telefone = scanner.nextLine();

        Responsavel responsavel = new Responsavel(nextResponsavelId++, nome, idade, cpf, parentesco, telefone);
        responsaveis.add(responsavel);

        System.out.println("Responsável cadastrado com sucesso! ID: " + responsavel.getId());
    }

    private static void cadastrarTurma() {
        System.out.println("\n--- Cadastro de Turma ---");
        System.out.println("Tipos: 1. Creche (2-3 anos), 2. Infantil (4-5 anos), 3. Pré (6 anos)");
        System.out.print("Selecione o tipo: ");
        int tipo = Integer.parseInt(scanner.nextLine());

        System.out.print("Nome: ");
        String nome = scanner.nextLine();

        System.out.print("Turno: ");
        String turno = scanner.nextLine();

        Turma turma = null;

        switch (tipo) {
            case 1: // Creche
                System.out.print("Hora do cochilo: ");
                String horaCochilo = scanner.nextLine();
                turma = new TurmaCreche(nextTurmaId++, nome, turno, horaCochilo);
                break;
            case 2: // Infantil
                System.out.print("Aulas de psicomotricidade: ");
                String aulasPsicomotricidade = scanner.nextLine();
                turma = new TurmaInfantil(nextTurmaId++, nome, turno, aulasPsicomotricidade);
                break;
            case 3: // Pré
                System.out.print("Aulas de alfabetização: ");
                String aulasAlfabetizacao = scanner.nextLine();
                turma = new TurmaPre(nextTurmaId++, nome, turno, aulasAlfabetizacao);
                break;
            default:
                System.out.println("Tipo inválido!");
                return;
        }

        // Listar professores para seleção
        System.out.println("Professores disponíveis:");
        List<Professor> professores = new ArrayList<>();
        for (Funcionario f : funcionarios) {
            if (f instanceof Professor) {
                professores.add((Professor) f);
                System.out.println(professores.size() + ". " + f.getNome());
            }
        }

        if (professores.isEmpty()) {
            System.out.println("Nenhum professor cadastrado!");
        } else {
            System.out.print("Selecione o número do professor: ");
            int profIndex = Integer.parseInt(scanner.nextLine()) - 1;

            if (profIndex >= 0 && profIndex < professores.size()) {
                turma.setProfessor(professores.get(profIndex));
            }
        }

        turmas.add(turma);
        System.out.println("Turma cadastrada com sucesso! ID: " + turma.getId());
    }

    private static void cadastrarMatricula() {
        System.out.println("\n--- Cadastro de Matrícula ---");

        // Listar alunos para seleção
        System.out.println("Alunos disponíveis:");
        for (int i = 0; i < alunos.size(); i++) {
            System.out.println((i + 1) + ". " + alunos.get(i).getNome());
        }

        System.out.print("Selecione o número do aluno: ");
        int alunoIndex = Integer.parseInt(scanner.nextLine()) - 1;

        if (alunoIndex < 0 || alunoIndex >= alunos.size()) {
            System.out.println("Índice de aluno inválido!");
            return;
        }


        Aluno aluno = alunos.get(alunoIndex);

        // Listar responsáveis para seleção (múltiplos)
        System.out.println("Responsáveis disponíveis (selecione múltiplos separados por vírgula):");
        for (int i = 0; i < responsaveis.size(); i++) {
            System.out.println((i + 1) + ". " + responsaveis.get(i).getNome());
        }

        System.out.print("Selecione os números dos responsáveis: ");
        String[] respIndicesStr = scanner.nextLine().split(",");
        List<Responsavel> responsaveisMatricula = new ArrayList<>();

        for (String indexStr : respIndicesStr) {
            int respIndex = Integer.parseInt(indexStr.trim()) - 1;
            if (respIndex >= 0 && respIndex < responsaveis.size()) {
                responsaveisMatricula.add(responsaveis.get(respIndex));
            }
        }

        if (responsaveisMatricula.isEmpty()) {
            System.out.println("Nenhum responsável válido selecionado!");
            return;
        }

        // Listar funcionários para seleção
        System.out.println("Funcionários disponíveis:");
        for (int i = 0; i < funcionarios.size(); i++) {
            System.out.println((i + 1) + ". " + funcionarios.get(i).getNome());
        }

        System.out.print("Selecione o número do funcionário: ");
        int funcIndex = Integer.parseInt(scanner.nextLine()) - 1;

        if (funcIndex < 0 || funcIndex >= funcionarios.size()) {
            System.out.println("Índice de funcionário inválido!");
            return;
        }

        Funcionario funcionario = funcionarios.get(funcIndex);

        // Listar turmas para seleção

        System.out.println("Turmas disponíveis:");
        for (int i = 0; i < turmas.size(); i++) {
            System.out.println((i + 1) + ". " + turmas.get(i).getNome());
        }

        System.out.print("Selecione o número da turma: ");
        int turmaIndex = Integer.parseInt(scanner.nextLine()) - 1;

        if (turmaIndex < 0 || turmaIndex >= turmas.size()) {
            System.out.println("Índice de turma inválido!");
            return;
        }

        Turma turma = turmas.get(turmaIndex);

        // Verificar se a idade do aluno é compatível com a turma
        if (!turma.verificarIdadeAluno(aluno.getIdade())) {
            System.out.println("Idade do aluno não é compatível com a faixa etária da turma!");
            return;
        }

        System.out.print("Observações: ");
        String observacoes = scanner.nextLine();

        System.out.print("Endereço: ");
        String endereco = scanner.nextLine();

        // Listar situações disponíveis
        System.out.println("Situações disponíveis:");
        for (SituacaoMatricula situacao : SituacaoMatricula.values()) {
            System.out.println((situacao.ordinal() + 1) + ". " + situacao);
        }

        System.out.print("Selecione o número da situação: ");
        int situacaoIndex = Integer.parseInt(scanner.nextLine()) - 1;

        if (situacaoIndex < 0 || situacaoIndex >= SituacaoMatricula.values().length) {
            System.out.println("Índice de situação inválido!");
            return;
        }

        SituacaoMatricula situacao = SituacaoMatricula.values()[situacaoIndex];

        Matricula matricula = new Matricula(nextMatriculaId++, LocalDate.now(), situacao,
                observacoes, endereco, aluno,
                responsaveisMatricula, funcionario, turma, false);

        matriculas.add(matricula);

        // Adicionar aluno à turma
        if (turma.adicionarAluno(aluno)) {
            System.out.println("Matrícula cadastrada com sucesso! Número: " + matricula.getNumeroMatricula());
        } else {
            System.out.println("Matrícula cadastrada, mas aluno não pôde ser adicionado à turma devido à incompatibilidade de idade!");
        }
    }

    private static void adicionarIrmaos(){
        System.out.println("\n--- Associação de Irmãos ---");
        if(alunos.size() < 2){
            System.out.println("É necessário ter pelo menos dois alunos cadastrados!");
        }

        System.out.println("Lista de alunos:");
        for (int i = 0; i < alunos.size(); i++){
            System.out.println((i + 1)+ ". " + alunos.get(i).getNome());
        }
        System.out.println("Selecione o número do primeiro aluno: ");
        int index1 = Integer.parseInt(scanner.nextLine()) - 1;

        System.out.println("Selecione o número do segundo aluno (irmão):  ");
        int index2 = Integer.parseInt(scanner.nextLine()) - 1;

        if(index1 < 0 || index1 >= alunos.size() || index2 < 0 || index2 >= alunos.size()){
            System.out.println("Índice inválido!");
            return;
        }

        Aluno a1 = alunos.get(index1);
        Aluno a2 = alunos.get(index2);

        if(a1.equals(a2)){
            System.out.println("Um aluno não pode ser irmão de si mesmo! ");
            return;
        }

        a1.adicionarIrmao(a2);
        System.out.println("Irmãos vinculados com sucesso: "+ a1.getNome() + "<->" + a2.getNome());
    }

    private static void cadastrarPreMatricula(){
        System.out.println("\n--- Cadastro de PRÉ-MATRÍCULA ---");

        System.out.println("Alunos disponíveis: ");
        for (int i = 0; i < alunos.size(); i++){
            System.out.println((i+1)+ ". "+ alunos.get(i).getNome());
        }
        System.out.println("Selecione o número do aluno: ");
        int alunoIndex = Integer.parseInt(scanner.nextLine())-1;

        if (alunoIndex < 0 || alunoIndex >= alunos.size()){
            System.out.println("Índice inválido!");
            return;
        }
        Aluno aluno = alunos.get(alunoIndex);

        // Responsaveis (Multiplos)
        System.out.println("Selecione os responsáveis (separe por vírgula): ");
        for (int i =0; i < responsaveis.size(); i++){
            System.out.println((i+1)+". "+ responsaveis.get(i).getNome());
        }
        String[] respIndices = scanner.nextLine().split(",");
        List<Responsavel> respSelecionados = new ArrayList<>();

        for (String indexStr : respIndices){
            int idx = Integer.parseInt(indexStr.trim()) - 1;
            if(idx >= 0 && idx < responsaveis.size()){
                respSelecionados.add(responsaveis.get(idx));
            }
        }

        if (respSelecionados.isEmpty()){
            System.out.println("Nenhum responsável selecionado!");
            return;
        }
        System.out.println("Observações (motivo da pré-matricula, etc): ");
        String observacoes = scanner.nextLine();

        System.out.println("Endereço: ");
        String endereco = scanner.nextLine();

        Matricula preMatricula = new Matricula(nextMatriculaId++, LocalDate.now(), SituacaoMatricula.PENDENTE,
                observacoes, endereco, aluno, respSelecionados, null, null, true);

        matriculas.add(preMatricula);
        List<Matricula> preMatriculados = new ArrayList<>();
        preMatriculados.add(preMatricula);

        System.out.println("Pré-matrícula registrada com sucesso! Número: " + preMatricula.getNumeroMatricula());
    }

    private static void exibirAlunos() {
        System.out.println("\n--- Lista de Alunos ---");
        if (alunos.isEmpty()) {
            System.out.println("Nenhum aluno cadastrado.");
        } else {
            for (Aluno aluno : alunos) {
                System.out.println(aluno);
            }
        }
    }

    private static void exibirFuncionarios() {
        System.out.println("\n--- Lista de Funcionários ---");
        if (funcionarios.isEmpty()) {
            System.out.println("Nenhum funcionário cadastrado.");
        } else {
            for (Funcionario funcionario : funcionarios) {
                System.out.println(funcionario);
            }
        }
    }

    private static void exibirResponsaveis() {
        System.out.println("\n--- Lista de Responsáveis ---");
        if (responsaveis.isEmpty()) {
            System.out.println("Nenhum responsável cadastrado.");
        } else {
            for (Responsavel responsavel : responsaveis) {
                System.out.println(responsavel);
            }
        }
    }

    private static void exibirMatriculas() {
        System.out.println("\n--- Lista de Matrículas ---");
        if (matriculas.isEmpty()) {
            System.out.println("Nenhuma matrícula cadastrada.");
        } else {
            for (Matricula matricula : matriculas) {
                System.out.println(matricula);
            }
        }
    }

    private static void exibirIrmaos() {
        System.out.println("\n=== Relação de Irmãos ===");

        boolean encontrou = false;
        for (Aluno aluno : alunos){
            if(!aluno.getIrmaos().isEmpty()){
                encontrou = true;
                System.out.println("\n Aluno: " + aluno.getNome());
                System.out.println("   ↳ Irmãos: " +
                        aluno.getIrmaos().stream()
                                .map(Aluno::getNome).reduce((a,b) -> a + ", " + b)
                                .orElse(""));
            }
        }

        if(!encontrou){
            System.out.println("Nenhum vínculo de irmãos encontrado.");
        }

        System.out.println();

    }

    private static void exibirTurmas() {
        System.out.println("\n--- Lista de Turmas ---");
        if (turmas.isEmpty()) {
            System.out.println("Nenhuma turma cadastrada.");
        } else {
            for (Turma turma : turmas) {
                System.out.println(turma);
                System.out.println("  Alunos na turma:");
                for (Aluno aluno : turma.getAlunos()) {
                    System.out.println("  - " + aluno.getNome() + " (" + aluno.getIdade() + " anos)");
                }
                System.out.println();
            }
        }
    }
    private static void exibirPreMatriculas() {
        System.out.println("\n--- Lista de Pré-Matrículas ---");
        if (matriculas.isEmpty()) {
            System.out.println("Nenhuma matrícula cadastrada.");
        } else {
            boolean encontrou = false;
            for (Matricula matricula : matriculas) {
                if (matricula.isPreMatricula()) {
                    encontrou = true;
                    System.out.println(

                            "ID: " + matricula.getNumeroMatricula() +
                                    ", Nome: " + matricula.getAluno().getNome() +
                                    ", Idade: " + matricula.getAluno().getIdade() +
                                    ", CPF: " + matricula.getAluno().getCpf() +
                                    ", Responsável: " + matricula.getResponsaveis().stream()
                                                                        .map(Responsavel::getNome)
                                                                        .reduce((a, b) -> a + ", " + b)
                                                                       .orElse("Nenhum") +
                                    ", Necessidade Especial: " +(matricula.getAluno().getNecessidadeEspecial() != null ? matricula.getAluno().getNecessidadeEspecial() : "Não definido") +
                                    ", Turma: " +(matricula.getTurma() != null ? matricula.getTurma().getNome() : "Não atribuída") +
                                    ", Funcionário: " + (matricula.getFuncionario() != null ? matricula.getFuncionario().getNome() : "Não definido") +
                                    ", Endereço: " + matricula.getEndereco() +
                                    ", Irmãos: " + (matricula.getAluno().getIrmaos() != null ? matricula.getAluno().getIrmaos(): "Nenhum") +
                                    ", Observações: " + (matricula.getObservacoes().isEmpty() ? "Nenhuma" : matricula.getObservacoes())
                    );
                }
            }

            if (!encontrou) {
                System.out.println("Nenhuma pré-matrícula encontrada.");
            }
        }
    }



    private static void converterMatricula(){
        System.out.println("\n --- Converter PRÉ-MATRÍCULA em matrícula deinitiva ---");
        //Filtro de Pre-matriculas pendentes...
        List<Matricula> pendentes = new ArrayList<>();
        for (Matricula m : matriculas){
            if (m.isPreMatricula() && m.getSituacao() == SituacaoMatricula.PENDENTE){
                pendentes.add(m);
            }
        }

        if (pendentes.isEmpty()){
            System.out.println("Nenhuma pré-matrícula pendente encontrada.");
            return;
        }

        //Exiba as pre matriculas disponiveis :)
        System.out.println("Pré-matrículas disponíveis: ");
        for (int i = 0; i < pendentes.size(); i++) {
            System.out.println((i + 1)+ ". "+ pendentes.get(i));
        }

        System.out.println("Selecione o número da pré-matrícula: ");
        int index = Integer.parseInt(scanner.nextLine()) - 1;

        if (index < 0 || index >= pendentes.size()){
            System.out.println("Índice inválido!");
            return;
        }
        Matricula pre = pendentes.get(index);

        //selecionar funcionario responsaveel

        System.out.println("Funcionários disponíveis: ");
        for (int i = 0; i < funcionarios.size(); i++){
            System.out.println((i + 1)+ ". " + funcionarios.get(i).getNome());
        }
        System.out.println("Selecionar o número do funcionário: ");
        int funcIndex = Integer.parseInt(scanner.nextLine()) - 1;
        if (funcIndex < 0 || funcIndex >= funcionarios.size()){
            System.out.println("Índice inválido!");
        }
        Funcionario func = funcionarios.get(funcIndex);
        // Selelecionar Turma
        System.out.println("Turmas disponíveis:");
        for (int i = 0; i < turmas.size(); i++) {
            System.out.println((i + 1) + ". " + turmas.get(i).getNome());
        }
        System.out.print("Selecione o número da turma: ");
        int turmaIndex = Integer.parseInt(scanner.nextLine()) - 1;
        if (turmaIndex < 0 || turmaIndex >= turmas.size()) {
            System.out.println("Índice inválido!");
            return;
        }
        Turma turma = turmas.get(turmaIndex);

        //Atualizacao (de fato)

        pre.setPreMatricula(false);
        pre.setSituacao(SituacaoMatricula.ATIVA);
        pre.setFuncionario(func);
        pre.setTurma(turma);
        turma.adicionarAluno(pre.getAluno());

        System.out.println("Pré-matrícula convertida com sucesso!");
        System.out.println("Aluno " + pre.getAluno().getNome() + " agora está na turma " + turma.getNome());
    }

}