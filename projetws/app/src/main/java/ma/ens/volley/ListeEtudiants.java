package ma.ens.volley;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import ma.ens.volley.Adapter.EtudiantAdapter;
import ma.ens.volley.classes.Etudiant;

public class ListeEtudiants extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EtudiantAdapter adapter;
    private Button btnAdd;
    private RequestQueue requestQueue;
    private List<Etudiant> etudiants;

    private static final String loadUrl = "http://192.168.0.172/projetphp/ws/loadEtudiant.php";

    private ActivityResultLauncher<Intent> etudiantLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liste_etudiants);

        recyclerView = findViewById(R.id.recyclerViewEtudiants);
        btnAdd = findViewById(R.id.btnAdd);
        requestQueue = Volley.newRequestQueue(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        etudiants = new ArrayList<>();

        // Adapter initial avec liste vide
        adapter = new EtudiantAdapter(etudiants, this, requestQueue);
        recyclerView.setAdapter(adapter);

        // 🔹 Ajouter un étudiant
        btnAdd.setOnClickListener(v -> {
            Intent intent = new Intent(ListeEtudiants.this, AddEtudiant.class);
            etudiantLauncher.launch(intent);
        });

        // 🔹 Launcher pour recevoir le résultat de Add/Edit
        etudiantLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        // Rafraîchir la liste depuis la base de données
                        loadEtudiants();
                    }
                }
        );

        // Charger la liste initiale
        loadEtudiants();
    }

    private void loadEtudiants() {
        StringRequest request = new StringRequest(Request.Method.GET, loadUrl,
                response -> {
                    try {
                        Type type = new TypeToken<List<Etudiant>>() {}.getType();
                        List<Etudiant> list = new Gson().fromJson(response, type);

                        etudiants.clear();
                        etudiants.addAll(list);

                        adapter.notifyDataSetChanged();
                    } catch (Exception e) {
                        Toast.makeText(this, "Erreur parsing JSON", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                },
                error -> {
                    Toast.makeText(this, "Erreur réseau : " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("VOLLEY", "Erreur : " + error.getMessage());
                });

        requestQueue.add(request);
    }
}
