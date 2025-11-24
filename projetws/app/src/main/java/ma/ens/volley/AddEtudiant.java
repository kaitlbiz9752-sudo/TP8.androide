package ma.ens.volley;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ma.ens.volley.classes.Etudiant;

public class AddEtudiant extends AppCompatActivity implements View.OnClickListener {

    private EditText nom, prenom;
    private Spinner ville;
    private RadioButton m, f;
    private Button add;
    private RequestQueue requestQueue;

    private static final String insertUrl = "http://192.168.0.172/projetphp/ws/createEtudiant.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_etudiant);

        nom = findViewById(R.id.nom);
        prenom = findViewById(R.id.prenom);
        ville = findViewById(R.id.ville);
        m = findViewById(R.id.m);
        f = findViewById(R.id.f);
        add = findViewById(R.id.add);

        requestQueue = Volley.newRequestQueue(this);
        add.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.add) {
            envoyerEtudiant();
        }
    }

    private void envoyerEtudiant() {
        StringRequest request = new StringRequest(Request.Method.POST, insertUrl,
                response -> {
                    Toast.makeText(this, "Etudiant ajouté avec succès", Toast.LENGTH_SHORT).show();
                    Log.d("RESPONSE", response);

                    // Retourner OK à ListeEtudiants pour recharger la liste
                    setResult(RESULT_OK);
                    finish(); // Ferme AddEtudiant
                },
                error -> {
                    Toast.makeText(this, "Erreur : " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("VOLLEY", "Erreur : " + error.getMessage());
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                String sexe = m.isChecked() ? "homme" : "femme";
                Map<String, String> params = new HashMap<>();
                params.put("nom", nom.getText().toString().trim());
                params.put("prenom", prenom.getText().toString().trim());
                params.put("ville", ville.getSelectedItem().toString());
                params.put("sexe", sexe);
                return params;
            }
        };

        requestQueue.add(request);
    }




}
