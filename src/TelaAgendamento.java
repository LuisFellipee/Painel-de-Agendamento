import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TelaAgendamento extends JFrame {
    private JComboBox<Tutor> tutorCombo;
    private JComboBox<Animal> animalCombo;
    private JTextField dataHoraField;
    private JComboBox<HorarioDisp> horarioCombo;
    private JTextField motivoField;
    private JTextArea areaConsultas;
    private List<Consulta> consultas;

    public TelaAgendamento(List<Tutor> tutores) {
        setTitle("Agendamento de Consultas");
        setSize(500, 400);
        setLayout(new GridLayout(6, 2));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        consultas = new ArrayList<>();

        add(new JLabel("Tutor:"));
        tutorCombo = new JComboBox<>(tutores.toArray(new Tutor[0]));
        add(tutorCombo);

        add(new JLabel("Animal:"));
        animalCombo = new JComboBox<>();
        atualizarAnimais();
        add(animalCombo);

        tutorCombo.addActionListener(e -> atualizarAnimais());

        add(new JLabel("Horário:"));
        horarioCombo = new JComboBox<>(HorarioDisp.gerarHorariosDisponiveis().toArray(new HorarioDisp[0]));
        add(horarioCombo);

        add(new JLabel("Motivo:"));
        motivoField = new JTextField();
        add(motivoField);

        JButton agendarButton = new JButton("Agendar");
        JButton cancelarButton = new JButton("Cancelar Última");
        add(agendarButton);
        add(cancelarButton);

        areaConsultas = new JTextArea();
        areaConsultas.setEditable(false);
        add(new JScrollPane(areaConsultas));

        agendarButton.addActionListener(e -> agendarConsulta());
        cancelarButton.addActionListener(e -> cancelarUltimaConsulta());

        setVisible(true);
    }

    private void atualizarAnimais() {
        Tutor tutor = (Tutor) tutorCombo.getSelectedItem();
        if (tutor != null) {
            animalCombo.removeAllItems();
            for (Animal animal : tutor.getAnimais()) {
                animalCombo.addItem(animal);
            }
        }
    }

    private void agendarConsulta() {
        try {
            Tutor tutor = (Tutor) tutorCombo.getSelectedItem();
            Animal animal = (Animal) animalCombo.getSelectedItem();
            HorarioDisp horarioSelecionado = (HorarioDisp) horarioCombo.getSelectedItem();
            LocalDateTime dataHora = LocalDateTime.of(LocalDate.now(), horarioSelecionado.getHorario());
            String motivo = motivoField.getText();

            Consulta consulta = new Consulta(tutor, animal, dataHora, motivo);
            consultas.add(consulta);
            atualizarAreaConsultas();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao agendar consulta. Verifique os dados.");
        }
    }

    private void cancelarUltimaConsulta() {
        if (!consultas.isEmpty()) {
            consultas.remove(consultas.size() - 1);
            atualizarAreaConsultas();
        }
    }

    private void atualizarAreaConsultas() {
        StringBuilder sb = new StringBuilder();
        for (Consulta consulta : consultas) {
            sb.append(consulta).append("\n\n");
        }
        areaConsultas.setText(sb.toString());
    }
}
