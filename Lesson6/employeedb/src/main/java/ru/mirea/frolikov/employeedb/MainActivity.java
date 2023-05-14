package ru.mirea.frolikov.employeedb;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AppDatabase db = App.getInstance().getDatabase();
        SuperheroDao superheroDao = db.employeeDao();

        Superhero superhero = new Superhero();
        superhero.id = 1;
        superhero.name = "Spider-Man";
        superhero.superpowers = "Spider-webs shooting, wall-climbing";
        superheroDao.insert(superhero);
        superhero = new Superhero();
        superhero.id = 2;
        superhero.name = "Hulk";
        superhero.superpowers = "Superhuman strength";
        superheroDao.insert(superhero);

        superhero = superheroDao.getById(2);
        superhero.superpowers += ", anger empowerment";
        superheroDao.update(superhero);

        List<Superhero> superheroes = superheroDao.getAll();
        for (int i = 0; i < superheroes.size(); i++) {
            Log.d(MainActivity.class.getSimpleName(), "ID: " + superheroes.get(i).id + "; Name: "
                    + superheroes.get(i).name + "; Superpowers: " + superheroes.get(i).superpowers);
        }
    }
}