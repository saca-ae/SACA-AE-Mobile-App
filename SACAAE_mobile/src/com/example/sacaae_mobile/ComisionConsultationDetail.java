package com.example.sacaae_mobile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import commons.ConstantsFile;
import commons.Professor;

public class ComisionConsultationDetail extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_comision_consultation_detail);
		/* We get the textview by resource id */
		TextView comisionName = (TextView) findViewById(R.id.lblComisionName);
		TextView comisionStartDate = (TextView) findViewById(R.id.lblComisionStartDate);
		TextView comisionEndDate = (TextView) findViewById(R.id.lblComisionEndDate);
		/* we get the info that last activity sent to this activity */
		Bundle bundle = getIntent().getExtras();
		/* set the text on our textviews */
		comisionName.setText(bundle.getString("ComisionName"));
		comisionStartDate.setText(bundle.getString("ComisionStartDate"));
		comisionEndDate.setText(bundle.getString("ComisionEndDate"));

		/* Send the GetComisionProfessor request to WCF service */
		String vComisionID = bundle.getString("ComisionID");

		new ComisionDetailsAsyncTask()
				.execute(ConstantsFile.WCF_SERVICE_ADDRESS
						+ "GetComisionProfessors/" + vComisionID);
	}

	public ArrayList<Professor> FromJSONtoArrayList(String url) {
		ArrayList<Professor> professorList = new ArrayList<Professor>();

		try {
			URL json = new URL(url);
			URLConnection jc = json.openConnection();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					jc.getInputStream()));

			String line = reader.readLine();

			JSONObject jsonResponse = new JSONObject(line);
			JSONArray jsonArray = jsonResponse
					.getJSONArray("GetComisionProfessorsResult");

			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jObject = (JSONObject) jsonArray.get(i);
				Professor vProfessor = new Professor();
				vProfessor.setNombre(jObject.getString("Nombre"));
				professorList.add(vProfessor);
			}

			reader.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return professorList;
	}

	public class ComisionDetailsAsyncTask extends
			AsyncTask<String, Void, ArrayList<Professor>> {

		@Override
		protected ArrayList<Professor> doInBackground(String... url) {
			return FromJSONtoArrayList(url[0]);
		}

		protected void onPostExecute(ArrayList<Professor> professorsList) {
			ListView listView1 = (ListView) findViewById(R.id.ListViewComisionDetail);
			ArrayAdapter<Professor> adapter = new ArrayAdapter<Professor>(
					getApplicationContext(), R.layout.custom_text_view_detail,
					professorsList);
			listView1.setAdapter(adapter);
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.comision_consultation_detail, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
