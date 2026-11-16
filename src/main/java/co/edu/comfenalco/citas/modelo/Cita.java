package co.edu.comfenalco.citas.modelo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDateTime;

@Entity
@Table(name = "citas",
        uniqueConstraints = @UniqueConstraint(name = "uk_profesional_fecha",
                columnNames = {"profesional", "fecha_hora"}))
public class Cita {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String profesional;

    @Column(name = "fecha_hora", nullable = false)
    private LocalDateTime fechaHora;

    @Column(nullable = false)
    private String pacienteId;

    protected Cita() {
    }

    public Cita(String profesional, LocalDateTime fechaHora, String pacienteId) {
        this.profesional = profesional;
        this.fechaHora = fechaHora;
        this.pacienteId = pacienteId;
    }

    public Long getId() { return id; }
    public String getProfesional() { return profesional; }
    public LocalDateTime getFechaHora() { return fechaHora; }
    public String getPacienteId() { return pacienteId; }
}
