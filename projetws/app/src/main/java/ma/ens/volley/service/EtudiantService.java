package ma.ens.volley.service;

import java.util.ArrayList;
import java.util.List;

import ma.ens.volley.classes.Etudiant;
import ma.ens.volley.dao.IDao;

public class EtudiantService implements IDao<Etudiant> {
    private List<Etudiant> etudiants = new ArrayList<>();
    private int currentId = 1; // ID auto-incrémenté

    @Override
    public Etudiant create(Etudiant o) {
        o.setId(currentId++);
        etudiants.add(o);
        return o;
    }

    @Override
    public Etudiant update(Etudiant o) {
        for (int i = 0; i < etudiants.size(); i++) {
            if (etudiants.get(i).getId() == o.getId()) {
                etudiants.set(i, o);
                return o;
            }
        }
        return null;
    }

    @Override
    public boolean delete(Etudiant o) {
        return etudiants.removeIf(e -> e.getId() == o.getId());
    }

    @Override
    public Etudiant findById(int id) {
        for (Etudiant e : etudiants) {
            if (e.getId() == id) return e;
        }
        return null;
    }

    @Override
    public List<Etudiant> findAll() {
        return etudiants;
    }
}
