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

    private Double salaire = Entreprise.SALAIRE_BASE;

    private Integer performance = Entreprise.PERFORMANCE_BASE;

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
        }
        int monInt = (int) Entreprise.joursFeries(d).stream()
                .filter(localDate -> localDate.getDayOfWeek().getValue() <= DayOfWeek.FRIDAY.getValue())
                .count();
        return (int) Math.ceil((i1 - Entreprise.NB_JOURS_MAX_FORFAIT - var - Entreprise.NB_CONGES_BASE - monInt) * tempsPartiel);
    }

    /**
     * Calcul de la prime annuelle selon la règle :
     * Pour les managers : Prime annuelle de base bonnifiée par l'indice prime manager
     * Pour les autres employés, la prime de base plus éventuellement la prime de performance calculée si l'employé
     * n'a pas la performance de base, en multipliant la prime de base par un l'indice de performance
     * (égal à la performance à laquelle on ajoute l'indice de prime de base)
     * <p>
     * Pour tous les employés, une prime supplémentaire d'ancienneté est ajoutée en multipliant le nombre d'année
     * d'ancienneté avec la prime d'ancienneté. La prime est calculée au pro rata du temps de travail de l'employé
     *
     * @return la prime annuelle de l'employé en Euros et cents
     */
    public Double getPrimeAnnuelle() {
        //Calcule de la prime d'ancienneté
        Double primeAnciennete = Entreprise.PRIME_ANCIENNETE * this.getNombreAnneeAnciennete(LocalDate.now());
        Double prime;
        //Prime du manager (matricule commençant par M) : Prime annuelle de base multipliée par l'indice prime manager
        //plus la prime d'anciennté.
        if (matricule != null && matricule.startsWith("M")) {
            prime = Entreprise.primeAnnuelleBase() * Entreprise.INDICE_PRIME_MANAGER + primeAnciennete;
        }
        //Pour les autres employés en performance de base, uniquement la prime annuelle plus la prime d'ancienneté.
        else if (this.performance == null || Entreprise.PERFORMANCE_BASE.equals(this.performance)) {
            prime = Entreprise.primeAnnuelleBase() + primeAnciennete;
        }
        //Pour les employés plus performance, on bonnifie la prime de base en multipliant par la performance de l'employé
        // et l'indice de prime de base.
        else {
            prime = Entreprise.primeAnnuelleBase() * (this.performance + Entreprise.INDICE_PRIME_BASE) + primeAnciennete;
        }
        //Au pro rata du temps partiel.
        return prime * this.tempsPartiel;
    }

    // Augmenter salaire
    // TODO: public void augmenterSalaire(double pourcentage){}
}
