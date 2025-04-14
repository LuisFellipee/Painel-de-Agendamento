import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

class Tutor {
    @Override
    public String toString() {
        return nome;
    }
    private String nome, telefone, email, endereco;
    private List<Animal> animais;

    public Tutor(String nome, String telefone, String email, String endereco) {
        this.nome = nome;
        this.telefone = telefone;
        this.email = email;
        this.endereco = endereco;
        this.animais = new ArrayList<>();
    }

    public void adicionarAnimal(Animal animal) {
        animais.add(animal);
    }

    public String getNome() { return nome; }
    public String getTelefone() { return telefone; }
    public String getEmail() { return email; }
    public String getEndereco() { return endereco; }
    public List<Animal> getAnimais() { return animais; }
}

class Animal {
    @Override
    public String toString() {
        return nome;
    }

    private String nome, especie, historicoMedico;
    private int idade;
    private double peso;

    public Animal(String nome, String especie, int idade, double peso, String historicoMedico) {
        this.nome = nome;
        this.especie = especie;
        this.idade = idade;
        this.peso = peso;
        this.historicoMedico = historicoMedico;
    }

    public String getNome() { return nome; }
    public String getEspecie() { return especie; }
    public int getIdade() { return idade; }
    public double getPeso() { return peso; }
    public String getHistoricoMedico() { return historicoMedico; }
}

class HorarioDisp{
    private LocalTime horario;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    public HorarioDisp(LocalTime horario) {
        this.horario = horario;
    }

    public static List<HorarioDisp> gerarHorariosDisponiveis() {
        List<HorarioDisp> horarios = new ArrayList<>();
        LocalTime inicio = LocalTime.of(8, 0);
        LocalTime fim = LocalTime.of(18, 0);

        LocalTime horarioAtual = inicio;
        while (!horarioAtual.isAfter(fim)) {
            horarios.add(new HorarioDisp(horarioAtual));
            horarioAtual = horarioAtual.plusMinutes(30);
        }

        return horarios;
    }
    @Override
    public String toString() {
        return horario.format(FORMATTER);
    }

    // Getters e setters
    public LocalTime getHorario() {
        return horario;
    }

    public void setHorario(LocalTime horario) {
        this.horario = horario;
    }

}

// Filtros
class numericFilter extends DocumentFilter {
    @Override
    public void insertString(FilterBypass fb, int offset, @NotNull String string, AttributeSet attr) throws BadLocationException {
        if (string.matches("[0-9.]+")) {
            super.insertString(fb, offset, string, attr);
        }
    }

    public void replace() throws BadLocationException {
        replace(null, 0, 0, null, null);
    }

    @Override
    public void replace(FilterBypass fb, int offset, int length, @NotNull String text, AttributeSet attrs) throws BadLocationException {
        if (text.matches("[0-9.]+")) {
            super.replace(fb, offset, length, text, attrs);
        }
    }
}

class LettersOnlyFilter extends DocumentFilter {
    @Override
    public void insertString(FilterBypass fb, int offset, @NotNull String string, AttributeSet attr) throws BadLocationException {
        if (string.matches("[a-zA-ZáàâãéèêíïóôõöúçñÁÀÂÃÉÈÍÓÔÕÚÇÑ ]+")) {
            super.insertString(fb, offset, string, attr);
        }
    }
    @Override
    public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
        if (text.matches("[a-zA-ZáàâãéèêíïóôõöúçñÁÀÂÃÉÈÍÓÔÕÚÇÑ ]+")) {
            super.replace(fb, offset, length, text, attrs);
        }
    }
}

class TelaCadastro extends JFrame {
    private JTextField nomeTutorField, emailField, enderecoField;
    private JFormattedTextField telefoneField;
    private JTextField nomeAnimalField, especieField, idadeField, pesoField, historicoField;
    private JTextArea resultadoArea;
    private List<Tutor> tutores;

    public TelaCadastro() {
        setTitle("Cadastro de Tutor e Animal");
        setSize(500, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(13, 2));
        tutores = new ArrayList<>();

        // Campos
        add(new JLabel("Nome do Tutor:"));
        nomeTutorField = new JTextField();
        add(nomeTutorField);

        add(new JLabel("Telefone:"));
        try {
            MaskFormatter mask = new MaskFormatter("(##) #####-####");
            telefoneField = new JFormattedTextField(mask);
        } catch (Exception e) {
            telefoneField = new JFormattedTextField();
        }
        add(telefoneField);

        add(new JLabel("E-mail:"));
        emailField = new JTextField();
        add(emailField);

        add(new JLabel("Endereço:"));
        enderecoField = new JTextField();
        add(enderecoField);

        add(new JLabel("Nome do Animal:"));
        nomeAnimalField = new JTextField();
        add(nomeAnimalField);

        add(new JLabel("Espécie:"));
        especieField = new JTextField();
        add(especieField);

        add(new JLabel("Idade:"));
        idadeField = new JTextField();
        add(idadeField);

        add(new JLabel("Peso (kg):"));
        pesoField = new JTextField();
        add(pesoField);

        add(new JLabel("Histórico Médico:"));
        historicoField = new JTextField();
        add(historicoField);

        JButton cadastrarButton = new JButton("Cadastrar");
        add(cadastrarButton);

        resultadoArea = new JTextArea();
        resultadoArea.setEditable(false);
        add(new JScrollPane(resultadoArea));

        // Filtros
        ((AbstractDocument) nomeTutorField.getDocument()).setDocumentFilter(new LettersOnlyFilter());
        ((AbstractDocument) nomeAnimalField.getDocument()).setDocumentFilter(new LettersOnlyFilter());
        ((AbstractDocument) idadeField.getDocument()).setDocumentFilter(new numericFilter());
        ((AbstractDocument) pesoField.getDocument()).setDocumentFilter(new numericFilter());

        cadastrarButton.addActionListener(e -> cadastrarTutorEAnimal());
        JButton agendarButton = new JButton("Agendamento de Consultas");
        agendarButton.addActionListener(e -> new TelaAgendamento(tutores));
        add(agendarButton);
        setVisible(true);
    }

    private void cadastrarTutorEAnimal() {
        String nomeTutor = nomeTutorField.getText().trim();
        String telefone = telefoneField.getText().trim();
        String email = emailField.getText().trim();
        String endereco = enderecoField.getText().trim();
        String nomeAnimal = nomeAnimalField.getText().trim();
        String especie = especieField.getText().trim();
        String historico = historicoField.getText().trim();

        // Validação de obrigatoriedade
        if (nomeTutor.isEmpty() || telefone.isEmpty() || email.isEmpty() || endereco.isEmpty()
                || nomeAnimal.isEmpty() || especie.isEmpty() || idadeField.getText().trim().isEmpty()
                || pesoField.getText().trim().isEmpty() || historico.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos os campos devem ser preenchidos.");
            return;
        }

        if (!email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$")) {
            JOptionPane.showMessageDialog(this, "E-mail inválido!");
            return;
        }

        int idade;
        double peso;
        try {
            idade = Integer.parseInt(idadeField.getText().trim());
            peso = Double.parseDouble(pesoField.getText().trim());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Idade e peso devem ser numéricos!");
            return;
        }

        Tutor tutor = new Tutor(nomeTutor, telefone, email, endereco);
        Animal animal = new Animal(nomeAnimal, especie, idade, peso, historico);
        tutor.adicionarAnimal(animal);
        tutores.add(tutor);

        exibirTutores();
        limparCampos();
    }

    private void exibirTutores() {
        StringBuilder sb = new StringBuilder();
        for (Tutor tutor : tutores) {
            sb.append("Tutor: ").append(tutor.getNome()).append("\n")
                    .append("Telefone: ").append(tutor.getTelefone()).append("\n")
                    .append("E-mail: ").append(tutor.getEmail()).append("\n")
                    .append("Endereço: ").append(tutor.getEndereco()).append("\n")
                    .append("Animais:\n");
            for (Animal animal : tutor.getAnimais()) {
                sb.append(" - ").append(animal.getNome())
                        .append(" (").append(animal.getEspecie()).append(")\n");
            }
            sb.append("\n");
        }
        resultadoArea.setText(sb.toString());
    }

    private void limparCampos() {
        nomeTutorField.setText("");
        telefoneField.setText("");
        emailField.setText("");
        enderecoField.setText("");
        nomeAnimalField.setText("");
        especieField.setText("");
        idadeField.setText("");
        pesoField.setText("");
        historicoField.setText("");
    }


}
