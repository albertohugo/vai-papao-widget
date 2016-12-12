package hugo.alberto.vaipapaowidget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RemoteViews;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

import hugo.alberto.vaipapaowidget.Handle.JsonParseHandler;

/**
 * Created by Alberto on 12/12/2016.
 */

public class SimpleWidgetProvider extends AppWidgetProvider {
    private static final String URL = "http://globoesporte.globo.com/servico/equipe/paysandu/jogos.json";

    private TextView campeonato_last;
    private TextView time1_last;
    private TextView time2_last ;
    private TextView placar1_last;
    private TextView placar2_last;
    private TextView informacoes_last;
    private ImageView imageTime1_last;
    private ImageView imageTime2_last;
    private TextView versus_last;

    private String campeonato_jogo_last;
    private String apelido_mandante_last;
    private String apelido_visitante_last;
    private String placar_oficial_mandante_last;
    private String placar_oficial_visitante_last;
    private String escudo_oficial_mandante_last;
    private String escudo_oficial_visitante_last;
    private String dataformatada_last;
    private String  estadio_last;
    private String hora_last;
    private Context contextWidget;
    private RemoteViews remoteViews;
    private int count;
    private int[] appWidget;
    int widgetId;
    AppWidgetManager appWidgetManagerW;
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        contextWidget=context;
        count = appWidgetIds.length;
        appWidget = appWidgetIds;
        appWidgetManagerW=appWidgetManager;
        for (int i = 0; i < count; i++) {
            widgetId = appWidgetIds[i];
            String number = String.format("%03d", (new Random().nextInt(900) + 100));

            remoteViews = new RemoteViews(context.getPackageName(),
                    R.layout.simple_widget);

            new DataFetch(){
                @Override
                protected void onPostExecute(Void aVoid) {
                    super.onPostExecute(aVoid);
                    remoteViews.setTextViewText(R.id.time1_last, apelido_mandante_last);
                    //Picasso.with(contextWidget).load(escudo_oficial_mandante_last).into(R.id.imageCategoria_last);
                    //remoteViews.setImageViewBitmap(R.id.imageCategoria_last,escudo_oficial_mandante_last );
                    Intent intent = new Intent(contextWidget, SimpleWidgetProvider.class);
                    intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
                    intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidget);
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(contextWidget,
                            0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                    remoteViews.setOnClickPendingIntent(R.id.imageCategoria_last, pendingIntent);
                    appWidgetManagerW.updateAppWidget(widgetId, remoteViews);
                }
            }.execute();

        }


    }

    private void readJsonParse(String json_data) {
        if (json_data != null) {
            try {
                JSONObject object_last = new JSONObject(json_data);

                JSONObject mandante_last = object_last.getJSONObject("anterior").getJSONObject("mandante");
                JSONObject visitante_last = object_last.getJSONObject("anterior").getJSONObject("visitante");
                JSONObject jogo_last = object_last.getJSONObject("anterior");
                apelido_mandante_last = mandante_last.getString("apelido");
                apelido_visitante_last = visitante_last.getString("apelido");
                placar_oficial_mandante_last = mandante_last.getString("placar_oficial");
                placar_oficial_visitante_last = visitante_last.getString("placar_oficial");
                escudo_oficial_mandante_last = mandante_last.getJSONObject("escudo").getString("grande");
                escudo_oficial_visitante_last = visitante_last.getJSONObject("escudo").getString("grande");
                campeonato_jogo_last = jogo_last.getString("nome_campeonato").toUpperCase();
                dataformatada_last = jogo_last.getString("data_formatada").toUpperCase();
                estadio_last = jogo_last.getString("estadio").toUpperCase();
                hora_last = jogo_last.getString("hora").toUpperCase();

            } catch (JSONException e) {
                e.printStackTrace();
            }  } else {
            Log.e("ServiceHandler", "Couldn't get any data from the url");
        }

    }

    public class DataFetch extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            JsonParseHandler jsonParseHandler = new JsonParseHandler();
            String json_data = jsonParseHandler.serviceCall(URL, JsonParseHandler.GET);

            Log.d("in inBG()", "fetch data" + json_data);
            readJsonParse(json_data);


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
              FillLastMatch(contextWidget);
        }}

    private void FillLastMatch(Context context) {
       /* campeonato_last.setText(campeonato_jogo_last);
        time1_last.setText(apelido_mandante_last);
        time2_last.setText(apelido_visitante_last);
        if(placar_oficial_mandante_last=="null" &&placar_oficial_visitante_last=="null"){
            placar1_last.setText("");
            placar2_last.setText("");
        }
        else{
            placar1_last.setText(placar_oficial_mandante_last);
            placar2_last.setText(placar_oficial_visitante_last);
        }

        if(dataformatada_last==null ){//&&estadio_last=="null"&&hora_last=="null"){
            informacoes_last.setText("");
            versus_last.setText("");
        }
        else{
            informacoes_last.setText(dataformatada_last + " " + estadio_last + " " + hora_last);
            versus_last.setText(" X ");
        }


        Picasso.with(context).load(escudo_oficial_mandante_last).into(imageTime1_last);
        Picasso.with(context).load(escudo_oficial_visitante_last).into(imageTime2_last);*/
    }
}