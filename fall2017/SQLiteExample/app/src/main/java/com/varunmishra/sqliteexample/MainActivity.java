package com.varunmishra.sqliteexample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private SQLiteDatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // create our sqlite helper class
        db = new SQLiteDatabaseHandler(this);
        // create some players
        Player player1 = new Player(1, "Lebron James", "F", 203);
        Player player2 = new Player(2, "Kevin Durant", "F", 208);
        Player player3 = new Player(3, "Rudy Gobert", "C", 214);
        Player player7 = new Player(7, "Rudy ", "C", 214);

        // add them
        db.addPlayer(player1);
        db.addPlayer(player2);
        db.addPlayer(player3);
        db.addPlayer(player7);

        player7.setName("Andy");
        db.updatePlayer(player7);
        // list all players
        List<Player> players = db.allPlayers();

        if (players != null) {
            String[] itemsNames = new String[players.size()];

            for (int i = 0; i < players.size(); i++) {
                itemsNames[i] = players.get(i).toString();
            }

            // display like string instances
            ListView list = (ListView) findViewById(R.id.list);
            list.setAdapter(new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1, android.R.id.text1, itemsNames));

        }

    }

}
