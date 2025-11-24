package ma.ens.volley;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class EditEtudiant extends AppCompatActivity {

    private EditText nom, prenom;
    private Spinner ville;
    private RadioButton m, f;
    private Button btnUpdate;
    private RequestQueue requestQueue;
    private int id;

    private static final String updateUrl = "http://192.168.0.172/projetphp/ws/updateEtudiant.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_etudiant);

        nom = findViewById(R.id.nom);
        prenom = findViewById(R.id.prenom);
        ville = findViewById(R.id.ville);
        m = findViewById(R.id.m);
        f = findViewById(R.id.f);
        btnUpdate = findViewById(R.id.btnUpdate);

        requestQueue = Volley.newRequestQueue(this);

        // Spinner setup
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.villes, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ville.setAdapter(adapter);

        // Récupérer les données de l'étudiant
        Intent intent = getIntent();
        id = intent.getIntExtra("id", -1);
        nom.setText(intent.getStringExtra("nom"));
        prenom.setText(intent.getStringExtra("prenom"));

        // Sélection de la ville
        String villeValue = intent.getStringExtra("ville");
        for (int i = 0; i < ville.getAdapter().getCount(); i++) {
            if (ville.getAdapter().getItem(i).toString().equals(villeValue)) {
                ville.setSelection(i);
                break;
            }
        }

        // Sexe
        String sexe = intent.getStringExtra("sexe");
        if ("homme".equalsIgnoreCase(sexe)) m.setChecked(true);
        else f.setChecked(true);

        btnUpdate.setOnClickListener(v -> updateEtudiant());
    }

    private void updateEtudiant() {
        StringRequest request = new StringRequest(Request.Method.POST, updateUrl,
                response -> {
                    Toast.makeText(this, "Étudiant mis à jour", Toast.LENGTH_SHORT).show();
                    // Retour à ListeEtudiants
                    Intent intent = new Intent(this, ListeEtudiants.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                },
                error -> Toast.makeText(this, "Erreur mise à jour", Toast.LENGTH_SHORT).show()
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id", String.valueOf(id));
                params.put("nom", nom.getText().toString().trim());
                params.put("prenom", prenom.getText().toString().trim());
                params.put("ville", ville.getSelectedItem().toString());
                params.put("sexe", m.isChecked() ? "homme" : "femme");
                return params;
            }
        };
        requestQueue.add(request);
    }
}
