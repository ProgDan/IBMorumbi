package com.progdan.ibmorumbi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.progdan.ibmorumbi.json.JSONParser;

import android.app.ListFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class FragmentMessage extends ListFragment {
	private List<Map<String, Object>> messages;
	private static String url = "http://mini.progdan.com/ibmorumbi/messages.php";
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		new ProgressTask(FragmentMessage.this).execute();
	}
	
	  @Override
	  public void onListItemClick(ListView list, View view, int position, long id) {
	    // do something with the data
	  }
	  
	  private List<Map<String, Object>> listMessages() {
		  messages = new ArrayList<Map<String, Object>>();
		  
		  Map<String, Object> item = new HashMap<String, Object>();
		  item.put("image", "http://www.ibmorumbi.com.br/pastores/imagens/pastor_lisanias.jpg");
		  item.put("text", "Reconstruindo a Esperança");
		  item.put("detail", "Pr. Lisânias Moura");
		  messages.add(item);
		  
		  item = new HashMap<String, Object>();
		  item.put("image", "http://www.ibmorumbi.com.br/pastores/imagens/pastor_lisanias.jpg");
		  item.put("text", "Um dia a casa Cai");
		  item.put("detail", "Pr. Lisânias Moura");
		  messages.add(item);
		  
		  item = new HashMap<String, Object>();
		  item.put("image", "http://www.ibmorumbi.com.br/pastores/imagens/samuel_mendes.jpg");
		  item.put("text", "Em busca da Felicidade");
		  item.put("detail", "Pr. Samuel Mendes");
		  messages.add(item);
		  
		  return messages;
	  }
	  
	  private class ProgressTask extends AsyncTask<String, Void, Boolean> {
		  private ProgressDialog dialog;		  
		  private Context context;
		  
		  public ProgressTask(ListFragment fragment) {
			  context = fragment.getActivity();
			  dialog = new ProgressDialog(context);
		  }
		  
		  protected void onPreExecute() {
			  this.dialog.setMessage("Progress start");
			  this.dialog.show();
		  }
		  
		  @Override
		  protected void onPostExecute(final Boolean success) {
			  if (dialog.isShowing()) {
				  dialog.dismiss();
			  }
			  
			  String[] from = {"image", "text", "detail"};
			  int[] to = {R.id.imageView, R.id.textLabel, R.id.detailTextLabel};
				
			  SimpleAdapter adapter = new SimpleAdapter(context, messages, R.layout.list_row, from, to);
			  adapter.setViewBinder(new MyViewBinder());
				
			  setListAdapter(adapter);
		  }
		  
		  @Override
		  protected Boolean doInBackground(String... args) {
			  messages = new ArrayList<Map<String, Object>>();
			  JSONParser jParser = new JSONParser();
			  
			  // get JSON data from URL
			  JSONArray json = jParser.getJSONFromUrl(url);
			  
			  for (int i = 0; i < json.length(); i++) {
				  try {
					  JSONObject c = json.getJSONObject(i);
					  
					  String data = c.getString("data");
					  String detail = c.getString("pregador");
					  Object image = c.getString("pregador_img");
					  String cultos = c.getString("cultos");
					  String text = c.getString("tema");
					  String passagens = c.getString("passagens");
					  String audio = c.getString("audio");
					  String video = c.getString("video");
					  
					  
					  HashMap<String, Object> map = new HashMap<String, Object>();
					  
					  // Add child node to HashMap key & value
					  map.put("data", data);
					  map.put("detail", detail);
					  map.put("image", image);
					  map.put("cultor", cultos);
					  map.put("text", text);
					  map.put("passagens", passagens);
					  map.put("audio", audio);
					  map.put("video", video);
					  messages.add(map);
					  
				  } catch (JSONException e) {
					  e.printStackTrace();
				  }
			  }
			  
			  return null;
		  }
	 }
}
