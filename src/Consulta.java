import java.time.LocalDateTime;

public class Consulta {
    private Tutor tutor;
    private Animal animal;
    private LocalDateTime dataHora;
    private String motivo;

    public Consulta(Tutor tutor, Animal animal, LocalDateTime dataHora, String motivo) {
        this.tutor = tutor;
        this.animal = animal;
        this.dataHora = dataHora;
        this.motivo = motivo;
    }

    public Tutor getTutor() {
        return tutor;
    }

    public Animal getAnimal() {
        return animal;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public String getMotivo() {
        return motivo;
    }

    @Override
    public String toString() {
        return String.format("Consulta de %s (%s) em %s\nMotivo: %s",
                animal.getNome(), tutor.getNome(), dataHora.toString(), motivo);
    }
}
