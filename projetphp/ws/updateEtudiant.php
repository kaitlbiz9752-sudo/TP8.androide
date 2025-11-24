<?php
include_once '../service/EtudiantService.php';

header('Content-Type: application/json');

if ($_SERVER["REQUEST_METHOD"] == "POST") {
    $id = $_POST['id'] ?? null;
    $nom = $_POST['nom'] ?? null;
    $prenom = $_POST['prenom'] ?? null;
    $ville = $_POST['ville'] ?? null;
    $sexe = $_POST['sexe'] ?? null;

    if ($id && $nom && $prenom && $ville && $sexe) {
        $es = new EtudiantService();
        $etudiant = $es->findById((int)$id);
        if ($etudiant) {
            $etudiant->setNom($nom);
            $etudiant->setPrenom($prenom);
            $etudiant->setVille($ville);
            $etudiant->setSexe($sexe);
            $es->update($etudiant);
            echo json_encode(["success" => true, "message" => "Étudiant mis à jour"]);
        } else {
            echo json_encode(["success" => false, "message" => "Étudiant introuvable"]);
        }
    } else {
        echo json_encode(["success" => false, "message" => "Données manquantes"]);
    }
} else {
    echo json_encode(["success" => false, "message" => "Méthode non autorisée"]);
}
?>
