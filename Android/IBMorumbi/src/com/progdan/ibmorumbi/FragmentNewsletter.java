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

public class FragmentNewsletter extends ListFragment {
	private List<Map<String, Object>> boletins;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		String[] from = {"image", "text", "detail"};
		int[] to = {R.id.imageView, R.id.textLabel, R.id.detailTextLabel};
		
		SimpleAdapter adapter = new SimpleAdapter(getActivity(), listBoletins(), R.layout.list_row, from, to);
		adapter.setViewBinder(new MyViewBinder());
		
		setListAdapter(adapter);
	}
	
	  @Override
	  public void onListItemClick(ListView list, View view, int position, long id) {
	    // do something with the data
	  }

	  private List<Map<String, Object>> listBoletins() {
		  boletins = new ArrayList<Map<String, Object>>();
		  
		  Map<String, Object> item = new HashMap<String, Object>();
		  item.put("image", R.drawable.icon_pdf);
		  item.put("text", "Boletim Informativo");
		  item.put("detail", "16 a 23 de Março de 2014");
		  boletins.add(item);
		  
		  item = new HashMap<String, Object>();
		  item.put("image", R.drawable.icon_pdf);
		  item.put("text", "Boletim Informativo");
		  item.put("detail", "02 a 09 de Março de 2014");
		  boletins.add(item);
		  
		  item = new HashMap<String, Object>();
		  item.put("image", R.drawable.icon_pdf);
		  item.put("text", "Boletim Informativo");
		  item.put("detail", "16 a 23 de Fevereiro de 2014");
		  boletins.add(item);
		  
		  return boletins;
	  }

	
/*	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {
		View view = inflater.inflate(R.layout.tab, container, false);
		TextView textView = (TextView) view.findViewById(R.id.tabtextview);
		textView.setText(R.string.newsletter);
		return view;
	}
*/
}
