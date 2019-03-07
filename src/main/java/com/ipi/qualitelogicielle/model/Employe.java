package com.ipi.qualitelogicielle.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.DayOfWeek;
import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Employe {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String nom;

    private String prenom;

    private String matricule;

    private LocalDate dateEmbauche;

    @Builder.Default
    private Double salaire = Entreprise.SALAIRE_BASE;

    @Builder.Default
    private Integer performance = Entreprise.PERFORMANCE_BASE;

    @Builder.Default
    private Double tempsPartiel = 1.0;

    public Employe(String nom, String prenom, String matricule, LocalDate dateEmbauche, Double salaire, Integer performance, Double tempsPartiel) {
        this.nom = nom;
        this.prenom = prenom;
        this.matricule = matricule;
        this.dateEmbauche = dateEmbauche;
        this.salaire = salaire;
        this.performance = performance;
        this.tempsPartiel = tempsPartiel;
    }

    public int getNombreAnneeAnciennete(LocalDate now) {
        if (dateEmbauche == null) {
            return 0;
        }
        int nombreAnnee = now.getYear() - dateEmbauche.getYear();
        return nombreAnnee >= 0
                ? nombreAnnee
                : 0;
    }

    public int getNbConges(LocalDate now) {
        return Entreprise.NB_CONGES_BASE + this.getNombreAnneeAnciennete(now);
    }

    public int getNbRtt() {
        return getNbRtt(LocalDate.now());
    }

    private int getNbRtt(LocalDate d) {
        int i1 = d.isLeapYear() ? 365 : 366;
        int var = 104;
        switch (LocalDate.of(d.getYear(), 1, 1).getDayOfWeek()) {
            case THURSDAY:
                if (d.isLeapYear()) var = var + 1;
                break;
            case FRIDAY:
                if (d.isLeapYear()) var = var + 2;
                else var = var + 1;
                break;
            case SATURDAY:
                var = var + 1;
                break;
            default:
                break;
        }
        int monInt = (int) Entreprise.joursFeries(d).stream()
                .filter(localDate -> localDate.getDayOfWeek().getValue() <= DayOfWeek.FRIDAY.getValue())
                .count();
        return (int) Math.ceil((i1 - Entreprise.NB_JOURS_MAX_FORFAIT - var - Entreprise.NB_CONGES_BASE - monInt) * tempsPartiel);
    }

    /**
     * Calcul de la prime annuelle selon la règle :
     * - Pour les managers : Prime annuelle de base bonnifiée par l'indice prime manager
     * - Pour les autres employés, la prime de base plus éventuellement la prime de performance calculée si l'employé
     * n'a pas la performance de base, en multipliant la prime de base par un l'indice de performance
     * (égal à la performance à laquelle on ajoute l'indice de prime de base)
     * <p>
     * - Pour tous les employés, une prime supplémentaire d'ancienneté est ajoutée en multipliant le nombre d'année
     * d'ancienneté avec la prime d'ancienneté. La prime est calculée au pro rata du temps de travail de l'employé
     *
     * @return la prime annuelle de l'employé en Euros et cents
     */
    public Double getPrimeAnnuelle(LocalDate now) {
        double prime = Entreprise.primeAnnuelleBase();

        prime *= getPrimeMultiplicator();
        prime += getPrimeAnciennete(now);
        prime *= this.tempsPartiel;

        return prime;
    }

    private Double getPrimeMultiplicator() {
        if (this.isManager()) {
            return Entreprise.INDICE_PRIME_MANAGER;
        }
        else if (this.isMorePerformant()) {
            return this.performance + Entreprise.INDICE_PRIME_BASE;
        }
        return 1D;
    }

    public boolean isManager() {
        return this.matricule != null
                && matricule.startsWith("M");
    }

    private double getPrimeAnciennete(LocalDate now) {
        return Entreprise.PRIME_ANCIENNETE * this.getNombreAnneeAnciennete(now);
    }

    private boolean isMorePerformant() {
        return !(this.performance == null
                || Entreprise.PERFORMANCE_BASE.equals(this.performance)
        );
    }

    // Augmenter salaire
    public void augmenterSalaire(double pourcentage) {
        throw new UnsupportedOperationException();
    }
}
