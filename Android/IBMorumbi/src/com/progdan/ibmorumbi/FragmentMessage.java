package com.progdan.ibmorumbi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.ListFragment;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class FragmentMessage extends ListFragment {
	private List<Map<String, Object>> messages;
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		String[] from = {"image", "text", "detail"};
		int[] to = {R.id.imageView, R.id.textLabel, R.id.detailTextLabel};
		
		SimpleAdapter adapter = new SimpleAdapter(getActivity(), listMessages(), R.layout.list_row, from, to);
		adapter.setViewBinder(new MyViewBinder());
		
		setListAdapter(adapter);
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

/*
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {
		View view = inflater.inflate(R.layout.tab, container, false);
		return view;
	}
*/
}
