package com.example.quizimages



import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.quizimages.databinding.ActivityMainBinding

// Une question = texte + image + 3 choix + indice de la bonne réponse + description
data class Question(
    val texte: String,
    val imageId: Int,                // ex : R.drawable.triangle
    val choix: Array<String>,        // exactement 3 choix
    val bonneReponse: Int,           // indice 0, 1 ou 2
    val description: String          // pour l'accessibilité
)

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var boutonsChoix: List<Button>

    // Les 5 questions statiques (la bonne réponse change de position)
    private val questions = arrayOf(
        Question("Combien de côtés possède cette forme ?", R.drawable.triangle,
            arrayOf("4", "3", "5"), 1, "Un triangle rouge"),
        Question("Quelle est cette forme ?", R.drawable.carre,
            arrayOf("Carré", "Cercle", "Triangle"), 0, "Un carré bleu"),
        Question("Cette forme a-t-elle des angles ?", R.drawable.cercle,
            arrayOf("Oui, quatre", "Non, aucun", "Oui, trois"), 1, "Un cercle vert"),
        Question("Combien de branches a cette étoile ?", R.drawable.etoile,
            arrayOf("6", "4", "5"), 2, "Une étoile jaune"),
        Question("Quelle forme est représentée ?", R.drawable.rectangle,
            arrayOf("Cercle", "Rectangle", "Étoile"), 1, "Un rectangle violet")
    )

    // État du jeu
    private var indexCourant = 0        // question affichée
    private var score = 0               // nombre de bonnes réponses
    private var reponsesDonnees = 0     // progression (bonnes ET mauvaises)
    private var aRepondu = false        // la question courante a-t-elle déjà reçu une réponse ?
    private var partieTerminee = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        boutonsChoix = listOf(binding.btnChoix1, binding.btnChoix2, binding.btnChoix3)

        // Chaque bouton transmet SON indice (0, 1 ou 2) à verifierReponse
        boutonsChoix.forEachIndexed { indice, bouton ->
            bouton.setOnClickListener { verifierReponse(indice) }
        }
        binding.btnSuivant.setOnClickListener { questionSuivante() }
        binding.btnRejouer.setOnClickListener { demarrerPartie() }

        demarrerPartie()
    }

    // Remet tout à zéro et affiche la première question
    private fun demarrerPartie() {
        indexCourant = 0
        score = 0
        reponsesDonnees = 0
        aRepondu = false
        partieTerminee = false
        binding.zoneQuiz.visibility = View.VISIBLE
        binding.layoutResultat.visibility = View.GONE
        afficherQuestion()
    }

    // Affiche la question courante, son image et ses 3 choix
    private fun afficherQuestion() {
        val q = questions[indexCourant]
        aRepondu = false

        binding.txtQuestion.text = q.texte
        binding.imgQuestion.setImageResource(q.imageId)
        binding.imgQuestion.contentDescription = q.description

        for (i in boutonsChoix.indices) {
            boutonsChoix[i].text = q.choix[i]
            boutonsChoix[i].isEnabled = true
        }

        binding.txtFeedback.visibility = View.GONE
        binding.btnSuivant.visibility = View.GONE
        actualiserBandeau()
    }

    // Vérifie le choix cliqué
    private fun verifierReponse(choix: Int) {
        if (aRepondu || partieTerminee) return   // empêche un 2e point
        aRepondu = true

        val q = questions[indexCourant]
        if (choix == q.bonneReponse) {
            score++
            binding.txtFeedback.text = "✔ Bonne réponse !"
            binding.txtFeedback.setTextColor(Color.parseColor("#2E7D32"))
        } else {
            binding.txtFeedback.text =
                "✘ Mauvaise réponse. La bonne réponse était : ${q.choix[q.bonneReponse]}"
            binding.txtFeedback.setTextColor(Color.parseColor("#C62828"))
        }
        reponsesDonnees++   // la progression avance dans les deux cas

        boutonsChoix.forEach { it.isEnabled = false }
        binding.txtFeedback.visibility = View.VISIBLE

        // Dernière question ? Le texte du bouton change
        binding.btnSuivant.text =
            if (indexCourant == questions.size - 1) "Voir le résultat" else "Question suivante"
        binding.btnSuivant.visibility = View.VISIBLE
        actualiserBandeau()
    }

    // Met à jour score, progression et numéro de question
    private fun actualiserBandeau() {
        binding.txtScore.text = "Score : $score / ${questions.size}"
        binding.progressQuiz.max = questions.size
        binding.progressQuiz.progress = reponsesDonnees
        binding.txtNumero.text = "Question ${indexCourant + 1} sur ${questions.size}"
    }

    private fun questionSuivante() {
        if (!aRepondu) return
        if (indexCourant == questions.size - 1) {
            afficherResultat()
        } else {
            indexCourant++
            afficherQuestion()
        }
    }

    private fun afficherResultat() {
        partieTerminee = true
        binding.zoneQuiz.visibility = View.GONE
        binding.layoutResultat.visibility = View.VISIBLE
        binding.txtResultat.text = "Score final : $score / ${questions.size}"
    }
}