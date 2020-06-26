package ar.edu.itba.hci.database.wrapper;

import android.os.AsyncTask;


import java.util.function.Function;

import javax.security.auth.callback.Callback;

import ar.edu.itba.hci.database.DeviceDatabase;

public class DatabaseAsyncTask extends AsyncTask<Void,Void,Void> {


    private Function<Void,Void> onBackground,postExecute;
    public DatabaseAsyncTask(Function<Void,Void> onBackground,Function<Void,Void> postExecute){

        this.onBackground = onBackground;
        this.postExecute = postExecute;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        onBackground.apply(null);
        System.out.println("clear");
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        postExecute.apply(null);
        System.out.println("fetch");
    }
}
