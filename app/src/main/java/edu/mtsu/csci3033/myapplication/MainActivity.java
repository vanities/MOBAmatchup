package edu.mtsu.csci3033.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Scanner;

public class MainActivity extends Activity {

private EditText    searchEdit;
private String      champQuery;
private boolean fileOpen = true;
private ArrayList<String> champList = new ArrayList<String>();
    private Scanner sc;
    private String foundChampUnparsed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        AssetManager am = this.getAssets();

        try {
            sc = new Scanner(am.open("champs.txt"));
        }catch (java.io.IOException e){
            fileOpen = false;
        }

        if(fileOpen){
            getArrayListChamps();
        }

        searchEdit = (EditText) findViewById(R.id.champSearchEditText);
        Button searchButton = (Button) findViewById(R.id.button);
        searchButton.setOnClickListener(searchListener);

        Button linkButton = (Button) findViewById(R.id.linkButton);
        linkButton.setOnClickListener(linkListener);


    }

    View.OnClickListener linkListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://leagueoflegends.wikia.com/wiki/"+champQuery));
            startActivity(browserIntent);
        }
    };

    public void getArrayListChamps(){
        while (sc.hasNextLine()){

            String champInfo = sc.nextLine();

            champList.add(champInfo);

        }

    }

    View.OnClickListener searchListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            champQuery = searchEdit.getText().toString();
            if(champQueryFunc(champQuery)){
                champParse();
                searchEdit.setText("");
            }
            else {
                AlertDialog.Builder builder =
                        new AlertDialog.Builder(MainActivity.this);

                builder.setMessage(R.string.missingMessage);

                builder.setPositiveButton("OK", null);

                AlertDialog errorDialog = builder.create();
                errorDialog.show();
                searchEdit.setText("");
            }

            InputMethodManager imm = (InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    };

    public void champParse(){
        String[] champParsed = foundChampUnparsed.split("/");

        TextView champNameView = (TextView) findViewById(R.id.champNameText);
        champNameView.setText(champParsed[0]);

        TextView primaryView = (TextView) findViewById(R.id.primaryView);
        primaryView.setText(champParsed[1]);

        TextView secondaryView = (TextView) findViewById(R.id.secondaryView);
        secondaryView.setText(champParsed[2]);

        TextView counterView = (TextView) findViewById(R.id.counterList);
        counterView.setText(champParsed[3]);

        TextView laneView = (TextView) findViewById(R.id.laneView);
        laneView.setText(champParsed[4]);

        ImageView champPic = (ImageView) findViewById(R.id.imageView);
        String champBase = "drawable/" + champParsed[5] + "square";
        String lowerChampBase = champBase.toLowerCase();
        int imageResource = getResources().getIdentifier(lowerChampBase, null, getPackageName());
        Drawable image = getResources().getDrawable(imageResource);
        champPic.setImageDrawable(image);
        //champPic.setImageResource(R.drawable.);
        //champPic.setImageDrawable(getResources().getDrawable(R.drawable.d));

    }

    public boolean champQueryFunc(String champion){

        boolean flag =  false;

        for(int i = 0; i < champList.size(); i++){
            String champName = champList.get(i);
            String searchString = champName.substring(0,champName.indexOf(" ")).toLowerCase();
            if(champion.toLowerCase().equals(searchString)){
                flag = true;
                foundChampUnparsed = champName;
                break;
            }
        }

        return flag;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
