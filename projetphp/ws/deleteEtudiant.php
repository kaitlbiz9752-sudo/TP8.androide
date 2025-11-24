<?php
include_once '../service/EtudiantService.php';

header('Content-Type: application/json');

if ($_SERVER["REQUEST_METHOD"] == "POST") {
    $id = $_POST['id'] ?? null;

    if ($id) {
        $es = new EtudiantService();
        $etudiant = $es->findById((int)$id);
        if ($etudiant) {
            $es->delete($etudiant);
            echo json_encode(["success" => true, "message" => "Étudiant supprimé"]);
        } else {
            echo json_encode(["success" => false, "message" => "Étudiant introuvable"]);
        }
    } else {
        echo json_encode(["success" => false, "message" => "ID manquant"]);
    }
} else {
    echo json_encode(["success" => false, "message" => "Méthode non autorisée"]);
}
?>
