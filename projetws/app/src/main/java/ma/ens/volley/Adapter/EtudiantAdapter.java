package ma.ens.volley.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;
import android.graphics.Color; // (plus vraiment utilisé, tu peux le supprimer)
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ma.ens.volley.EditEtudiant;
import ma.ens.volley.R;
import ma.ens.volley.classes.Etudiant;

public class EtudiantAdapter extends RecyclerView.Adapter<EtudiantAdapter.ViewHolder> {

    private List<Etudiant> etudiants;
    private Context context;
    private RequestQueue requestQueue;

    public EtudiantAdapter(List<Etudiant> etudiants, Context context, RequestQueue requestQueue) {
        this.etudiants = etudiants;
        this.context = context;
        this.requestQueue = requestQueue;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_etudiant, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Etudiant e = etudiants.get(position);

        // Affichage des informations
        holder.tvNomPrenom.setText(e.getNom() + " " + e.getPrenom());
        holder.tvVille.setText("Ville : " + e.getVille());
        holder.tvSexe.setText("Sexe : " + e.getSexe());

        // Clic sur tout l’item
        holder.itemView.setOnClickListener(v -> {
            // Popup avec deux choix : Modifier / Supprimer
            CharSequence[] options = {"Modifier", "Supprimer"};

            new AlertDialog.Builder(context)
                    .setTitle("Choisir une action")
                    .setItems(options, (dialog, which) -> {

                        int currentPos = holder.getAdapterPosition();
                        if (currentPos == RecyclerView.NO_POSITION) return;

                        Etudiant etu = etudiants.get(currentPos);

                        if (which == 0) { // Modifier
                            // Tu peux garder ou enlever la confirmation
                            new AlertDialog.Builder(context)
                                    .setTitle("Confirmation")
                                    .setMessage("Voulez-vous modifier cet étudiant ?")
                                    .setPositiveButton("Oui", (d, w) -> {
                                        Intent intent = new Intent(context, EditEtudiant.class);
                                        intent.putExtra("id", etu.getId());
                                        intent.putExtra("nom", etu.getNom());
                                        intent.putExtra("prenom", etu.getPrenom());
                                        intent.putExtra("ville", etu.getVille());
                                        intent.putExtra("sexe", etu.getSexe());
                                        context.startActivity(intent);
                                    })
                                    .setNegativeButton("Non", (d, w) -> d.dismiss())
                                    .create()
                                    .show();

                        } else if (which == 1) { // Supprimer
                            new AlertDialog.Builder(context)
                                    .setTitle("Confirmation")
                                    .setMessage("Voulez-vous vraiment supprimer cet étudiant ?")
                                    .setPositiveButton("Oui", (d, w) -> {
                                        String url = "http://192.168.0.172/projetphp/ws/deleteEtudiant.php";
                                        StringRequest deleteRequest = new StringRequest(
                                                Request.Method.POST,
                                                url,
                                                response -> {
                                                    Toast.makeText(context, "Étudiant supprimé", Toast.LENGTH_SHORT).show();
                                                    etudiants.remove(currentPos);
                                                    notifyItemRemoved(currentPos);
                                                    notifyItemRangeChanged(currentPos, etudiants.size());
                                                },
                                                error -> Toast.makeText(context, "Erreur suppression", Toast.LENGTH_SHORT).show()
                                        ) {
                                            @Override
                                            protected Map<String, String> getParams() {
                                                Map<String, String> params = new HashMap<>();
                                                params.put("id", String.valueOf(etu.getId()));
                                                return params;
                                            }
                                        };
                                        requestQueue.add(deleteRequest);
                                    })
                                    .setNegativeButton("Non", (d, w) -> d.dismiss())
                                    .create()
                                    .show();
                        }
                    })
                    .show();
        });
    }

    @Override
    public int getItemCount() {
        return etudiants.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNomPrenom, tvVille, tvSexe;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNomPrenom = itemView.findViewById(R.id.tvNomPrenom);
            tvVille = itemView.findViewById(R.id.tvVille);
            tvSexe = itemView.findViewById(R.id.tvSexe);
            // Plus de btnDelete / btnEdit ici
        }
    }
}