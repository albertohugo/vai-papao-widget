package hugo.alberto.vaipapaowidget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

import hugo.alberto.vaipapaowidget.Handle.JsonParseHandler;

/**
 * Created by Alberto on 12/12/2016.
 */

public class SimpleWidgetProvider extends AppWidgetProvider {
    private static final String URL = "http://globoesporte.globo.com/servico/equipe/paysandu/jogos.json";

    private ImageButton imageButton;
    private LinearLayout layout_widget;
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

    private String campeonato_jogo_current;
    private String apelido_mandante_current;
    private String apelido_visitante_current;
    private String placar_oficial_mandante_current;
    private String placar_oficial_visitante_current;
    private String escudo_oficial_mandante_current;
    private String escudo_oficial_visitante_current;
    private String dataformatada_current;
    private String estadio_current;
    private String hora_current;

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

                    remoteViews.setTextViewText(R.id.campeonato_last,campeonato_jogo_current);

                    remoteViews.setTextViewText(R.id.time1_last, apelido_mandante_current);
                    remoteViews.setTextViewText(R.id.time2_last, apelido_visitante_current);
                    if(placar_oficial_mandante_current=="null" &&placar_oficial_visitante_current=="null"){
                        remoteViews.setTextViewText(R.id.placar1_last, "");
                        remoteViews.setTextViewText(R.id.placar2_last, "");
                    }
                    else{
                        remoteViews.setTextViewText(R.id.placar1_last, "");
                        remoteViews.setTextViewText(R.id.placar2_last, "");
                    }

                    if(dataformatada_current==null ){//&&estadio_last=="null"&&hora_last=="null"){
                        remoteViews.setTextViewText(R.id.informacoes_last,"");
                        remoteViews.setTextViewText(R.id.X_last,"");
                    }else{
                        remoteViews.setTextViewText(R.id.informacoes_last,dataformatada_current + " " + estadio_current + " " + hora_current);
                        remoteViews.setTextViewText(R.id.X_last," X ");
                    }

                    Picasso.with(contextWidget)
                            .load(escudo_oficial_mandante_current)
                            .into(remoteViews, R.id.imageCategoria_last, new int[] {widgetId});
                    Picasso.with(contextWidget)
                            .load(escudo_oficial_visitante_current)
                            .into(remoteViews, R.id.imageCategoria2_last, new int[] {widgetId});

                    Intent intent = new Intent(contextWidget, SimpleWidgetProvider.class);
                    intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
                    intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidget);
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(contextWidget,
                            0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                    remoteViews.setOnClickPendingIntent(R.id.imageButton, pendingIntent);
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

                JSONObject object = new JSONObject(json_data);
                JSONArray jsonArray = object.getJSONArray("proximos");

                JSONObject mandante = jsonArray.getJSONObject(0).getJSONObject("mandante");
                JSONObject visitante = jsonArray.getJSONObject(0).getJSONObject("visitante");
                JSONObject jogo = jsonArray.getJSONObject(0);
                apelido_mandante_current = mandante.getString("apelido");
                apelido_visitante_current = visitante.getString("apelido");
                placar_oficial_mandante_current = mandante.getString("placar_oficial");
                placar_oficial_visitante_current = visitante.getString("placar_oficial");
                escudo_oficial_mandante_current = mandante.getJSONObject("escudo").getString("grande");
                escudo_oficial_visitante_current = visitante.getJSONObject("escudo").getString("grande");
                campeonato_jogo_current = jogo.getString("nome_campeonato").toUpperCase();
                dataformatada_current = jogo.getString("data_formatada").toUpperCase();
                estadio_current = jogo.getString("estadio").toUpperCase();
                hora_current = jogo.getString("hora").toUpperCase();


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
        }}
}