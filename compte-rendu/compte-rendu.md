# Compte rendu : TP Android « Quiz en images »

**Étudiant :** Ayoub BSK
**Thème :** formes géométriques (triangle, carré, cercle, étoile, rectangle)
**Dépôt :** https://github.com/AYOUBSK7/quiz-images-android

## 1. Interface et widgets

L'interface est écrite en XML (`activity_main.xml`) et utilisée par une seule activité, `MainActivity`.

- `TextView` : titre, score (en haut à droite), numéro de question, texte de la question, message de correction, résultat final
- `ProgressBar` horizontale : nombre de questions répondues
- `ImageView` : image de la question
- 3 `Button` : les choix de réponse
- `Button` : « Question suivante » / « Voir le résultat » et « Rejouer »
- `ScrollView` : pour que tout reste accessible sur un petit écran (le bandeau du score reste fixe en haut)

## 2. Tableau des questions

Chaque question est une `data class Question` contenant : le texte, l'identifiant de l'image (`R.drawable.xxx`), un tableau de 3 choix, l'indice de la bonne réponse (0, 1 ou 2) et une description de l'image pour l'accessibilité.

Les 5 questions sont déclarées dans un tableau `arrayOf(...)`. Chaque image (fichier XML vectoriel dans `res/drawable`) est associée à sa question par son identifiant. La bonne réponse change de position d'une question à l'autre.

## 3. Événements, score et progression

- **Clic sur un choix :** chaque bouton envoie son propre indice (0, 1 ou 2) à la fonction `verifierReponse`, qui le compare à la bonne réponse.
- **Score :** il augmente de 1 uniquement si la réponse est juste.
- **Progression :** la variable `reponsesDonnees` augmente à chaque réponse, juste ou fausse. La barre avance donc même si la réponse est fausse.
- **Un seul point par question :** la variable `aRepondu` bloque les clics suivants et les trois boutons sont désactivés après la réponse.
- **Correction :** un message avec texte et symbole (✔ ou ✘) en plus de la couleur ; en cas d'erreur, la bonne réponse est affichée.
- **Dernière question :** on teste `indexCourant == questions.size - 1`. Le bouton affiche alors « Voir le résultat » et le score final apparaît dans la même activité. « Rejouer » remet tout à zéro.

## 4. Tests réalisés

| Test | Résultat |
|---|---|
| Nouvelle partie : 1re question, 3 choix, score 0/5, progression 0/5 | OK |
| Bonne réponse : score +1, progression +1 | OK |
| Mauvaise réponse : score inchangé, progression +1 | OK |
| Plusieurs clics sur un choix : aucun point en plus, boutons désactivés | OK |
| Question suivante : image, texte et choix mis à jour, score conservé | OK |
| 5 bonnes réponses : score 5/5, progression 5/5 | OK |
| 5 mauvaises réponses : score 0/5, progression 5/5 | OK |
| Réponses mélangées : score égal au nombre de bonnes réponses | OK |
| Rejouer : retour à la question 1, score et progression à zéro | OK |
| Petit écran : tous les choix accessibles | OK |

## 5. Difficultés rencontrées

- Erreur Gradle `Unresolved reference buildFeatures` : le bloc `viewBinding` était en dehors du bloc `android { }`. Je l'ai déplacé à l'intérieur.
- Erreur Git `src refspec main does not match any` : ma branche s'appelait `master`. Je l'ai renommée avec `git branch -M main`.

## 6. Captures d'écran

Elles sont dans le dossier `captures/` : une question, une bonne réponse, une mauvaise réponse, l'écran final.
